package com.uniware.integrations.shipment.v1.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.unicommerce.platform.integration.Error;
import com.unicommerce.platform.integration.shipment.models.Shipment;
import com.unicommerce.platform.integration.shipment.models.ShipmentResponse;
import com.unicommerce.platform.integration.shipment.models.ShipmentTracking;
import com.unicommerce.platform.integration.shipment.models.request.DispatchShipmentRequest;
import com.unicommerce.platform.integration.shipment.models.request.InvoiceCreateRequest;
import com.unicommerce.platform.integration.shipment.models.request.LabelCreateRequest;
import com.unicommerce.platform.integration.shipment.models.response.DispatchShipmentResponse;
import com.unicommerce.platform.integration.shipment.models.response.InvoiceCreateResponse;
import com.unicommerce.platform.integration.shipment.models.response.LabelCreateResponse;
import com.unifier.core.utils.DateUtils;
import com.unifier.core.utils.JsonUtils;
import com.unifier.core.utils.PdfUtils;
import com.unifier.core.utils.StringUtils;
import com.unicommerce.platform.aws.S3Service;
import com.uniware.integrations.client.annotation.FlipkartClient;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.dto.ConfirmItemRow;
import com.uniware.integrations.client.dto.Dimensions;
import com.uniware.integrations.client.dto.DispatchRequest;
import com.uniware.integrations.client.dto.DispatchShipmentStatus;
import com.uniware.integrations.client.dto.Invoice;
import com.uniware.integrations.client.dto.OrderItem;
import com.uniware.integrations.client.dto.PackRequest;
import com.uniware.integrations.client.dto.SerialNumber;
import com.uniware.integrations.client.dto.ShipmentDetails;
import com.uniware.integrations.client.dto.SubShipments;
import com.uniware.integrations.client.dto.TaxItem;
import com.uniware.integrations.client.dto.api.requestDto.DispatchSelfShipmentV3Request;
import com.uniware.integrations.client.dto.api.requestDto.DispatchStandardShipmentV3Request;
import com.uniware.integrations.client.dto.api.requestDto.ShipmentPackV3Request;
import com.uniware.integrations.client.dto.api.responseDto.DispatchShipmentV3Response;
import com.uniware.integrations.client.dto.api.responseDto.InvoiceDetailsV3Response;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentDetailsV3WithAddressResponse;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentDetailsV3WithSubPackages;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentPackV3Response;
import com.uniware.integrations.flipkart.services.FlipkartHelperService;
import com.uniware.integrations.flipkart.services.FlipkartSellerApiService;
import com.uniware.integrations.shipment.v1.services.IShipmentService;
import com.unicommerce.platform.web.context.TenantRequestContext;
import com.uniware.integrations.web.exception.BadRequest;
import com.uniware.integrations.web.exception.FailureResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.uniware.integrations.client.constants.flipkartConstants.BUCKET_NAME;
import static com.uniware.integrations.client.constants.flipkartConstants.INVOICE;
import static com.uniware.integrations.client.constants.flipkartConstants.INVOICE_LABEL;
import static com.uniware.integrations.client.constants.flipkartConstants.LABEL;
import static com.uniware.integrations.client.constants.flipkartConstants.SUCCESS;

@Service
@FlipkartClient(module = "SHIPMENT", version = "v1", channelSource = ChannelSource.FLIPKART_DROPSHIP)
public class dropshipShipmentServiceImpl implements IShipmentService {

    @Autowired FlipkartHelperService flipkartHelperService;
    @Autowired private FlipkartSellerApiService flipkartSellerApiService;
    @Autowired private S3Service s3Service;
    private static final Logger LOGGER = LoggerFactory.getLogger(dropshipShipmentServiceImpl.class);

    @Override public InvoiceCreateResponse createInvoice(InvoiceCreateRequest invoiceCreateRequest) {

        InvoiceCreateResponse invoiceCreateResponse = new InvoiceCreateResponse();

        if ( invoiceCreateRequest.getShipments().size() > 20)
            throw new BadRequest("shipment count should not be greater than 20");

        Map<String, Shipment> shipmentIdToUniwareShipment = new HashMap<>();
        Map<String, com.uniware.integrations.client.dto.Shipment> shipmentIdToFlipkartShipment = new HashMap<>();
        List<String> channelShippingShipments = new ArrayList<>();
        List<String> selfShippingShipments = new ArrayList<>();
        List<String> packingInProgressShipments = new ArrayList<>();
        List<String> packedShipments = new ArrayList<>();
        List<String> approvedShipments = new ArrayList<>();
        Map<String,Error> failedShipmentToError = new HashMap<>();

        invoiceCreateRequest.getShipments().forEach(shipment -> {
            String shipmentId = getShipmentIdFromAdditionalInfo(shipment.getAdditionalInfo());
            shipmentIdToUniwareShipment.put(shipmentId,shipment);
           switch ( shipment.getShippingManager()) {
           case UNIWARE:
               selfShippingShipments.add(shipmentId);
               break;
           case CHANNEL:
               channelShippingShipments.add(shipmentId);
               break;
           default:
               LOGGER.error("invalid shippingManager, shippingManager {}", shipment.getShippingManager().toString());
               throw new FailureResponse("invalid shipping manager");
           }
        });

        ShipmentDetailsV3WithSubPackages flipkartShipmentDetails = flipkartSellerApiService.getShipmentDetailsWithSubPackages(channelShippingShipments);
        if (flipkartShipmentDetails.getShipments() == null) {
            LOGGER.error("shipment list is null in ShipmentDetailsV3WithSubPackages response");
            throw new FailureResponse("shipment list is null in ShipmentDetailsV3WithSubPackages response");
        }

        flipkartShipmentDetails.getShipments().forEach(fkShipment -> {
            List<OrderItem> orderItems = fkShipment.getOrderItems();
            if ( orderItems.stream().anyMatch(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("approved")) ){
                approvedShipments.add(fkShipment.getShipmentId());
            }
            else if ( orderItems.stream().anyMatch(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("PACKING_IN_PROGRESS")) ) {
                packingInProgressShipments.add(fkShipment.getShipmentId());
            }
            else if ( orderItems.stream().anyMatch(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("PACKED")) ){
                packedShipments.add(fkShipment.getShipmentId());
            }

            shipmentIdToFlipkartShipment.put(fkShipment.getShipmentId(), fkShipment);
            orderItems.forEach(orderItem -> LOGGER.info("Shipment:{} order item id:{}, listingId:{}, status :{}, quantity:{} ", fkShipment.getShipmentId(), orderItem.getOrderItemId(),orderItem.getListingId(), orderItem.getStatus(), orderItem.getQuantity()));

        });

        if ( packedShipments.size() != 0) {
            packShipments(approvedShipments, shipmentIdToUniwareShipment, shipmentIdToFlipkartShipment, failedShipmentToError);
        }

        packingInProgressShipments.addAll(approvedShipments);
        List<String> packConfirmedShipments = isPackConfirmed(packingInProgressShipments);

        packedShipments.addAll(packConfirmedShipments);

        String invoiceSize = (String) invoiceCreateRequest.getMetadata().get("invoiceSize");
        String labelSize = (String) invoiceCreateRequest.getMetadata().get("labelSize");
        Map<String, Shipment> shipmentIdToResponseShipments = getInvoiceAndCourierInfo(packedShipments, shipmentIdToUniwareShipment, invoiceSize, labelSize, failedShipmentToError);

        invoiceCreateRequest.getShipments().forEach( shipment -> {
            String shipmentId = getShipmentIdFromAdditionalInfo(shipment.getAdditionalInfo());
            ShipmentResponse shipmentResponse = new ShipmentResponse();
            if (  selfShippingShipments.contains(shipmentId)) {
                Shipment responseShipment = new Shipment();
                responseShipment.setCode(shipment.getCode());
                com.unicommerce.platform.integration.shipment.models.Invoice invoice = new com.unicommerce.platform.integration.shipment.models.Invoice();
                invoice.setThirdPartyInvoicingNotAvailable(true);
                responseShipment.setInvoice(invoice);
                shipmentResponse.setShipment(responseShipment);
                shipmentResponse.setSuccessful(true);
                invoiceCreateResponse.addShipmentResponse(shipmentResponse);
            }
            else if ( packedShipments.contains(shipmentId)) {
                shipmentResponse.setShipment(shipmentIdToResponseShipments.get(shipmentId));
                shipmentResponse.setSuccessful(true);
                invoiceCreateResponse.addShipmentResponse(shipmentResponse);
            }
            else if ( failedShipmentToError.containsKey(shipmentId)) {
                Shipment responseShipment = new Shipment();
                responseShipment.setCode(shipment.getCode());
                shipmentResponse.setShipment(responseShipment);
                shipmentResponse.setSuccessful(false);
                shipmentResponse.setErrors(Collections.singletonList(failedShipmentToError.get(shipmentId)));
                invoiceCreateResponse.addShipmentResponse(shipmentResponse);
            }
            else {
                Shipment responseShipment = new Shipment();
                responseShipment.setCode(shipment.getCode());
                shipmentResponse.setShipment(responseShipment);
                shipmentResponse.setSuccessful(false);
                shipmentResponse.setErrors(Collections.singletonList(new Error( "", "shipment not present in self, packed, failed shipment list. Kindly check support team")));
                invoiceCreateResponse.addShipmentResponse(shipmentResponse);
            }

        });
        return invoiceCreateResponse;
    }

    @Override public LabelCreateResponse createLabel(LabelCreateRequest labelCreateRequest) {

        LabelCreateResponse labelCreateResponse = new LabelCreateResponse();

        Shipment shipmentReq = labelCreateRequest.getShipments().get(0);
        String shipmentId = getShipmentIdFromAdditionalInfo(shipmentReq.getAdditionalInfo());

        ShipmentResponse shipmentResponse = new ShipmentResponse();
        Shipment shipment = new Shipment();
        shipment.setCode(shipmentReq.getCode());

        ShipmentTracking shipmentTracking = getCourierInfo(Collections.singletonList(shipmentId)).get(shipmentId);

        String filePath = flipkartHelperService.getFilePath(INVOICE_LABEL,null);
        boolean isInvoiceLabelDownloaded = flipkartSellerApiService.downloadInvoiceAndLabel(shipmentId,filePath);

        if (isInvoiceLabelDownloaded) {
            String labelSize = (String) labelCreateRequest.getMetadata().get("labelSize");
            String labelS3URL;
            if(StringUtils.isNotBlank(labelSize) && !("A4_FK_Label+Invoice").equals(labelSize)){
                labelS3URL = formatLabel(labelSize, filePath);
            }else{
                labelS3URL = s3Service.uploadFile(new File(filePath),BUCKET_NAME);
            }

            ShipmentTracking.Label label = ShipmentTracking.Label.builder().url(labelS3URL).format("PDF").build();
            shipmentTracking.setLabel(label);
            shipment.setTracking(shipmentTracking);
            shipmentResponse.setShipment(shipment);
            shipmentResponse.setSuccessful(true);


        } else {
            shipmentResponse.setShipment(shipment);
            shipmentResponse.setSuccessful(false);
            shipmentResponse.setErrors(Collections.singletonList(new Error("","failed to download shipping label")));
        }

        labelCreateResponse.addShipmentResponse(shipmentResponse);
        return labelCreateResponse;
    }

    @Override public DispatchShipmentResponse dispatchShipment(DispatchShipmentRequest dispatchShipmentRequest) {
        DispatchShipmentResponse dispatchShipmentResponse = new DispatchShipmentResponse();

        DispatchShipmentV3Response dispatchShipmentV3Response;

        switch ( dispatchShipmentRequest.getShipment().getShippingManager() ) {
        case CHANNEL:
            DispatchStandardShipmentV3Request dispatchStandardShipmentV3Request = prepareDispatchStandardShipmentRequest(dispatchShipmentRequest);
            dispatchShipmentV3Response = flipkartSellerApiService.markStandardFulfilmentShipmentsRTD(dispatchStandardShipmentV3Request);
            break;
        case UNIWARE:
            DispatchSelfShipmentV3Request dispatchSelfShipmentV3Request = prepareDispatchSelfShipmentRequest(dispatchShipmentRequest);
            dispatchShipmentV3Response = flipkartSellerApiService.markSelfShipDispatch(dispatchSelfShipmentV3Request);
            break;
        default:
            LOGGER.error("invalid shipping manager");
            throw new FailureResponse("invalid shipping manager");
        }

        if ( dispatchShipmentV3Response == null ) {
            throw new FailureResponse("empty or null response from flipkart");
        }

        DispatchShipmentStatus flipkartShipmentResponse = dispatchShipmentV3Response.getShipments().get(0);
        if ( SUCCESS.equalsIgnoreCase(flipkartShipmentResponse.getStatus())) {
            dispatchShipmentResponse.setSuccessful(true);
        } else {
            dispatchShipmentResponse.setSuccessful(false);
            dispatchShipmentResponse.setErrors(Collections.singletonList(new Error(flipkartShipmentResponse.getErrorCode(), flipkartShipmentResponse.getErrorMessage())));
        }

        return dispatchShipmentResponse;
    }

    // TODO : Error code
    private Map<String,Shipment> getInvoiceAndCourierInfo(List<String> packedShipments, Map<String,Shipment> shipmentIdToUniwareShipment, String invoiceSize, String labelSize,
            Map<String, Error> failedShipmentToError) {

        Map<String,Shipment> shipmentIdToResponseShipments = getInvoiceInfo(packedShipments, shipmentIdToUniwareShipment);
        Map<String,ShipmentTracking> shipmentIdToShipmentTracking = getCourierInfo(packedShipments);

        packedShipments.forEach( shipmentId -> {

            String filePath = flipkartHelperService.getFilePath(INVOICE_LABEL,null);
            boolean isInvoiceLabelDownloaded = flipkartSellerApiService.downloadInvoiceAndLabel(shipmentId,filePath);

            if (isInvoiceLabelDownloaded) {
                Shipment responseShipment = shipmentIdToResponseShipments.get(shipmentId);

                if(StringUtils.isNotBlank(invoiceSize) && !("Default_UC_Invoice").equals(invoiceSize)){
                    String invoiceUrl = formatInvoice(invoiceSize, filePath);
                    responseShipment.getInvoice().setUrl(invoiceUrl);
                }

                String labelS3URL;
                if(StringUtils.isNotBlank(labelSize) && !("A4_FK_Label+Invoice").equals(labelSize)){
                    labelS3URL = formatLabel(labelSize, filePath);
                }else{
                    labelS3URL = s3Service.uploadFile(new File(filePath),BUCKET_NAME);
                }


                ShipmentTracking shipmentTracking = shipmentIdToShipmentTracking.get(shipmentId);
                if ( shipmentTracking != null ){
                    ShipmentTracking.Label.builder().url(labelS3URL).format("PDF").build();
                    responseShipment.setTracking(shipmentIdToShipmentTracking.get(shipmentId));
                }

            } else {
                failedShipmentToError.put(shipmentId,new Error("","Unable to download InvoiceLabel document "));
            }
        });
        return shipmentIdToResponseShipments;
    }

    private String formatLabel(String labelSize, String filePath) {
        String labelOutFilePath = flipkartHelperService.getFilePath(LABEL,null);
        List<String> paths = new ArrayList<>();
        paths.add(filePath);

        try {
            if(labelSize.equals("A4_TO_B7")){
                PdfUtils.cropPDF(PageSize.B7,false,paths,labelOutFilePath,172,115,0,-1);
            }else if(labelSize.equals("A4_TO_B7_LARGE")){
                PdfUtils.cropPDF(new Rectangle(260F, 375F),false,paths,labelOutFilePath,165,80,0,-1);
            }
        }
        catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
        return s3Service.uploadFile(new File(labelOutFilePath),BUCKET_NAME);
    }

    private String formatInvoice(String invoiceSize, String filePath) {

        String invoiceOutFilePath = flipkartHelperService.getFilePath(INVOICE,null);
        List<String> paths = new ArrayList<>();
        paths.add(filePath);

        try {
            switch (invoiceSize) {
            case "A4_TO_CrownOctavo":
                PdfUtils.cropPDF(PageSize.CROWN_OCTAVO, true, paths, invoiceOutFilePath, 30, 125, 0, 0);
                break;
            case "CrownOctavo_LANDSCAPE_TO_PORTRAIT": {
                String tempInvoicePath =
                        "/tmp/" + TenantRequestContext.current().getHttpSenderIdentifier() + "-"
                                + UUID.randomUUID() + ".pdf";
                PdfUtils.cropPDF(PageSize.CROWN_OCTAVO, true, paths, tempInvoicePath, 30, 125, 0, 0);
                paths.clear();
                paths.add(tempInvoicePath);
                PdfUtils.cropPDF(PageSize.CROWN_OCTAVO, false, paths, invoiceOutFilePath, 0, 0, 0, 0);
                break;
            }
            case "LargeCrownOctavo_LANDSCAPE_TO_PORTRAIT": {
                String tempInvoicePath =
                        "/tmp/" + TenantRequestContext.current().getHttpSenderIdentifier() + "-"
                                + UUID.randomUUID() + ".pdf";
                PdfUtils.cropPDF(PageSize.LARGE_CROWN_OCTAVO, true, paths, tempInvoicePath, 30, 80, 0, 0);
                paths.clear();
                paths.add(tempInvoicePath);
                PdfUtils.cropPDF(PageSize.LARGE_CROWN_OCTAVO, false, paths, invoiceOutFilePath, 0, 0, 0, 0);
                break;
            }
            case "A4_TO_LargeCrownOctavo":
                PdfUtils.cropPDF(PageSize.LARGE_CROWN_OCTAVO, true, paths, invoiceOutFilePath, 30, 80, 0, 0);
                break;
            }
        }
        catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }

        return s3Service.uploadFile(new File(invoiceOutFilePath), BUCKET_NAME);
    }

    private Map<String,ShipmentTracking> getCourierInfo(List<String> packedShipments) {

        Map<String,ShipmentTracking> shipmentIdToShipmentTracking = new HashMap<>();
        ShipmentDetailsV3WithAddressResponse shipmentDetailsWithAddress = flipkartSellerApiService.getShipmentDetailsWithAddress(packedShipments);

        shipmentDetailsWithAddress.getShipments().forEach( fkShipmentAddresDetails -> {
            Optional<ShipmentDetails.SubShipment> subShipment = fkShipmentAddresDetails.getSubShipments()
                    .stream().filter(ss -> ("SS-1").equalsIgnoreCase(ss.getSubShipmentId())).findFirst();
            if ( subShipment.isPresent() ) {
                ShipmentTracking shipmentTracking = new ShipmentTracking();
                ShipmentDetails.SubShipment subShipmentDetails = subShipment.get();
                shipmentTracking.setCourier(subShipmentDetails.getCourierDetails().getPickupDetails().getVendorName());
                shipmentTracking.setAwb(subShipmentDetails.getCourierDetails().getPickupDetails().getTrackingId());
                shipmentIdToShipmentTracking.put(fkShipmentAddresDetails.getShipmentId(),shipmentTracking);
            }
        });
        return shipmentIdToShipmentTracking;
    }

    private Map<String,Shipment> getInvoiceInfo(List<String> packedShipments, Map<String, Shipment> shipmentIdToUniwareShipment) {

        Map<String,Shipment> shipmentIdToResponseShipments = new HashMap<>();

        InvoiceDetailsV3Response invoiceDetailsV3Response = flipkartSellerApiService.getInvoicesInfo(packedShipments);
        invoiceDetailsV3Response.getInvoices().forEach( fkInvoiceDetails -> {
            Shipment uniwareShipment = shipmentIdToUniwareShipment.get(fkInvoiceDetails.getShipmentId());
            Map<String,String> lineItemCodeToChannelProductId = new HashMap<>();
            uniwareShipment.getLineItems().forEach(lineItem -> lineItemCodeToChannelProductId.put(lineItem.getCode(),lineItem.getChannelProductId()));

            Shipment shipment = new Shipment();
            shipment.setCode(uniwareShipment.getCode());
            shipment.setCode(uniwareShipment.getCode());
            com.unicommerce.platform.integration.shipment.models.Invoice invoice = new com.unicommerce.platform.integration.shipment.models.Invoice();
            if (StringUtils.isNotBlank(uniwareShipment.getInvoice().getCode()))
                invoice.setCode(uniwareShipment.getInvoice().getCode().split("-")[1]);
            else
                invoice.setCode(fkInvoiceDetails.getInvoiceNumber());

            invoice.setDisplayCode(fkInvoiceDetails.getInvoiceNumber());
            invoice.setCreated(DateUtils.dateToString(DateUtils.stringToDate(fkInvoiceDetails.getInvoiceDate(),"yyyy-MM-dd"),"yyyy-MM-dd"));
            com.unicommerce.platform.integration.shipment.models.Invoice.TaxInformation taxInformation = new com.unicommerce.platform.integration.shipment.models.Invoice.TaxInformation();
            for ( Invoice.OrderItem orderItem : fkInvoiceDetails.getOrderItems()) {
                com.unicommerce.platform.integration.shipment.models.Invoice.ProductTax productTax = new com.unicommerce.platform.integration.shipment.models.Invoice.ProductTax();
                productTax.setChannelProductId(lineItemCodeToChannelProductId.get(orderItem.getOrderItemId().replaceAll("\n","")));
                productTax.setCentralGst(orderItem.getTaxDetails().getCgstRate() != null ? orderItem.getTaxDetails().getCgstRate() : BigDecimal.ZERO);
                productTax.setStateGst(orderItem.getTaxDetails().getSgstRate() != null ? orderItem.getTaxDetails().getSgstRate() : BigDecimal.ZERO);
                productTax.setIntegratedGst(orderItem.getTaxDetails().getIgstRate() != null ? orderItem.getTaxDetails().getIgstRate() : BigDecimal.ZERO);
                productTax.setCompensationCess(orderItem.getTaxDetails().getCessRate() != null ? orderItem.getTaxDetails().getUtgstRate() : BigDecimal.ZERO);
                productTax.setUnionTerritoryGst(orderItem.getTaxDetails().getUtgstRate() != null ? orderItem.getTaxDetails().getUtgstRate() : BigDecimal.ZERO);
                taxInformation.addProductTax(productTax);
            }
            invoice.setTaxInformation(taxInformation);
            shipment.setInvoice(invoice);
            shipmentIdToResponseShipments.put(fkInvoiceDetails.getShipmentId(),shipment);
        });
        return shipmentIdToResponseShipments;
    }

    private List<String> isPackConfirmed(List<String> packingInProgressShipments) {
        List<String> packedShipments = new ArrayList<>();
        int retryCount = 10;
        while (--retryCount > 0 && packingInProgressShipments.size() > 0 ) {
            try {
                LOGGER.info("Retry attempt : {}", 10 - retryCount);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ShipmentDetailsV3WithSubPackages flipkartShipmentDetails = flipkartSellerApiService.getShipmentDetailsWithSubPackages(packingInProgressShipments);
            flipkartShipmentDetails.getShipments().forEach(fkShipment -> {
                List<OrderItem> orderItems = fkShipment.getOrderItems();
                if ( orderItems.stream().anyMatch(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("PACKED")) ) {
                    packedShipments.add(fkShipment.getShipmentId());
                    packingInProgressShipments.remove(fkShipment.getShipmentId());
                }
            });
        }
        return packedShipments;
    }

    private void packShipments(List<String> approvedShipments, Map<String, Shipment> shipmentIdToUniwareShipment, Map<String,com.uniware.integrations.client.dto.Shipment> shipmentIdToFlipkartShipment , Map<String,Error> failedShipmentToError) {

        ShipmentPackV3Request shipmentPackV3Request = new ShipmentPackV3Request();
        approvedShipments.forEach( shipmentId -> shipmentPackV3Request.addShipmentsItem(preparePackShipmentRequest(shipmentIdToUniwareShipment.get(shipmentId), shipmentIdToFlipkartShipment.get(shipmentId))));

        ShipmentPackV3Response shipmentPackV3Response = flipkartSellerApiService.packShipment(shipmentPackV3Request);
        List<String> retryableErrorShipments = new ArrayList<>();
        shipmentPackV3Response.getShipments().forEach(packShipmentStatus -> {
            if ( "FAILURE".equalsIgnoreCase(packShipmentStatus.getStatus()) ) {
                if ( (packShipmentStatus.getErrorMessage().contains("Please re-check your packaging") || packShipmentStatus.getErrorMessage().contains("dimensions mentioned in My Listings tab at the time of order placement")) ) {
                    retryableErrorShipments.add(packShipmentStatus.getShipmentId());
                }
                else {
                    failedShipmentToError.put(packShipmentStatus.getShipmentId(), new  Error(packShipmentStatus.getErrorCode(),packShipmentStatus.getErrorMessage()));
                }
            }
        });

        if ( retryableErrorShipments.size() > 0 ) {
            ShipmentPackV3Request retryshipmentPackV3Request = new ShipmentPackV3Request();
            shipmentPackV3Request.getShipments().forEach( packRequest -> {
                if ( retryableErrorShipments.contains(packRequest.getShipmentId())){
                    packRequest.getSubShipments().get(0).setDimensions(getPackDimension(shipmentIdToUniwareShipment.get(packRequest.getShipmentId())));
                    retryshipmentPackV3Request.addShipmentsItem(packRequest);
                }
            });

            shipmentPackV3Response = flipkartSellerApiService.packShipment(shipmentPackV3Request);
            shipmentPackV3Response.getShipments().forEach(packShipmentStatus -> {
                if ( "FAILURE".equalsIgnoreCase(packShipmentStatus.getStatus()) ) {
                    failedShipmentToError.put(packShipmentStatus.getShipmentId(), new  Error(packShipmentStatus.getErrorCode(),packShipmentStatus.getErrorMessage()));
                }
            });

        }
    }

    private PackRequest preparePackShipmentRequest(Shipment uniwareShipment, com.uniware.integrations.client.dto.Shipment flipkartShipment) {
        PackRequest packRequest = new PackRequest();
        Dimensions dimensions = null;

        try {
            dimensions = flipkartShipment.getSubShipments().get(0).getPackages().get(0).getDimensions();
        } catch (NullPointerException ex) {
            LOGGER.error("Getting NPE while fetching dimensions");
        }

        uniwareShipment.getLineItems().forEach(lineItem -> {
            TaxItem taxItem = new TaxItem();
            taxItem.orderItemId(lineItem.getCode());
            taxItem.setQuantity(lineItem.getQuantity());
            taxItem.setTaxRate(BigDecimal.ZERO);
            packRequest.addTaxItemsItem(taxItem);

            // TODO : SERIAL NUMBER HANDLING
            // SerialNumber/imei handling
            List<String> serialNumberList = getSerialList(lineItem.getItemDetails());
            if( !serialNumberList.isEmpty() ) {
                SerialNumber serialNumber = new SerialNumber();
                serialNumber.addSerialNumbersItem(serialNumberList);
                serialNumber.setOrderItemId(lineItem.getCode());
                packRequest.addSerialNumbersItem(serialNumber);
            }
        });


        SubShipments subShipment = new SubShipments().subShipmentId("SS-1").dimensions(dimensions != null ? dimensions : getPackDimension(uniwareShipment));
        Invoice invoice = new Invoice();
        invoice.setOrderId(flipkartShipment.getOrderItems().get(0).getOrderId());
        invoice.setInvoiceNumber("");
        invoice.setInvoiceDate(DateUtils.dateToString(DateUtils.getCurrentDate(),"yyyy-MM-dd"));

        packRequest.setShipmentId(flipkartShipment.getShipmentId());
        packRequest.setLocationId(FlipkartRequestContext.current().getLocationId());
        packRequest.addSubShipmentsItem(subShipment);
        packRequest.addInvoicesItem(invoice);
        return packRequest;
    }

    private Dimensions getPackDimension(Shipment shipment) {
        Dimensions dimensions = new Dimensions();
        if (shipment.getDimensions().getLength().compareTo(BigDecimal.ONE) > 0) {
            dimensions.setLength(shipment.getDimensions().getLength().divide(BigDecimal.TEN));
            dimensions.setBreadth(shipment.getDimensions().getWidth().divide(BigDecimal.TEN));
            dimensions.setHeight(shipment.getDimensions().getHeight().divide(BigDecimal.TEN));
            dimensions.setWeight(shipment.getWeight().divide(BigDecimal.valueOf(1000)));
        }
        else {
            dimensions.defaultDimensions();
        }
        return dimensions;
    }

    private List<String> getSerialList(String itemDetails) {
        List<String> serialList = new ArrayList<>();
        if (StringUtils.isNotBlank(itemDetails)) {
            JsonObject jsonObject = new Gson().fromJson(itemDetails, JsonObject.class);
            String serialNumbers = jsonObject.get("imei") != null ? jsonObject.get("imei").getAsString() : (jsonObject.get("serialNumber") != null ? jsonObject.get("serialNumber").getAsString() : null);
            if (serialNumbers != null) {
                serialList = StringUtils.split(serialNumbers);
            }
        }
        return serialList;
    }

    private DispatchStandardShipmentV3Request prepareDispatchStandardShipmentRequest(DispatchShipmentRequest dispatchShipmentRequest) {
        Shipment shipment = dispatchShipmentRequest.getShipment();
        DispatchStandardShipmentV3Request dispatchStandardShipmentV3Request = new DispatchStandardShipmentV3Request();
        dispatchStandardShipmentV3Request.addShipmentId(getShipmentIdFromAdditionalInfo(shipment.getAdditionalInfo()))
                .setLocationId(FlipkartRequestContext.current().getLocationId());
        return dispatchStandardShipmentV3Request;
    }

    private DispatchSelfShipmentV3Request prepareDispatchSelfShipmentRequest(DispatchShipmentRequest dispatchShipmentRequest ) {
        Shipment shipment = dispatchShipmentRequest.getShipment();
        DispatchSelfShipmentV3Request dispatchSelfShipmentV3Request = new DispatchSelfShipmentV3Request();

        DispatchRequest.Invoice invoice = new DispatchRequest.Invoice();
        invoice.setInvoiceNumber(shipment.getInvoice().getCode());
        invoice.setInvoiceDate(DateUtils.dateToString(DateUtils.stringToDate(shipment.getInvoice().getCreated(), "yyyy-MM-dd"),"yyyy-MM-dd"));
        for (Shipment.LineItem lineItem  : shipment.getLineItems()) {
            ConfirmItemRow confirmItemRow = new ConfirmItemRow();
            confirmItemRow.setOrderItemId(lineItem.getCode());
            confirmItemRow.setQuantity(lineItem.getQuantity());
            confirmItemRow.serialNumbers(new ArrayList<>());
            invoice.addOrderItemsItem(confirmItemRow);
        }

        DispatchRequest flipkartShipment = new DispatchRequest();
        flipkartShipment.setInvoice(invoice);
        flipkartShipment.setShipmentId(getShipmentIdFromAdditionalInfo(shipment.getAdditionalInfo()));
        // TODO : Check with varad and prashanta
        flipkartShipment.setDispatchDate(DateUtils.dateToString(DateUtils.clearTime(DateUtils.getCurrentDate()),"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        flipkartShipment.setLocationId(FlipkartRequestContext.current().getLocationId());
        flipkartShipment.setTrackingId(shipment.getTracking().getAwb());
        flipkartShipment.setTentativeDeliveryDate((DateUtils.dateToString(DateUtils.addDaysToDate(DateUtils.getCurrentTime(),5),"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));
        if (StringUtils.isNotBlank(shipment.getTracking().getAggregator()))
            flipkartShipment.setDeliveryPartner(shipment.getTracking().getAggregator());
        else
            flipkartShipment.setDeliveryPartner(shipment.getTracking().getCourier());
        dispatchSelfShipmentV3Request.addShipmentsItem(flipkartShipment);
        return dispatchSelfShipmentV3Request;
    }

    private String getShipmentIdFromAdditionalInfo(String additionalInfo){
        if ( additionalInfo.contains("shipmentId")) {
            if ( additionalInfo.contains("value") ) {
                return ((JsonObject)JsonUtils.stringToJson(additionalInfo)).get("shipmentId").getAsJsonObject().get("value").getAsString();
            } else {
                return ((JsonObject)JsonUtils.stringToJson(additionalInfo)).get("shipmentId").getAsJsonObject().get("value").getAsString();
            }
        } else {
            return additionalInfo;
        }
    }


}

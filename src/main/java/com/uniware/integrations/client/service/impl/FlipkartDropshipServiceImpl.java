package com.uniware.integrations.client.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.unifier.core.fileparser.DelimitedFileParser;
import com.unifier.core.fileparser.ExcelSheetParser;
import com.unifier.core.fileparser.Row;
import com.unifier.core.utils.DateUtils;
import com.unifier.core.utils.EncryptionUtils;
import com.unifier.core.utils.JsonUtils;
import com.unifier.core.utils.NumberUtils;
import com.unifier.core.utils.PdfUtils;
import com.unifier.core.utils.StringUtils;
import com.uniware.integrations.aws.S3Service;
import com.uniware.integrations.client.annotation.FlipkartClient;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.constants.EnvironmentPropertiesConstant;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.dto.Address;
import com.uniware.integrations.client.dto.ConfirmItemRow;
import com.uniware.integrations.client.dto.DateFilter;
import com.uniware.integrations.client.dto.Dimensions;
import com.uniware.integrations.client.dto.DispatchRequest;
import com.uniware.integrations.client.dto.DispatchShipmentStatus;
import com.uniware.integrations.client.dto.Filter;
import com.uniware.integrations.client.dto.Invoice;
import com.uniware.integrations.client.dto.OrderItem;
import com.uniware.integrations.client.dto.PackRequest;
import com.uniware.integrations.client.dto.Pagination;
import com.uniware.integrations.client.dto.SerialNumber;
import com.uniware.integrations.client.dto.Shipment;
import com.uniware.integrations.client.dto.ShipmentDetails;
import com.uniware.integrations.client.dto.Sort;
import com.uniware.integrations.client.dto.SubShipments;
import com.uniware.integrations.client.dto.TaxItem;
import com.uniware.integrations.client.dto.api.requestDto.DispatchSelfShipmentV3Request;
import com.uniware.integrations.client.dto.api.requestDto.DispatchStandardShipmentV3Request;
import com.uniware.integrations.client.dto.api.requestDto.FetchOnHoldOrderRequest;
import com.uniware.integrations.client.dto.api.requestDto.GetManifestRequest;
import com.uniware.integrations.client.dto.api.requestDto.SearchShipmentRequest;
import com.uniware.integrations.client.dto.api.requestDto.UpdateInventoryV3Request;
import com.uniware.integrations.client.dto.api.responseDto.DispatchShipmentV3Response;
import com.uniware.integrations.client.dto.api.responseDto.InvoiceDetailsV3Response;
import com.uniware.integrations.client.dto.api.responseDto.LocationDetailsResponse;
import com.uniware.integrations.client.dto.api.responseDto.SearchShipmentV3Response;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentDetailsV3WithSubPackages;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentPackV3Response;
import com.uniware.integrations.client.dto.api.responseDto.StockFileDownloadNUploadHistoryResponse;
import com.uniware.integrations.client.dto.api.responseDto.StockFileDownloadRequestStatusResponse;
import com.uniware.integrations.client.dto.api.responseDto.UpdateInventoryV3Response;
import com.uniware.integrations.client.dto.uniware.CatalogPreProcessorRequest;
import com.uniware.integrations.client.dto.uniware.CatalogPreProcessorResponse;
import com.uniware.integrations.client.dto.uniware.CatalogSyncRequest;
import com.uniware.integrations.client.dto.uniware.CatalogSyncResponse;
import com.uniware.integrations.client.dto.uniware.ChannelItemType;
import com.uniware.integrations.client.dto.uniware.CloseShippingManifestRequest;
import com.uniware.integrations.client.dto.uniware.CloseShippingManifestResponse;
import com.uniware.integrations.client.dto.uniware.CreateInvoiceResponse;
import com.uniware.integrations.client.dto.uniware.DispatchShipmentRequest;
import com.uniware.integrations.client.dto.uniware.DispatchShipmentResponse;
import com.uniware.integrations.client.dto.uniware.Error;
import com.uniware.integrations.client.dto.uniware.FetchCurrentChannelManifestRequest;
import com.uniware.integrations.client.dto.uniware.FetchCurrentChannelManifestResponse;
import com.uniware.integrations.client.dto.uniware.FetchOrderRequest;
import com.uniware.integrations.client.dto.api.requestDto.ShipmentPackV3Request;
import com.uniware.integrations.client.dto.api.responseDto.AuthTokenResponse;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentDetailsV3WithAddressResponse;
import com.uniware.integrations.client.dto.uniware.AddressDetail;
import com.uniware.integrations.client.dto.uniware.AddressRef;
import com.uniware.integrations.client.dto.uniware.FetchOrderResponse;
import com.uniware.integrations.client.dto.uniware.FetchPendencyRequest;
import com.uniware.integrations.client.dto.uniware.FetchPendencyResponse;
import com.uniware.integrations.client.dto.uniware.GenerateInvoiceRequest;
import com.uniware.integrations.client.dto.uniware.Pendency;
import com.uniware.integrations.client.dto.uniware.SaleOrder;
import com.uniware.integrations.client.dto.uniware.SaleOrderItem;
import com.uniware.integrations.client.dto.uniware.ShippingPackage;
import com.uniware.integrations.client.dto.uniware.UpdateInventoryRequest;
import com.uniware.integrations.client.dto.uniware.UpdateInventoryResponse;
import com.uniware.integrations.client.service.FlipkartSellerApiService;
import com.uniware.integrations.client.service.FlipkartSellerPanelService;
import com.uniware.integrations.core.dto.api.Response;
import com.uniware.integrations.client.dto.uniware.PreConfigurationResponse;
import com.uniware.integrations.utils.ResponseUtil;
import com.uniware.integrations.web.context.TenantRequestContext;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Created by vipin on 20/05/22.
 */

@Service(value = "flipkartDropshipServiceImpl")
@FlipkartClient(version = "v1",channelSource = { ChannelSource.FLIPKART})
public class FlipkartDropshipServiceImpl extends AbstractSalesFlipkartService {

    public static final String AUTH_TOKEN_EXPIRES_IN = "authTokenExpiresIn";
    @Autowired
    private Environment environment;
    @Autowired
    private FlipkartSellerApiService flipkartSellerApiService;
    @Autowired
    private FlipkartSellerPanelService flipkartSellerPanelService;
    private static final Logger LOGGER = LoggerFactory.getLogger(FlipkartDropshipServiceImpl.class);
    @Autowired
    private S3Service s3Service;
    private static final String BUCKET_NAME = "unicommerce-channel-shippinglabels";
    private static final String SUCCESS = "success";
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS+05:30";
    private static final String DEFAULT_PHONE_NUMBER = "9999999999";
    private static final String UNIWARE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    private static Map<String,List<String>> sourceCodeToFulfilmentModeList= new HashMap();

    static  {
        for ( ChannelSource channelSource: ChannelSource.values() ) {

            if ( StringUtils.equalsIngoreCaseAny(channelSource.getChannelSourceCode(),"FLIPKART","2GUD","FLIPKART_OMNI")){
                List<String> fullfilmentByList = new ArrayList<>();
                fullfilmentByList.add("seller");
                sourceCodeToFulfilmentModeList.put(channelSource.getChannelSourceCode(),fullfilmentByList);
            }
            else if ( StringUtils.equalsIngoreCaseAny(channelSource.getChannelSourceCode(),"FLIPKART_LITE","FLIPKART_SMART")) {
                List<String> fullfilmentByList = new ArrayList<>();
                fullfilmentByList.add("SellerSmart");
                fullfilmentByList.add("Seller Smart");
                fullfilmentByList.add("FA & SellerSmart");
                fullfilmentByList.add("Flipkart and Seller Smart");
                sourceCodeToFulfilmentModeList.put(channelSource.getChannelSourceCode(),fullfilmentByList);
            }
            else if ( StringUtils.equalsIngoreCaseAny(channelSource.getChannelSourceCode(),"FLIPKART_FA") ) {
                List<String> fullfilmentByList = new ArrayList<>();
                fullfilmentByList.add("FA");
                fullfilmentByList.add("Flipkart");
                fullfilmentByList.add("FA & SellerSmart");
                fullfilmentByList.add("Flipkart and Seller Smart");
                fullfilmentByList.add("Flipkart and Seller");
                sourceCodeToFulfilmentModeList.put(channelSource.getChannelSourceCode(),fullfilmentByList);
            }
        }
    }

    @Override public Response preConfiguration(Map<String, String> headers, String payload, String connectorName) {
        PreConfigurationResponse channelPreConfigurationResponse = new PreConfigurationResponse();
        HashMap<String, String> params = new HashMap<>();
        params.put("client_id", environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_APPLICATION_ID));
        params.put("response_type", "code");
        params.put("scope", environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_SCOPE));
        params.put("state", headers.get("state"));
        params.put("redirect_uri", environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_REDIRECT_URL));
        channelPreConfigurationResponse.setUrl(flipkartSellerApiService.getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource()) + "/oauth-service/oauth/authorize");
        channelPreConfigurationResponse.setMethod("GET");
        channelPreConfigurationResponse.setParams(params);
        return ResponseUtil.success(SUCCESS, channelPreConfigurationResponse);
    }

    @Override public Response postConfiguration(Map<String, String> headers, String payload, String connectorName) {

        AuthTokenResponse authTokenResponse = flipkartSellerApiService.getAuthToken(headers,payload);

        if ( authTokenResponse !=null && authTokenResponse.getAccessToken() != null ) {
            String authToken = authTokenResponse.getTokenType() + " " + authTokenResponse.getAccessToken();
            Long authTokenExpireIn = authTokenResponse.getExpiresIn();
            String refreshToken = authTokenResponse.getRefreshToken();

            HashMap<String, String> responseParam = new HashMap<>();
            responseParam.put("authToken", authToken);
            responseParam.put("refreshToken", refreshToken);
            responseParam.put(AUTH_TOKEN_EXPIRES_IN, String.valueOf(authTokenExpireIn));
            return ResponseUtil.success(SUCCESS, responseParam);
        }

        return ResponseUtil.failure("Unable to generate AuthToken");
    }

    @Override public Response connectorVerification(Map<String, String> headers, String payload, String connectorName) {

        Map<String,String> requestParams = JsonUtils.jsonToMap(payload);
        Map<String,String> responseParams = new HashMap<>();

        if ("FLIPKART_SELLER_PANEL".equalsIgnoreCase(connectorName)){
            String username = requestParams.get("username");
            String password = requestParams.get("password");
            boolean loginSuccess = flipkartSellerPanelService.sellerPanelLogin(username, password, false);
            if (!loginSuccess) {
                return ResponseUtil.failure("Unable to login on Flipkart panel.");
            }
            String sellerId = flipkartSellerPanelService.getFeaturesForSeller().getFirst();
            responseParams.put("username", username);
            responseParams.put("password", password);
            responseParams.put("sellerId", sellerId);
            return ResponseUtil.success("Logged in Successfully");
        }
        else if ("FLIPKART_INVENTORY_PANEL".equalsIgnoreCase(connectorName)) {
            String locationId = requestParams.get("locationId");
            String authToken = requestParams.get("authToken");
            String refreshToken = requestParams.get("refreshToken");
            Long authTokenExpiresIn = Long.valueOf(requestParams.get("authTokenExpiresIn"));

            boolean isAuthTokenExpiryNear = isAuthTokenExpiryNear(authTokenExpiresIn);
            if ( isAuthTokenExpiryNear ) {
                AuthTokenResponse authTokenResponse = flipkartSellerApiService.refreshAuthToken(refreshToken);
                if ( authTokenResponse == null)
                    return ResponseUtil.failure("Unable to fetch auth token");
                authToken = authTokenResponse.getAccessToken();
                refreshToken = authTokenResponse.getRefreshToken();
                authTokenExpiresIn = authTokenResponse.getExpiresIn();
            }

            LocationDetailsResponse locationDetailsResponse = flipkartSellerApiService.getAllLocations();
            if ( locationDetailsResponse == null )
                return ResponseUtil.failure("Getting error while fetching location details");
            boolean isValidLocation = locationDetailsResponse.getLocations().stream().anyMatch(location -> locationId.equalsIgnoreCase(location.getLocationId()));

            if ( isValidLocation ) {
                responseParams.put("authToken",authToken);
                responseParams.put("refreshToken",refreshToken);
                responseParams.put("authTokenExpiresIn", String.valueOf(authTokenExpiresIn));
                responseParams.put("locationId",locationId);
                return ResponseUtil.success("Connector verified successfully");
            }
            else {
                return ResponseUtil.failure("Invalid locationId");
            }
        }

        return ResponseUtil.failure("Unable to verify connector");
    }

//    @Override public Response fetchCatalog(Map<String, String> headers, CatalogSyncRequest catalogSyncRequest) {
//
//        CatalogSyncResponse catalogSyncResponse = new CatalogSyncResponse();
//
//        String stockFilePath = catalogSyncRequest.getStockFilePath();
//        String fileFormat = stockFilePath.substring(stockFilePath.lastIndexOf('.')+1);
//        int skipIntialLines = (catalogSyncRequest.getPageNumber()-1) * catalogSyncRequest.getPageSize();
//        Iterator<Row> rows = null;
//        if ( ("CSV").equalsIgnoreCase(fileFormat)){
//            rows = new DelimitedFileParser(stockFilePath).parse(skipIntialLines,true);
//        }
//        else if (("XLS").equalsIgnoreCase(fileFormat)) {
//            rows = new ExcelSheetParser(stockFilePath).parse(skipIntialLines,false);
//        } else {
//            LOGGER.error("file format not supported {}",fileFormat);
//        }
//
//        catalogSyncResponse.setTotalPages((Iterators.size(rows) + skipIntialLines)/catalogSyncRequest.getPageSize());
//
//        if (rows != null) {
//            if ( catalogSyncRequest.getPageNumber() == 1) {
//                rows.next();
//            }
//            catalogSyncResponse = fetchCatalogInternal(rows, catalogSyncRequest.getPageSize());
//
//        } else {
//            return ResponseUtil.failure("Unable to parse listing report");
//        }
//
//        return ResponseUtil.success("Catalog fetched successfully", catalogSyncResponse);
//
//    }

    @Override public Response fetchCatalog(Map<String, String> headers, CatalogSyncRequest catalogSyncRequest) {

        CatalogSyncResponse catalogSyncResponse;

        String stockFilePath = catalogSyncRequest.getStockFilePath();
        if ( StringUtils.isBlank(stockFilePath) ) {
            LOGGER.error("stockFilePath is either blank or null");
            return ResponseUtil.failure("stock filePath is either blank or null");
        }
        
        String fileFormat = stockFilePath.substring(stockFilePath.lastIndexOf('.')+1);

        Iterator<Row> rows = null;
        try {
            if ( ("CSV").equalsIgnoreCase(fileFormat)){
                rows = new DelimitedFileParser(stockFilePath).parse(true);
            }
            else if (("XLS").equalsIgnoreCase(fileFormat)) {
                rows = new ExcelSheetParser(stockFilePath).parse(true);
            } else {
                LOGGER.error("file format not supported {}",fileFormat);
                return ResponseUtil.failure("file format not supported, fileFormat : " +fileFormat);
            }
        } catch ( Exception ex ) {
            LOGGER.error("Exception occur while parsing stock file, exception : {}", ex);
            return ResponseUtil.failure("Exception occur while parsing stock file " + ex.getMessage());
        }
        
        if ( rows.hasNext() ) {
            // Skipping second row as it's containing header description.
            rows.next();
            catalogSyncResponse = fetchCatalogInternal(rows);
            catalogSyncResponse.setTotalPages(catalogSyncRequest.getPageNumber());
        }
        else {
            return ResponseUtil.failure("Invalid Listing report found, path : " + stockFilePath);
        }

        return ResponseUtil.success("Catalog fetched successfully", catalogSyncResponse);
    }


    @Override public Response catalogSyncPreProcessor(Map<String, String> headers, CatalogPreProcessorRequest catalogPreProcessorRequest) {

        CatalogPreProcessorResponse catalogPreProcessorResponse = new CatalogPreProcessorResponse();
        boolean loginSuccess = flipkartSellerPanelService.sellerPanelLogin(FlipkartRequestContext.current().getUserName(), FlipkartRequestContext.current().getPassword(), false);
        if (!loginSuccess) {
            return ResponseUtil.failure("Login Session could not be established");
        }

        String stockfilePath = null;

        // check if already a file is under generation process or not.
        StockFileDownloadRequestStatusResponse stockFileDownloadRequestStatusResponse = flipkartSellerPanelService.getStockFileDownloadRequestStatus();
        if ( "DOWNLOADING".equalsIgnoreCase(stockFileDownloadRequestStatusResponse.getDownloadState())){
            LOGGER.info("File generation is in DOWNLOADING state. Will retry after sometime...");
            fillAsyncDetails(stockFileDownloadRequestStatusResponse, catalogPreProcessorResponse, catalogPreProcessorRequest.isAsyncRun());
            return ResponseUtil.success("File generation is in PROCESSING state. Wait for sometime...", catalogPreProcessorResponse);
        }
        else if ( "COMPLETED".equalsIgnoreCase(stockFileDownloadRequestStatusResponse.getDownloadState())) {
            catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.COMPLETE);
            LOGGER.info("Stock file generation completed.");
        }
        else if ( "FAILED".equalsIgnoreCase(stockFileDownloadRequestStatusResponse.getDownloadState())) {
            catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.FAILED);
            LOGGER.info("Status of Last file requested is FAILED, requesting a new file");
        }
        else {
            catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.FAILED);
            LOGGER.error("ALERT - CASE NOT HANDLED");
        }

        // Check if the stock file exist in StockFileDownloadNUploadHistory if yes then download the it otherwise generate a new one
        StockFileDownloadNUploadHistoryResponse stockFileDownloadNUploadHistoryResponse = flipkartSellerPanelService.getStockFileDownloadNUploadHistory();
        stockfilePath = getStockFile(stockFileDownloadNUploadHistoryResponse);

        if ( stockfilePath == null && !catalogPreProcessorRequest.isAsyncRun()) {
            boolean isRequestStockFileSuccessful = flipkartSellerPanelService.requestStockFile();
            if (isRequestStockFileSuccessful) {
                stockFileDownloadRequestStatusResponse = flipkartSellerPanelService.getStockFileDownloadRequestStatus();
                catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.PROCESSING);
                fillAsyncDetails(stockFileDownloadRequestStatusResponse, catalogPreProcessorResponse, catalogPreProcessorRequest.isAsyncRun());
                return ResponseUtil.success("Requested a new stock file. Wait for sometime...", catalogPreProcessorResponse);
            } else {
                return ResponseUtil.failure("Getting Error while requesting report...");
    }
}

        catalogPreProcessorResponse.setFilePath(stockfilePath);
        return ResponseUtil.success("File downloaded Successfully", catalogPreProcessorResponse);
    }

    @Override public Response fetchPendency(Map<String, String> headers, FetchPendencyRequest fetchPendencyRequest) {

        FetchPendencyResponse fetchPendencyResponse = new FetchPendencyResponse();
        boolean loginSuccess = flipkartSellerPanelService.sellerPanelLogin(FlipkartRequestContext.current().getUserName(), FlipkartRequestContext.current().getPassword(), false);
        if (!loginSuccess) {
            return ResponseUtil.failure("Login Session could not be established");
        }

        Map<String, Pendency> channelProductIdToPendency =new HashMap<>();

        boolean isPendencyFetchedForApprovedShipments = getPendencyOfApprovedShipments(channelProductIdToPendency);
        if ( !isPendencyFetchedForApprovedShipments) {
            return ResponseUtil.failure("Unable to fetch pendency of Approved shipments");
        }

        boolean isPendencyFetchedForOnHoldsShipments = getPendencyOfOnHoldsOrders(channelProductIdToPendency);
        if ( !isPendencyFetchedForOnHoldsShipments) {
            return ResponseUtil.failure("Unable to fetch pendency of OnHold Orders");
        }

        List<Pendency> pendencyList = new ArrayList<>(channelProductIdToPendency.values());
        fetchPendencyResponse.addPendency(pendencyList);

        return ResponseUtil.success("Pendency Fetched Successfully",fetchPendencyResponse);
    }

    @Override  public Response fetchOrders(Map<String, String> headers, FetchOrderRequest fetchOrderRequest) {

        FetchOrderResponse fetchOrderResponse = new FetchOrderResponse();

        if ( fetchOrderRequest.getMetadata() == null || fetchOrderRequest.getMetadata().get("hasMoreNormalShipments") == null || (boolean) fetchOrderRequest.getMetadata().get("hasMoreNormalShipments")) {
            LOGGER.info("Fetching normal shipments, pageNumber: {}", fetchOrderRequest.getPageNumber());
            getNormalShipments(fetchOrderRequest, fetchOrderResponse);
        }
        if ( fetchOrderRequest.getMetadata() == null || fetchOrderRequest.getMetadata().get("hasMoreSelfShipments") == null || (boolean) fetchOrderRequest.getMetadata().get("hasMoreSelfShipments")) {
            LOGGER.info("Fetching self shipments, pageNumber: {}", fetchOrderRequest.getPageNumber());
            getSelfShipments(fetchOrderRequest, fetchOrderResponse);
        }
        if ( fetchOrderRequest.getMetadata() == null || fetchOrderRequest.getMetadata().get("hasMoreExpressShipments") == null || (boolean) fetchOrderRequest.getMetadata().get("hasMoreExpressShipments")) {
            LOGGER.info("Fetching express shipments, pageNumber: {}", fetchOrderRequest.getPageNumber());
            getExpressShipments(fetchOrderRequest, fetchOrderResponse);
        }

        if (fetchOrderResponse.isHasMoreOrders())
            fetchOrderResponse.setTotalPages(fetchOrderRequest.getPageNumber() + 1);

        return ResponseUtil.success("ORDERS LIST FETCHED SUCCESSFULLY!", fetchOrderResponse);
    }

    @Override public Response generateInvoice(Map<String, String> headers, GenerateInvoiceRequest generateInvoiceRequest) {

        CreateInvoiceResponse createInvoiceResponse = new CreateInvoiceResponse();

        if ("UNIWARE".equalsIgnoreCase(generateInvoiceRequest.getShippingPackage().getShippingManager())) {
            return ResponseUtil.success("Shipping Manger is uniware. Generating Uniware Invoicing.");
        }

        ShippingPackage shippingPackage = generateInvoiceRequest.getShippingPackage();
        ShipmentDetailsV3WithSubPackages shipmentDetails = flipkartSellerApiService.getShipmentDetailsWithSubPackages(shippingPackage.getSaleOrder().getCode());
        if (shipmentDetails.getShipments() == null) {
            return ResponseUtil.failure("Unable to fetch shipment details");
        }

        List<OrderItem> orderItems = shipmentDetails.getShipments().get(0).getOrderItems();
        boolean isLabelGenerated = orderItems.stream().noneMatch(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("approved"));
//        boolean isShipmentReadyToShip = orderItems.stream().anyMatch(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("PACKED"));
        boolean packingInProgress = orderItems.stream().anyMatch(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("PACKING_IN_PROGRESS"));
        orderItems.forEach(orderItem -> LOGGER.info("Shipment:{} order item id:{}, listingId:{}, status :{}, quantity:{} ", shippingPackage.getSaleOrder().getCode(), orderItem.getOrderItemId(),orderItem.getListingId(), orderItem.getStatus(), orderItem.getQuantity()));

        if(packingInProgress){
            return ResponseUtil.failure("Packing in process. Kinldy retry after sometime");
        }

        if(!isLabelGenerated){
                boolean isPackConfirmed = packShipment(shippingPackage, shipmentDetails);
            if(!isPackConfirmed){
                return ResponseUtil.failure("Unable to pack shipment. Retry after sometime");
            }
        }

        boolean invoiceDetailsFetched = getInvoiceDetails(shippingPackage, createInvoiceResponse);
        if ( !invoiceDetailsFetched ){
            return ResponseUtil.failure("Unable to fetch Invoice details");
        }

        boolean isCourierInfoFetched = getCourierInfo(shippingPackage,createInvoiceResponse);
        if ( !isCourierInfoFetched ){
            return ResponseUtil.failure("Unable to fetch courier details");
        }

        String filePath = "/tmp/" + TenantRequestContext.current().getHttpSenderIdentifier() + "-" + UUID.randomUUID() + "-invoice_label" + ".pdf";;
        boolean isInvoiceLabelDownloaded = flipkartSellerApiService.downloadInvoiceAndLabel(shippingPackage.getSaleOrder().getCode(),filePath);

        if (isInvoiceLabelDownloaded) {
            String labelSize   = headers.get("labelsize");
            String invoiceSize = headers.get("invoicesize");

            if(StringUtils.isNotBlank(labelSize) && !("A4_FK_Label+Invoice").equals(labelSize)){
                String labelS3URL = formatLabel(labelSize, filePath);
                createInvoiceResponse.getShippingProviderInfo().setShippingLabelLink(labelS3URL);
            }else{
                String labelS3URL = s3Service.uploadFile(new File(filePath),BUCKET_NAME);
                createInvoiceResponse.getShippingProviderInfo().setShippingLabelLink(labelS3URL);
            }

            if(StringUtils.isNotBlank(invoiceSize) && !("Default_UC_Invoice").equals(invoiceSize)){
                String invoiceUrl = formatInvoice(invoiceSize, filePath);
                createInvoiceResponse.setInvoiceUrl(invoiceUrl);
            }
        }
        else {
            return ResponseUtil.failure("Unable to download InvoiceLabel document ");
        }

        return ResponseUtil.success("Invoice Generated",createInvoiceResponse);
    }

    private boolean getCourierInfo(ShippingPackage shippingPackage,CreateInvoiceResponse createInvoiceResponse) {
        ShipmentDetailsV3WithAddressResponse shipmentDetailsWithAddress = flipkartSellerApiService.getShipmentDetailsWithAddress(shippingPackage.getSaleOrder().getCode());
        if (shipmentDetailsWithAddress.getShipments() == null) {
            LOGGER.info("Unable to fetch shipment details");
            return false;
        }

        Optional<ShipmentDetails.SubShipment> subShipment = shipmentDetailsWithAddress.getShipments().get(0).getSubShipments()
                .stream().filter(ss -> ("SS-1").equalsIgnoreCase(ss.getSubShipmentId())).findFirst();
        if ( subShipment.isPresent() ) {
            ShipmentDetails.SubShipment subShipmentDetails = subShipment.get();
            String trackingNumber = subShipmentDetails.getCourierDetails().getPickupDetails().getTrackingId();
            String courierName = subShipmentDetails.getCourierDetails().getPickupDetails().getVendorName();
            CreateInvoiceResponse.ShippingProviderInfo shippingProviderInfo = new CreateInvoiceResponse.ShippingProviderInfo();
            shippingProviderInfo.setShippingProvider(courierName);
            shippingProviderInfo.setTrackingNumber(trackingNumber);
            createInvoiceResponse.setShippingProviderInfo(shippingProviderInfo);
        } else {
            LOGGER.error("Unable to found subShipment details");
            return false;
        }
        return true;
    }

    private boolean getInvoiceDetails(ShippingPackage shippingPackage,CreateInvoiceResponse createInvoiceResponse) {

        Map<String,String> channelSaleOrderItemCodeToChannelProductId = new HashMap<>();
        shippingPackage.getSaleOrderItems().forEach(entry -> channelSaleOrderItemCodeToChannelProductId.put(entry.getChannelSaleOrderItemCode(),entry.getChannelProductId()));

        InvoiceDetailsV3Response invoiceDetailsV3Response = flipkartSellerApiService.getInvoicesInfo(shippingPackage.getSaleOrder().getCode());

        if (invoiceDetailsV3Response != null) {
            if (StringUtils.isNotBlank(shippingPackage.getInvoiceCode()))
                createInvoiceResponse.setInvoiceCode(shippingPackage.getInvoiceCode());
            else
                createInvoiceResponse.setInvoiceCode(invoiceDetailsV3Response.getInvoices().get(0).getInvoiceNumber());

            createInvoiceResponse.setDisplayCode(invoiceDetailsV3Response.getInvoices().get(0).getInvoiceNumber());
            createInvoiceResponse.setChannelCreatedTime(invoiceDetailsV3Response.getInvoices().get(0).getInvoiceDate());
            CreateInvoiceResponse.TaxInformation taxInformation = new CreateInvoiceResponse.TaxInformation();
            for ( Invoice.OrderItem orderItem : invoiceDetailsV3Response.getInvoices().get(0).getOrderItems()) {
                CreateInvoiceResponse.ProductTax productTax = new CreateInvoiceResponse.ProductTax();
                productTax.setChannelProductId(channelSaleOrderItemCodeToChannelProductId.get(orderItem.getOrderItemId().replaceAll("\n","")));
                productTax.setCentralGst(orderItem.getTaxDetails().getCgstRate() != null ? orderItem.getTaxDetails().getCgstRate() : BigDecimal.ZERO);
                productTax.setStateGst(orderItem.getTaxDetails().getSgstRate() != null ? orderItem.getTaxDetails().getSgstRate() : BigDecimal.ZERO);
                productTax.setIntegratedGst(orderItem.getTaxDetails().getIgstRate() != null ? orderItem.getTaxDetails().getIgstRate() : BigDecimal.ZERO);
                productTax.setCompensationCess(orderItem.getTaxDetails().getCessRate() != null ? orderItem.getTaxDetails().getUtgstRate() : BigDecimal.ZERO);
                productTax.setUnionTerritoryGst(orderItem.getTaxDetails().getUtgstRate() != null ? orderItem.getTaxDetails().getUtgstRate() : BigDecimal.ZERO);
                taxInformation.addProductTax(productTax);
            }
            createInvoiceResponse.setTaxInformation(taxInformation);
            return true;
        }
        return false;
    }

    private String formatLabel(String labelSize, String filePath) {
        String labelOutFilePath = "/tmp/" +
                TenantRequestContext.current().getHttpSenderIdentifier() + "-" + UUID.randomUUID() + ".pdf";
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
        String labelS3URL = s3Service.uploadFile(new File(labelOutFilePath),BUCKET_NAME);
        return labelS3URL;
    }

    private String formatInvoice(String invoiceSize, String filePath) {

        String invoiceOutFilePath = "/tmp/" +
                TenantRequestContext.current().getHttpSenderIdentifier()  + "-" + UUID.randomUUID()+ ".pdf";
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

        String invoiceS3URL = s3Service.uploadFile(new File(invoiceOutFilePath), BUCKET_NAME);
        return invoiceS3URL;
    }

    @Override
    public Response dispatchShipments(Map<String, String> headers, DispatchShipmentRequest dispatchShipmentRequest) {

        DispatchShipmentResponse dispatchShipmentResponse = new DispatchShipmentResponse();

        DispatchShipmentV3Response dispatchShipmentV3Response = null;
        if ("CHANNEL".equalsIgnoreCase(dispatchShipmentRequest.getShippingManager())){
            DispatchStandardShipmentV3Request dispatchStandardShipmentV3Request = new DispatchStandardShipmentV3Request();
            dispatchStandardShipmentV3Request.addShipmentId(dispatchShipmentRequest.getSaleOrderCode()).setLocationId(FlipkartRequestContext.current().getLocationId());
            dispatchShipmentV3Response = flipkartSellerApiService.markStandardFulfilmentShipmentsRTD(dispatchStandardShipmentV3Request);
        }
        else if ("SELF".equalsIgnoreCase(dispatchShipmentRequest.getShippingManager())) {
            DispatchSelfShipmentV3Request dispatchSelfShipmentV3Request = new DispatchSelfShipmentV3Request();
            DispatchRequest shipment = new DispatchRequest();
            HashSet<String> combinationIdentifierSet = new HashSet<>();
            HashSet<String> channelSaleOrderItemCodeSet = new HashSet<>();
            for (SaleOrderItem saleOrderItem : dispatchShipmentRequest.getSaleOrderItems()) {
                ConfirmItemRow confirmItemRow = new ConfirmItemRow();
                // Inside if block handling bundle sku's
                if ( saleOrderItem.getCombinationIdentifier() != null
                        && combinationIdentifierSet.add(saleOrderItem.getCombinationIdentifier())
                        && channelSaleOrderItemCodeSet.add(saleOrderItem.getChannelSaleOrderItemCode()) ) {
                    confirmItemRow.orderItemId(saleOrderItem.getChannelSaleOrderItemCode());
                    confirmItemRow.quantity(1);
                    shipment.addOrderItemsItem(confirmItemRow);
                }
                else {
                    if ( channelSaleOrderItemCodeSet.add(saleOrderItem.getChannelSaleOrderItemCode())){
                        confirmItemRow.orderItemId(saleOrderItem.getChannelSaleOrderItemCode());
                        confirmItemRow.quantity(1);
                        shipment.addOrderItemsItem(confirmItemRow);
                    } else {
                        shipment.incrementOrderItemQuantityByOne(saleOrderItem.getChannelSaleOrderItemCode());
                    }
                }
            }

            DispatchRequest.Invoice invoice = new DispatchRequest.Invoice();
            invoice.setInvoiceNumber(dispatchShipmentRequest.getInvoiceCode());
            invoice.setInvoiceDate(DateUtils.dateToString(dispatchShipmentRequest.getInvoiceDate(),"yyyy-MM-dd"));

            shipment.setInvoice(invoice);
            shipment.setShipmentId(dispatchShipmentRequest.getSaleOrderCode());
            shipment.setDispatchDate(dispatchShipmentRequest.getDispatchDate());
            shipment.setLocationId(FlipkartRequestContext.current().getLocationId());
            shipment.setTrackingId(dispatchShipmentRequest.getTrackingNumber());
            shipment.setTentativeDeliveryDate((DateUtils.addDaysToDate(DateUtils.getCurrentDate(),5)));
            if (dispatchShipmentRequest.isShippingProviderIsAggregator())
                shipment.setDeliveryPartner(dispatchShipmentRequest.getAggregatorAllocatedCourier());
            else
                shipment.setDeliveryPartner(dispatchShipmentRequest.getShippingProvider());
            dispatchSelfShipmentV3Request.addShipmentsItem(shipment);
            dispatchShipmentV3Response = flipkartSellerApiService.markSelfShipDispatch(dispatchSelfShipmentV3Request);
        }

        if ( dispatchShipmentV3Response != null ) {
            DispatchShipmentStatus shipment = dispatchShipmentV3Response.getShipments().get(0);
            dispatchShipmentResponse.setSaleOrderCode(shipment.getShipmentId());
            if ( ("success").equalsIgnoreCase(shipment.getStatus())) {
                dispatchShipmentResponse.setStatus(DispatchShipmentResponse.Status.SUCCESS);
            } else {
                dispatchShipmentResponse.setStatus(DispatchShipmentResponse.Status.FAILED);
                dispatchShipmentResponse.setError(new Error(shipment.getErrorCode(),shipment.getErrorMessage()));
            }
        }
        else {
            return ResponseUtil.failure("Unable to dispatch shipment on Flipkart");
        }

        return ResponseUtil.success("Shipment Dispatched Successfully.",dispatchShipmentResponse);
    }
    @Override public Response closeShippingManifest(Map<String, String> headers, CloseShippingManifestRequest closeShippingManifestRequest) {

        CloseShippingManifestResponse closeShippingManifestResponse = new CloseShippingManifestResponse();
        Map<String,String> saleOrderCodeToShippingPackageCode = new HashMap<>();

        DispatchShipmentV3Response dispatchShipmentV3Response = null;

        if ("CHANNEL".equalsIgnoreCase(closeShippingManifestRequest.getShippingManager())){
            DispatchStandardShipmentV3Request dispatchStandardShipmentRequestV3 = prepareDispatchStandardShipmentRequest(closeShippingManifestRequest, saleOrderCodeToShippingPackageCode);
            dispatchShipmentV3Response = flipkartSellerApiService.markStandardFulfilmentShipmentsRTD(dispatchStandardShipmentRequestV3);
        }
        else if ("SELF".equalsIgnoreCase(closeShippingManifestRequest.getShippingManager())) {
            DispatchSelfShipmentV3Request dispatchSelfShipmentV3Request = prepareDispatchSelfShipmentRequest(closeShippingManifestRequest, saleOrderCodeToShippingPackageCode);
            dispatchShipmentV3Response = flipkartSellerApiService.markSelfShipDispatch(dispatchSelfShipmentV3Request);
        }

        if ( dispatchShipmentV3Response != null ) {
            for (DispatchShipmentStatus shipments : dispatchShipmentV3Response.getShipments()) {
                if ( "FAILURE".equalsIgnoreCase(shipments.getStatus()) ) {
                    CloseShippingManifestResponse.FailedShipment failedShipment = new CloseShippingManifestResponse.FailedShipment();
                    failedShipment.setShipmentCode(saleOrderCodeToShippingPackageCode.get(shipments.getShipmentId()));
                    failedShipment.setFailureReason(shipments.getErrorMessage());
                    failedShipment.setSaleOrderCode(shipments.getShipmentId());
                    // Todo check whether we can identify if the shipment is cancelled or not ??
                    if ( "CANCELLED".equalsIgnoreCase(shipments.getErrorMessage()))
                        failedShipment.setCancelled(true);

                    closeShippingManifestResponse.addFailedShipment(failedShipment);
                }
            }
            closeShippingManifestResponse.setShippingManifestLink("");
        } else {
            return ResponseUtil.failure("Unable to close Manifest");
        }

        return ResponseUtil.success("Manifest Closed Successfully.",closeShippingManifestResponse);
    }

    @Override public Response fetchCurrentChannelManifest(Map<String,String> headers, FetchCurrentChannelManifestRequest fetchCurrentChannelManifestRequest) {

        GetManifestRequest getManifestRequest = new GetManifestRequest.Builder()
                .setParams(new GetManifestRequest.Params.Builder()
                        .setVendorGroupCode(getVendorGroupCode(fetchCurrentChannelManifestRequest.getShippingProviderCode()))
                        .setIsMps(false)
                        .setLocationId(FlipkartRequestContext.current().getLocationId())
                        .build())
                .build();

        String manifestFilePath = "/tmp/"+ com.uniware.integrations.utils.StringUtils.join('_', TenantRequestContext.current().getTenantCode(), UUID.randomUUID().toString(), fetchCurrentChannelManifestRequest.getShippingManifestCode()) + ".pdf";

        boolean isManifestDownloaded = flipkartSellerApiService.getCurrentChannelManifest(getManifestRequest, manifestFilePath);

        if (isManifestDownloaded) {
            String manifestLink = s3Service.uploadFile(new File(manifestFilePath),BUCKET_NAME);
            FetchCurrentChannelManifestResponse fetchCurrentChannelManifestResponse = new FetchCurrentChannelManifestResponse();
            fetchCurrentChannelManifestResponse.setManifestLink(manifestLink);
            return ResponseUtil.success("Manifest downloaded successfully", fetchCurrentChannelManifestResponse);
        }

        return ResponseUtil.failure("Unable to download Manifest");
    }

    @Override public Response updateInventory(Map<String, String> headers, UpdateInventoryRequest updateInventoryRequest) {

        UpdateInventoryV3Request updateInventoryV3Request = prepareFlipkartInventoryUpdateRequest(updateInventoryRequest);
        UpdateInventoryV3Response updateInventoryV3Response = flipkartSellerApiService.updateInventory(updateInventoryV3Request);
        Map<String,String> channelSkuCodeToChannelProductId = updateInventoryRequest.getListings().stream().collect(Collectors.toMap(UpdateInventoryRequest.Listing::getChannelSkuCode, UpdateInventoryRequest.Listing::getChannelProductId));
        UpdateInventoryResponse updateInventoryResponse = new UpdateInventoryResponse();
        for (Map.Entry<String, UpdateInventoryV3Response.InventoryUpdateStatus> entry: updateInventoryV3Response.getSkus().entrySet()) {
            String channelSkuCode = entry.getKey();
            UpdateInventoryV3Response.InventoryUpdateStatus inventoryUpdateStatus = entry.getValue();
            UpdateInventoryResponse.Listing listing = new UpdateInventoryResponse.Listing();
            if (channelSkuCodeToChannelProductId.containsKey(channelSkuCode)) {
                if ( ("success").equalsIgnoreCase(inventoryUpdateStatus.getStatus())) {
                    listing.setChannelProductId(channelSkuCodeToChannelProductId.get(channelSkuCode));
                    listing.setChannelSkuCode(channelSkuCode);
                    listing.setStatus(UpdateInventoryResponse.Status.SUCCESS);
                }
                else if ( ("failure").equalsIgnoreCase(inventoryUpdateStatus.getStatus()) || ("warning").equalsIgnoreCase(inventoryUpdateStatus.getStatus()) ) {
                    listing.setChannelProductId(channelSkuCodeToChannelProductId.get(channelSkuCode));
                    listing.setChannelSkuCode(channelSkuCode);
                    listing.setStatus(UpdateInventoryResponse.Status.FAILED);
                    if (inventoryUpdateStatus.getErrors() != null)
                    inventoryUpdateStatus.getErrors().stream().forEach(error -> listing.addError(new Error(error.getCode(),error.getDescription())));
                    if (inventoryUpdateStatus.getAttributeErrors() != null)
                        inventoryUpdateStatus.getAttributeErrors().stream().forEach(error -> listing.addError(new Error(error.getCode(),error.getDescription())));
                }
            }
            updateInventoryResponse.addListing(listing);
            channelSkuCodeToChannelProductId.remove(channelSkuCode);
        }
        channelSkuCodeToChannelProductId.entrySet().stream().forEach(entry -> {
            UpdateInventoryResponse.Listing listing = new UpdateInventoryResponse.Listing();
            listing.addError(new Error("FAILED","Not getting update confirmation in api response from Flipkart"));
            listing.setChannelSkuCode(entry.getKey());
            listing.setChannelProductId(entry.getValue());
            updateInventoryResponse.addListing(listing);
        });

        return ResponseUtil.success("SUCCESS",updateInventoryResponse);

    }

    private boolean isAuthTokenExpiryNear(Long authTokenExpiresIn){

        Date currentTime = DateUtils.getCurrentTime();
        Date authTokenExpiresInDate = new Date(authTokenExpiresIn) ;
        int numberOfDaysLeftBeforeExpiry = DateUtils.diff(currentTime, authTokenExpiresInDate, DateUtils.Resolution.DAY);
        LOGGER.info("Number Of days are left in auth token expiry.",numberOfDaysLeftBeforeExpiry);
        if ( numberOfDaysLeftBeforeExpiry < 2 ){
            LOGGER.info("AuthToken Expiry is near refetch new authToken");
            return true;
        }
        else{
            return false;
        }
    }

    private SearchShipmentRequest prepareSearchShimentRequest(Filter.ShipmentTypesEnum shipmentType, int orderWindow, Date orderDateOfLastOrderOfLastPage) {

        DateFilter DispatchAfterDateFilter = new DateFilter();
        DispatchAfterDateFilter.from(DateUtils.dateToString((DateUtils.addToDate(DateUtils.getCurrentDate(), Calendar.DATE, -5)),DATE_PATTERN));
        DispatchAfterDateFilter.to(DateUtils.dateToString(DateUtils.getCurrentTime(),DATE_PATTERN));

        DateFilter orderDateFilter = new DateFilter();
        orderDateFilter.from(DateUtils.dateToString(DateUtils.addToDate(DateUtils.getCurrentDate(), Calendar.DATE, -orderWindow),DATE_PATTERN));
        if (orderDateOfLastOrderOfLastPage != null )
            orderDateFilter.to((DateUtils.dateToString(orderDateOfLastOrderOfLastPage,DATE_PATTERN)));
        else
            orderDateFilter.to((DateUtils.dateToString(DateUtils.getCurrentTime(),DATE_PATTERN)));

        Filter filter = new Filter();
        filter.type(Filter.TypeEnum.PREDISPATCH);
        filter.dispatchAfterDate(DispatchAfterDateFilter);
        filter.orderDate(orderDateFilter);
        filter.locationId(FlipkartRequestContext.current().getLocationId());
        filter.addStatesItem(Filter.StatesEnum.APPROVED);
        filter.addShipmentTypesItem(shipmentType);

        Sort sort = new Sort();
        sort.setField(Sort.FieldEnum.ORDERDATE);
        sort.setOrder(Sort.OrderEnum.DESC);

        Pagination pagination = new Pagination();
        pagination.pageSize(20);

        SearchShipmentRequest searchShipmentRequest = new SearchShipmentRequest();
        searchShipmentRequest.setFilter(filter);
        searchShipmentRequest.setPagination(pagination);
        searchShipmentRequest.setSort(sort);
        return searchShipmentRequest;
    }

    private void getNormalShipments(FetchOrderRequest fetchOrderRequest, FetchOrderResponse fetchOrderResponse) {

        SearchShipmentV3Response searchShipmentV3Response = new SearchShipmentV3Response();

        int pageNumber = fetchOrderRequest.getPageNumber();
        Date orderDateOfLastOrderOfLastPage = null;

        if ( (pageNumber % 249) == 1 ) {

            if (pageNumber != 1) {
                orderDateOfLastOrderOfLastPage = new Date((Long) fetchOrderRequest.getMetadata().get("orderDateOfLastOrderOfLastPageOfNormalShipments"));
                if ( orderDateOfLastOrderOfLastPage == null ) {
                    LOGGER.info("orderDateOfLastOrderOfLastPage can't be null for page number more than 1, pageNumber : {}",pageNumber);
                    return;
                }
            }

            SearchShipmentRequest searchShipmentRequest = prepareSearchShimentRequest(Filter.ShipmentTypesEnum.NORMAL,fetchOrderRequest.getOrderWindow(), orderDateOfLastOrderOfLastPage);
            searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentPost(searchShipmentRequest);
        }
        else {
            String nextPageUrl = (String) fetchOrderRequest.getMetadata().get("nextPageUrlForNormalShipments");
            searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentGet(nextPageUrl);
        }

        if (searchShipmentV3Response != null ){
            if ( searchShipmentV3Response.getShipments().size() > 0 ) {
                List<SaleOrder> saleOrderList = createSaleOrder(searchShipmentV3Response, false);
                fetchOrderResponse = fetchOrderResponse.addOrders(saleOrderList);

                if ( (pageNumber % 249) == 0){
                    SaleOrder saleOrder = Collections.min(saleOrderList, Comparator.comparing(c -> c.getDisplayOrderDateTime()));
                    fetchOrderResponse.getMetadata().put("orderDateOfLastOrderOfLastPageOfNormalShipments",DateUtils.addToDate(saleOrder.getDisplayOrderDateTime(),Calendar.MINUTE,-10));
                }
            }

            if ( searchShipmentV3Response.isHasMore()){
                fetchOrderResponse.getMetadata().put("nextPageUrlForNormalShipments", searchShipmentV3Response.getNextPageUrl());
                fetchOrderResponse.getMetadata().put("hasMoreNormalShipments",true);
                fetchOrderResponse.setHasMoreOrders(true);
            } else{
                fetchOrderResponse.getMetadata().put("hasMoreNormalShipments",false);
            }
        }
    }

    private void getSelfShipments(FetchOrderRequest fetchOrderRequest, FetchOrderResponse fetchOrderResponse) {

        SearchShipmentV3Response searchShipmentV3Response;

        int pageNumber = fetchOrderRequest.getPageNumber();
        Date orderDateOfLastOrderOfLastPage = null;

        if ( (pageNumber % 249) == 1 ) {

            if (pageNumber != 1) {
                orderDateOfLastOrderOfLastPage = new Date((Long) fetchOrderRequest.getMetadata().get("orderDateOfLastOrderOfLastPageOfSelfShipments"));
                if ( orderDateOfLastOrderOfLastPage == null ) {
                    LOGGER.info("orderDateOfLastOrderOfLastPage can't be null for page number more than 1, pageNumber : {}",pageNumber);
                    return;
                }
            }

            SearchShipmentRequest searchShipmentRequest = prepareSearchShimentRequest(Filter.ShipmentTypesEnum.SELF,fetchOrderRequest.getOrderWindow(), orderDateOfLastOrderOfLastPage);
            searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentPost(searchShipmentRequest);
        }
        else {
            String nextPageUrl = (String) fetchOrderRequest.getMetadata().get("nextPageUrlForSelfShipments");
            searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentGet(nextPageUrl);
        }

        if (searchShipmentV3Response != null ){
            if ( searchShipmentV3Response.getShipments().size() > 0 ) {
                List<SaleOrder> saleOrderList = createSaleOrder(searchShipmentV3Response, false);
                fetchOrderResponse = fetchOrderResponse.addOrders(saleOrderList);

                if ( (pageNumber % 249) == 0 ){
                    SaleOrder saleOrder = Collections.min(saleOrderList, Comparator.comparing(c -> c.getDisplayOrderDateTime()));
                    fetchOrderResponse.getMetadata().put("orderDateOfLastOrderOfLastPageOfSelfShipments",DateUtils.addToDate(saleOrder.getDisplayOrderDateTime(),Calendar.MINUTE,-10));
                }
            }

            if ( searchShipmentV3Response.isHasMore()){
                fetchOrderResponse.getMetadata().put("nextPageUrlForSelfShipments", searchShipmentV3Response.getNextPageUrl());
                fetchOrderResponse.getMetadata().put("hasMoreSelfShipments",true);
                fetchOrderResponse.setHasMoreOrders(true);
            } else{
                fetchOrderResponse.getMetadata().put("hasMoreSelfShipments",false);
            }
        }
    }

    private void getExpressShipments(FetchOrderRequest fetchOrderRequest, FetchOrderResponse fetchOrderResponse) {

        SearchShipmentV3Response searchShipmentV3Response;

        int pageNumber = fetchOrderRequest.getPageNumber();
        Date orderDateOfLastOrderOfLastPage = null;

        if ( pageNumber % 249 == 1 ) {

            if (pageNumber != 1) {
                orderDateOfLastOrderOfLastPage = new Date((Long) fetchOrderRequest.getMetadata().get("orderDateOfLastOrderOfLastPageOfExpressShipments"));
                if ( orderDateOfLastOrderOfLastPage == null ) {
                    LOGGER.info("orderDateOfLastOrderOfLastPage can't be null for page number more than 1, pageNumber : {}",pageNumber);
                    return;
                }
            }

            SearchShipmentRequest searchShipmentRequest = prepareSearchShimentRequest(Filter.ShipmentTypesEnum.EXPRESS,fetchOrderRequest.getOrderWindow(), orderDateOfLastOrderOfLastPage);
            searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentPost(searchShipmentRequest);
        }
        else {
            String nextPageUrl = (String) fetchOrderRequest.getMetadata().get("nextPageUrlForExpressShipments");
            searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentGet(nextPageUrl);
        }

        if (searchShipmentV3Response != null ){
            if ( searchShipmentV3Response.getShipments().size() > 0 ) {
                List<SaleOrder> saleOrderList = createSaleOrder(searchShipmentV3Response, true);
                fetchOrderResponse = fetchOrderResponse.addOrders(saleOrderList);

                if ( (pageNumber % 249) == 0 ){
                    SaleOrder saleOrder = Collections.min(saleOrderList, Comparator.comparing(c -> c.getDisplayOrderDateTime()));
                    fetchOrderResponse.getMetadata().put("orderDateOfLastOrderOfLastPageOfExpressShipments",DateUtils.addToDate(saleOrder.getDisplayOrderDateTime(),Calendar.MINUTE,-10));
                }
            }

            if ( searchShipmentV3Response.isHasMore()){
                fetchOrderResponse.getMetadata().put("nextPageUrlForExpressShipments", searchShipmentV3Response.getNextPageUrl());
                fetchOrderResponse.getMetadata().put("hasMoreExpressShipments",true);
                fetchOrderResponse.setHasMoreOrders(true);
            } else{
                fetchOrderResponse.getMetadata().put("hasMoreExpressShipments",false);
            }
        }
    }

    private boolean packShipment(ShippingPackage shippingPackage, ShipmentDetailsV3WithSubPackages shipmentDetails) {

        ShipmentPackV3Request shipmentPackV3Request = preparePackShipmentRequest(shippingPackage, shipmentDetails);
        ShipmentPackV3Response shipmentPackV3Response = flipkartSellerApiService.packShipment(shipmentPackV3Request);
        if("SUCCESS".equalsIgnoreCase(shipmentPackV3Response.getShipments().get(0).getStatus())){
            return confirmIfShipmentPacked(shippingPackage.getSaleOrder().getCode());
        }
        return false;
    }

    private boolean confirmIfShipmentPacked(String shipmentId) {
        int retryCount = 10;
        boolean packingInProgess = true;
        while (--retryCount > 0 && packingInProgess) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ShipmentDetailsV3WithSubPackages shipmentDetails = flipkartSellerApiService.getShipmentDetailsWithSubPackages(shipmentId);
            if (shipmentDetails.getShipments() == null) {
                return false;
            }

            List<OrderItem> orderItems = shipmentDetails.getShipments().get(0).getOrderItems();
            boolean shipmentPacked = orderItems.stream().anyMatch(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("PACKED"));
            packingInProgess = orderItems.stream().anyMatch(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("PACKING_IN_PROGRESS"));
            if (shipmentPacked)
                return true;
        }
        return false;
    }

    private List<String> getSerialList(ShippingPackage.SaleOrderItem saleOrderItem) {
        List<String> serialList = new ArrayList<>();
        if (StringUtils.isNotBlank(saleOrderItem.getItemDetails())) {
            JsonObject jsonObject = new Gson().fromJson(saleOrderItem.getItemDetails(), JsonObject.class);
            String serialNumbers = jsonObject.get("imei") != null ? jsonObject.get("imei").getAsString() : (jsonObject.get("serialNumber") != null ? jsonObject.get("serialNumber").getAsString() : null);
            if (serialNumbers != null) {
                serialList = StringUtils.split(serialNumbers);
            }
        }
        return serialList;
    }

    private ShipmentPackV3Request preparePackShipmentRequest(ShippingPackage shippingPackage,
            ShipmentDetailsV3WithSubPackages shipmentDetails) {

        PackRequest packRequest = new PackRequest();
        Dimensions dimensions = null;

        try {
            dimensions = shipmentDetails.getShipments().get(0).getSubShipments().get(0).getPackages().get(0).getDimensions();
        } catch (NullPointerException ex) {
            LOGGER.error("Getting NPE while fetching dimensions");
        }

        Map<String,Integer> channelSaleOrderItemCodeToQty = new HashMap<>();
        HashSet<String> combinationIdentifierSet = new HashSet<>();
        for (ShippingPackage.SaleOrderItem saleOrderItem: shippingPackage.getSaleOrderItems() ) {
            if (channelSaleOrderItemCodeToQty.computeIfPresent(saleOrderItem.getChannelSaleOrderItemCode(), (key, val) -> val + 1) == null && combinationIdentifierSet.add(saleOrderItem.getCombinationIdentifier())) {
                channelSaleOrderItemCodeToQty.put(saleOrderItem.getChannelSaleOrderItemCode(),1);
            }
        }

        shippingPackage.getSaleOrderItems().forEach(saleOrderItem -> {
            String orderItemId = saleOrderItem.getChannelSaleOrderItemCode();
            String combinationIdentifier = StringUtils.isNotBlank(saleOrderItem.getBundleSkuCode()) ? saleOrderItem.getBundleSkuCode() : orderItemId;
            if (!combinationIdentifierSet.add(combinationIdentifier)) {
                return;
            }

            TaxItem taxItem = new TaxItem();
            taxItem.orderItemId(orderItemId);
            taxItem.setQuantity(channelSaleOrderItemCodeToQty.get(orderItemId));
            taxItem.setTaxRate(BigDecimal.ZERO);
            packRequest.addTaxItemsItem(taxItem);

            List<String> serialNumberList=getSerialList(saleOrderItem);
            if(!serialNumberList.isEmpty()){
                SerialNumber serialNumber = new SerialNumber();
                serialNumber.addSerialNumbersItem(serialNumberList);
                serialNumber.setOrderItemId(orderItemId);
                packRequest.addSerialNumbersItem(serialNumber);
            }

        });

        SubShipments subShipment = new SubShipments()
                .subShipmentId("SS-1")
                .dimensions(dimensions != null ? dimensions : getPackDimension(shippingPackage));
        Invoice invoice = new Invoice()
                .orderId(shippingPackage.getSaleOrder()
                .getDisplayOrderCode())
                .invoiceNumber(shippingPackage.getInvoiceCode())
                .invoiceDate(DateUtils.getCurrentDate());


        packRequest.shipmentId(shippingPackage.getSaleOrder().getCode());
        packRequest.setLocationId(FlipkartRequestContext.current().getLocationId());
        packRequest.addSubShipmentsItem(subShipment);
        packRequest.addInvoicesItem(invoice);
        ShipmentPackV3Request shipmentPackV3Request = new ShipmentPackV3Request();
        shipmentPackV3Request.addShipmentsItem(packRequest);
        return shipmentPackV3Request;
    }

    private Dimensions getPackDimension(ShippingPackage shippingPackage) {
        Dimensions dimensions = new Dimensions();
        if ( shippingPackage.getLength().compareTo(BigDecimal.ONE) > 1) {
            dimensions.setLength(shippingPackage.getLength().divide(BigDecimal.TEN));
            dimensions.setBreadth(shippingPackage.getBreadth().divide(BigDecimal.TEN));
            dimensions.setHeight(shippingPackage.getHeight().divide(BigDecimal.TEN));
            dimensions.setWeight(shippingPackage.getWeight().divide(BigDecimal.valueOf(1000)));
        }
        else {
            dimensions.defaultDimensions();
        }
        return dimensions;
    }

    /*
        Description - Returns latest file of same day with following properties operationType = GENERATED, errorRowExist = false, fileFormat - CSV or XLS, uploadedOn is today date.
        Note - Order of files in StockFileResponseList is from PRESENT TO PAST
     */
    private String getStockFile(StockFileDownloadNUploadHistoryResponse stockFileDownloadNUploadHistoryResponse) {

        Optional<StockFileDownloadNUploadHistoryResponse.StockFileResponseList> stockFileResponseList = stockFileDownloadNUploadHistoryResponse.getStockFileResponseList().stream()
                .filter( e -> !e.getErrorRowsExists()
                        &&  ("GENERATED").equalsIgnoreCase(e.getFileOperationType())
                        && StringUtils.equalsIngoreCaseAny(e.getFileFormat(),"CSV","XLS")
                        && DateUtils.isToday(DateUtils.getDateFromEpoch(e.getUploadedOn())))
                .findFirst();
        if ( stockFileResponseList.isPresent()){
            StockFileDownloadNUploadHistoryResponse.StockFileResponseList fileDetails = stockFileResponseList.get();
            String fileLink = fileDetails.getFileLink();
            String fileFormat = fileDetails.getFileFormat();
            String fileName = fileDetails.getFileName();
            LOGGER.info("Stock file found with following details. FileName - {}, fileFormat - {}, FileLink - {}",fileName, fileFormat, fileLink);

            String stockFilePath = flipkartSellerPanelService.downloadStockFile(fileName,fileLink,fileFormat);
            if ( stockFilePath == null ){
                LOGGER.error("Unable to download stock file.");
            }
            else {
                return stockFilePath;
            }
        }
        else {
            LOGGER.info("Unable to found valid stock file. Either of following properties not match - operationType = GENERATED, errorRowExist = false, fileFormat - CSV or XLS ");
        }
        return null;
    }

    private CatalogSyncResponse fetchCatalogInternal(Iterator<Row> rows) {

        CatalogSyncResponse catalogSyncResponse = new CatalogSyncResponse();
        while ( rows.hasNext() ) {
            Row row = rows.next();
            boolean isValid = validateRow(row);
            if (isValid) {
                ChannelItemType channelItemType = new ChannelItemType.Builder()
                        .setChannelCode(TenantRequestContext.current().getChannelCode())
                        .setChannelProductId(getChannelProductIdBySourceCode(row,TenantRequestContext.current().getSourceCode()))
                        .setSellerSkuCode(row.getColumnValue("Seller SKU Id"))
                        .setProductName(row.getColumnValue("Product Title"))
                        .setLive(true)
                        .setSellingPrice(new BigDecimal(row.getColumnValue("Your Selling Price")))
                        .setMrp(new BigDecimal(row.getColumnValue("MRP")))
                        .setCurrencyCode("INR")
                        .build();

                if ("FLIPKART_DROPSHIP".equalsIgnoreCase(TenantRequestContext.current().getSourceCode())){
                    channelItemType.addAttribute(new ChannelItemType.Attribute.Builder()
                            .setName("FSN")
                            .setValue(row.getColumnValue("Flipkart Serial Number"))
                            .build());
                }
                catalogSyncResponse.addChannelItemType(channelItemType);
            }
        }
        return catalogSyncResponse;
    }


    private boolean validateRow(Row row) {

        if ( StringUtils.isNotBlank(row.getColumnValue("Inactive Reason"))){
            LOGGER.info("Skipped row {}, due to inactive reason {}",row,row.getColumnValue("Inactive Reason"));
            return false;
        }   else if ( row.getColumnValue("Listing Archival") == null
                || ("ARCHIVED").equalsIgnoreCase(row.getColumnValue("Listing Archival"))) {
            LOGGER.info("Skipped row {}, as either its  Listing Archival is null or has value ARCHIVED",row);
            return false;
        }   else if ( row.getColumnValue("Fulfillment By") == null
                || !sourceCodeToFulfilmentModeList.get(TenantRequestContext.current().getSourceCode()).stream().anyMatch(row.getColumnValue("Fulfillment By")::equalsIgnoreCase)) {
            LOGGER.info("Skipped row {}, Fulfiment mode not match",row);
            return false;
        }

        if ( "FLIPKART_OMNI".equalsIgnoreCase(TenantRequestContext.current().getSourceCode())
                && "SELLER".equalsIgnoreCase(row.getColumnValue("Shipping Provider"))) {
            LOGGER.info("Skipped row {}, For FLIPKART_OMNI we don't fetch listing have SHIPPING_PROVDER : SELLER",row);
            return false;
        }
        return true;
    }

    private void fillAsyncDetails(StockFileDownloadRequestStatusResponse stockFileDownloadRequestStatusResponse,CatalogPreProcessorResponse catalogPreProcessorResponse, boolean isAsyncRun) {

        if ( !isAsyncRun ) {
            CatalogPreProcessorResponse.AsyncDetails asyncDetails = new CatalogPreProcessorResponse.AsyncDetails();
            asyncDetails.setNextRunInMs(1000*(stockFileDownloadRequestStatusResponse.getTotalCount() - stockFileDownloadRequestStatusResponse.getProcessed_count())/50);
            asyncDetails.setRetryCount(2);
            catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.PROCESSING);
            catalogPreProcessorResponse.setAsyncDetails(asyncDetails);
        }

    }

    private String getChannelProductIdBySourceCode(Row row, String sourceCode) {
        if ( "FLIPKART_FA".equalsIgnoreCase(sourceCode))
            return row.getColumnValue("Flipkart Serial Number");

        return row.getColumnValue("Listing ID");
    }

    private SearchShipmentRequest prepareFetchPendencyRequestForApprovedShipments(Date fromOrderDate) {

        Filter filter = new Filter();
        filter.addStatesItem(Filter.StatesEnum.APPROVED);
        filter.locationId(FlipkartRequestContext.current().getLocationId());
        filter.type(Filter.TypeEnum.PREDISPATCH);

        DateFilter dispatchAfterDateFilter = new DateFilter();
        dispatchAfterDateFilter.from(DateUtils.dateToString(DateUtils.getCurrentTime(),DATE_PATTERN));
        filter.dispatchAfterDate(dispatchAfterDateFilter);

        if ( fromOrderDate != null) {
            DateFilter orderDateFilter = new DateFilter();
            orderDateFilter.from(DateUtils.dateToString(fromOrderDate,DATE_PATTERN));
            orderDateFilter.to(DateUtils.dateToString(DateUtils.getCurrentTime(),DATE_PATTERN));
            filter.orderDate(orderDateFilter);
        }

        Sort sort = new Sort();
        sort.setField(Sort.FieldEnum.ORDERDATE);
        sort.setOrder(Sort.OrderEnum.ASC);

        Pagination pagination = new Pagination();
        pagination.pageSize(20);

        SearchShipmentRequest searchShipmentRequest = new SearchShipmentRequest();
        searchShipmentRequest.setFilter(filter);
        searchShipmentRequest.setPagination(pagination);
        searchShipmentRequest.setSort(sort);

        return searchShipmentRequest;
    }

    private FetchOnHoldOrderRequest prepareFetchOnHoldOrderRequest(int pageNumber) {

        FetchOnHoldOrderRequest.Pagination pagination = new FetchOnHoldOrderRequest.Pagination();
        pagination.setPageNumber(pageNumber);
        pagination.setPageSize(50);

        FetchOnHoldOrderRequest.Params params = new FetchOnHoldOrderRequest.Params();
        params.setOnHold(true);
        params.setSellerId(FlipkartRequestContext.current().getSellerId());

        FetchOnHoldOrderRequest.Payload payload = new FetchOnHoldOrderRequest.Payload();
        payload.setPagination(pagination);
        payload.setParams(params);

        FetchOnHoldOrderRequest fetchOnHoldOrderRequest = new FetchOnHoldOrderRequest();
        fetchOnHoldOrderRequest.setStatus("shipments_upcoming");
        fetchOnHoldOrderRequest.setPayload(payload);

        return fetchOnHoldOrderRequest;
    }

    private boolean getPendencyOfOnHoldsOrders(Map<String,Pendency> channelProductIdToPendency) {

        int pageNumber = 1;
        boolean hasMore = false;

        do {
            hasMore = false;

            FetchOnHoldOrderRequest fetchOnHoldOrderRequest = prepareFetchOnHoldOrderRequest(pageNumber);
            String fetchPendencyResponse = flipkartSellerPanelService.getOnHoldOrdersFromPanel(fetchOnHoldOrderRequest);
            if ( fetchPendencyResponse != null ) {
                try {
                    JsonObject jsonObject = new Gson().fromJson(fetchPendencyResponse, JsonObject.class);
                    hasMore = jsonObject.get("has_more").getAsBoolean();
                    JsonArray orders = jsonObject.getAsJsonArray("items");
                    Iterator<JsonElement> ordersIterator = orders.iterator();
                    while (ordersIterator.hasNext()) {
                        JsonObject order = ordersIterator.next().getAsJsonObject();
                        JsonArray orderItems = order.get("order_items").getAsJsonArray();
                        Iterator<JsonElement> orderItemsIterator = orderItems.iterator();
                        while (orderItemsIterator.hasNext()) {
                            JsonElement orderItem = orderItemsIterator.next();
                            String listingId = orderItem.getAsJsonObject().get("listing_id").getAsString();
                            if (channelProductIdToPendency.computeIfPresent(listingId, (key, val) -> val.addRequiredInventory(orderItem.getAsJsonObject().get("quantity").getAsInt())) == null) {
                                Pendency pendency = new Pendency();
                                pendency.setChannelProductId(listingId);
                                pendency.setProductName(orderItem.getAsJsonObject().get("product_details").getAsJsonObject().get("title").getAsString());
                                pendency.setRequiredInventory(orderItem.getAsJsonObject().get("quantity").getAsInt());
                                pendency.setSellerSkuCode(orderItem.getAsJsonObject().get("sku").getAsString().replaceAll("&quot;", ""));
                                channelProductIdToPendency.put(listingId,pendency);
                            }
                        }
                    }
                } catch (JsonSyntaxException e) {
                    LOGGER.error("Unable to parse response or Invalid Json, json ", fetchPendencyResponse);
                    return false;
                }
            }
            pageNumber++;
        } while (hasMore);

        return true;
    }

    private boolean getPendencyOfApprovedShipments(Map<String, Pendency> channelProductIdToPendency) {

        Set<String> shipmentIdSet = new HashSet<>();
        Date orderDateOfLastOrderOfLastpage = null;
        String nextPageUrl = null;
        boolean postApiCall = true;
        boolean hasMore = false;
        int pageNumber = 1;
        SearchShipmentV3Response searchShipmentV3Response = null;

        do {
            LOGGER.info("Fetching pendency for page number  : {}", pageNumber);
            hasMore = false;

            if (postApiCall) {
                SearchShipmentRequest searchShipmentRequest = prepareFetchPendencyRequestForApprovedShipments(orderDateOfLastOrderOfLastpage);
                searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentPost(searchShipmentRequest);
                postApiCall = false;
            }
            else {
                searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentGet(nextPageUrl);
            }
            if ( searchShipmentV3Response != null ) {
                for (Shipment shipment : searchShipmentV3Response.getShipments()) {
                    // Adding shipments in a HashSet to avoid duplicity of shipments in api response
                    if ( shipmentIdSet.add(shipment.getShipmentId())) {
                        for (OrderItem orderItem : shipment.getOrderItems()) {
                            String listingId = orderItem.getListingId();
                            if (channelProductIdToPendency.computeIfPresent(listingId, (key, val) -> val.addRequiredInventory(orderItem.getQuantity())) == null) {
                                Pendency pendency = new Pendency();
                                pendency.setChannelProductId(listingId);
                                pendency.setProductName(orderItem.getTitle());
                                pendency.setRequiredInventory(orderItem.getQuantity());
                                pendency.setSellerSkuCode(orderItem.getSku());
                                channelProductIdToPendency.put(listingId,pendency);
                            }
                        }
                    }
                    if ( (pageNumber % 249 ) == 0) {
                        Date orderDate = shipment.getOrderItems().get(0).getOrderDate();
                        orderDateOfLastOrderOfLastpage = orderDateOfLastOrderOfLastpage != null ? orderDateOfLastOrderOfLastpage : orderDate;
                        if ( (orderDate.getTime()-orderDateOfLastOrderOfLastpage.getTime()) > 0){
                            orderDateOfLastOrderOfLastpage = orderDate;
                        }
                        postApiCall = true;
                    }
                }

                nextPageUrl = searchShipmentV3Response.getNextPageUrl();
                if ( searchShipmentV3Response.isHasMore()) {
                    hasMore = true;
                }
            }

            pageNumber++;
        } while (hasMore);

        LOGGER.info("Fetched pendency till page number :{}", pageNumber);

        return true;
    }

    private List<SaleOrder> createSaleOrder(SearchShipmentV3Response searchShipmentV3Response, boolean ExpressShipment) {

        List<String> shipmentIds = searchShipmentV3Response.getShipments().stream().map(Shipment::getShipmentId).collect(Collectors.toList());
        String commaSepratedShipmentIds = StringUtils.join(shipmentIds);
        ShipmentDetailsV3WithAddressResponse shipmentDetailsWithAddressSearchResponse = flipkartSellerApiService.getShipmentDetailsWithAddress(commaSepratedShipmentIds);

        Map<String,ShipmentDetails> shipmentIdToShipmentAddressDetails = shipmentDetailsWithAddressSearchResponse.getShipments().stream().collect(
                Collectors.toMap(ShipmentDetails::getShipmentId, Function.identity() ));

        List<SaleOrder> saleOrderList = new ArrayList<>();

        for (Shipment shipment: searchShipmentV3Response.getShipments()) {

            ShipmentDetails shipmentDetailsWithAddress = shipmentIdToShipmentAddressDetails.get(shipment.getShipmentId());

            SaleOrder saleOrder = new SaleOrder();
            saleOrder.setCode(shipment.getShipmentId());
            saleOrder.setDisplayOrderCode(shipment.getOrderItems().get(0).getOrderId());
            saleOrder.setDisplayOrderDateTime(shipment.getOrderItems().get(0).getOrderDate());
            saleOrder.setFulfillmentTat(shipment.getDispatchByDate());
            saleOrder.setChannel(ChannelSource.FLIPKART.getChannelSourceCode());
            saleOrder.setCashOnDelivery("COD".equalsIgnoreCase(shipment.getOrderItems().get(0).getPaymentType().name()));
            String customerPhoneNumber = shipmentDetailsWithAddress.getDeliveryAddress().getContactNumber();
            saleOrder.setNotificationMobile(StringUtils.isNotBlank(customerPhoneNumber) ? customerPhoneNumber : DEFAULT_PHONE_NUMBER);

            if ( ExpressShipment )
                saleOrder.setPriority(1);

            List<SaleOrderItem> saleOrderItems = new ArrayList<>();
            for (OrderItem orderItem: shipment.getOrderItems()) {
                if ( "CANCELLED".equalsIgnoreCase(orderItem.getStatus().name())) {
                    LOGGER.info("Skipping orderItem : {}, orderCode : {}, since it's status is CANCELLED",orderItem.getOrderItemId(),orderItem.getOrderId());
                }
                else {
                    for (int count = 0; count < orderItem.getQuantity(); count++) {
                        SaleOrderItem saleOrderItem = new SaleOrderItem();
                        saleOrderItem.setCode(orderItem.getOrderItemId() + "-" + (count + 1));
                        BigDecimal quantity = BigDecimal.valueOf(orderItem.getQuantity());
                        saleOrderItem.setChannelSaleOrderItemCode(orderItem.getOrderItemId());
                        saleOrderItem.setItemSku(orderItem.getSku().replaceAll("&quot;", ""));
                        saleOrderItem.setChannelProductId(orderItem.getFsn().replaceAll("&quot;", ""));

                        String itemName = orderItem.getTitle() + orderItem.getSku().replaceAll("&quot;", "");
                        saleOrderItem.setItemName(itemName.length() > 200 ? itemName.substring(0,200) : itemName);
                        saleOrderItem.setShippingMethodCode("STD");

                        BigDecimal sellingPrice = orderItem.getPriceComponents().getSellingPrice();
                        BigDecimal customerPrice = orderItem.getPriceComponents().getCustomerPrice();
                        BigDecimal shippingCharges = orderItem.getPriceComponents().getShippingCharge();
                        BigDecimal totalPrice = orderItem.getPriceComponents().getTotalPrice();
                        BigDecimal discount = orderItem.getPriceComponents().getFlipkartDiscount();

                        //in v3, customer price includes shipping charges, so to get the true customer price, we always need to subtract shipping charges from it.
                        discount = customerPrice.subtract(shippingCharges).subtract(sellingPrice);

                        if (NumberUtils.lessThan(discount,new BigDecimal(0)))
                            discount =  discount.multiply(new BigDecimal(-1));

                        if (NumberUtils.lessThan(customerPrice.subtract(shippingCharges),sellingPrice))
                            sellingPrice = customerPrice.subtract(shippingCharges);

                        saleOrderItem.setSellingPrice(NumberUtils.divide(sellingPrice,quantity));
                        saleOrderItem.setDiscount(NumberUtils.divide(discount,quantity));
                        saleOrderItem.setShippingCharges(NumberUtils.divide(orderItem.getPriceComponents().getSellingPrice(),quantity));
                        saleOrderItem.setTotalPrice(NumberUtils.divide(orderItem.getPriceComponents().getTotalPrice(),quantity));
                        saleOrderItem.setPacketNumber(1);
                        saleOrderItems.add(saleOrderItem);
                    }
                }
            }
            saleOrder.setSaleOrderItems(saleOrderItems);

            List<AddressDetail> addresses = new ArrayList<>();
            addresses.add(getAddressDetails(shipmentDetailsWithAddress.getDeliveryAddress(),"1"));
            saleOrder.setAddresses(addresses);

            AddressRef shippingAddressRef = new AddressRef();
            shippingAddressRef.setReferenceId(addresses.get(0).getId());
            saleOrder.setShippingAddress(shippingAddressRef);

            AddressRef billingAddressRef = new AddressRef();
            billingAddressRef.setReferenceId(addresses.get(0).getId());
            saleOrder.setBillingAddress(billingAddressRef);

            saleOrderList.add(saleOrder);
        }

        return saleOrderList;
    }

    private AddressDetail getAddressDetails(Address address,String id) {
        AddressDetail addressDetail = new AddressDetail();
        addressDetail.setName(address.getFirstName() + " " + address.getLastName());
        addressDetail.setAddressLine1(address.getAddressLine1());

        if (addressDetail.getAddressLine2() != null) {
            addressDetail.setAddressLine2(address.getAddressLine2());
        }

        addressDetail.setCity(address.getCity());
        addressDetail.setState(address.getState());
        addressDetail.setCountry("IN");
        addressDetail.setPincode(address.getPinCode());

        if (addressDetail.getPhone() != null) {
            addressDetail.setPhone(address.getContactNumber());
        } else {
            addressDetail.setPhone(DEFAULT_PHONE_NUMBER);
        }

        addressDetail.setId(id);
        return addressDetail;

    }

    private UpdateInventoryV3Request prepareFlipkartInventoryUpdateRequest(UpdateInventoryRequest updateInventoryRequest) {

        UpdateInventoryV3Request updateInventoryV3Request = new UpdateInventoryV3Request();

        for (UpdateInventoryRequest.Listing listing : updateInventoryRequest.getListings()) {

            UpdateInventoryV3Request.Location location = new UpdateInventoryV3Request.Location();
            location.setId(FlipkartRequestContext.current().getLocationId());
            location.setInventory(listing.getQuantity());

            UpdateInventoryV3Request.SkuDetails skuDetails = new UpdateInventoryV3Request.SkuDetails();
            skuDetails.setProductId(getProductIdForInventoryUpdate(listing.getChannelProductId(), listing.getAttributes()));
            skuDetails.addLocation(location);
            updateInventoryV3Request.addSku(listing.getChannelSkuCode(), skuDetails);
        }
        return updateInventoryV3Request;
    }

    private String getProductIdForInventoryUpdate(String channelProductId, List<UpdateInventoryRequest.Attributes> attributes) {

        String fsn = null;
        Optional<UpdateInventoryRequest.Attributes> attribute = attributes.stream().filter(lineItem -> "fsn".equalsIgnoreCase(lineItem.getName())).findFirst();

        if (attribute.isPresent()) {
            fsn = attribute.get().getValue();
        } else {
            fsn = channelProductId.substring(3, channelProductId.length() - 6);
        }

        return fsn;
    }

    private DispatchStandardShipmentV3Request prepareDispatchStandardShipmentRequest(CloseShippingManifestRequest closeShippingManifestRequest, Map<String, String> saleOrderCodeToShippingPackageCode) {

        DispatchStandardShipmentV3Request dispatchStandardShipmentV3Request = new DispatchStandardShipmentV3Request();
        for (CloseShippingManifestRequest.ShippingManifestItems shippingManifestItem : closeShippingManifestRequest.getShippingManifestItems()) {
            dispatchStandardShipmentV3Request.addShipmentId(shippingManifestItem.getSaleOrderCode());
            saleOrderCodeToShippingPackageCode.put(shippingManifestItem.getSaleOrderCode(),shippingManifestItem.getShippingPackageCode());
        }
        dispatchStandardShipmentV3Request.setLocationId(FlipkartRequestContext.current().getLocationId());

        return dispatchStandardShipmentV3Request;
    }

    private DispatchSelfShipmentV3Request prepareDispatchSelfShipmentRequest(CloseShippingManifestRequest closeShippingManifestRequest, Map<String, String> shippingPackageCodeToSaleOrderCode) {
        DispatchSelfShipmentV3Request dispatchSelfShipmentV3Request = new DispatchSelfShipmentV3Request();
        for (CloseShippingManifestRequest.ShippingManifestItems shippingManifestItem : closeShippingManifestRequest.getShippingManifestItems()) {
            DispatchRequest shipment = new DispatchRequest();

            HashSet<String> combinationIdentifierSet = new HashSet<>();
            HashSet<String> channelSaleOrderItemCodeSet = new HashSet<>();
            for (SaleOrderItem saleOrderItem : shippingManifestItem.getSaleOrderItems()) {
                ConfirmItemRow confirmItemRow = new ConfirmItemRow();
                // Inside if block handle bundle sku
                if ( saleOrderItem.getCombinationIdentifier() != null
                        && combinationIdentifierSet.add(saleOrderItem.getCombinationIdentifier())
                        && channelSaleOrderItemCodeSet.add(saleOrderItem.getChannelSaleOrderItemCode()) ) {
                    confirmItemRow.orderItemId(saleOrderItem.getChannelSaleOrderItemCode());
                    confirmItemRow.quantity(1);
                }
                else {
                    if ( channelSaleOrderItemCodeSet.add(saleOrderItem.getChannelSaleOrderItemCode())){
                        confirmItemRow.orderItemId(saleOrderItem.getChannelSaleOrderItemCode());
                        confirmItemRow.quantity(1);
                    } else {
                        shipment.incrementOrderItemQuantityByOne(saleOrderItem.getChannelSaleOrderItemCode());
                    }
                }
                shipment.addOrderItemsItem(confirmItemRow);
            }

            shipment.setShipmentId(shippingManifestItem.getSaleOrderCode());
            shipment.setDispatchDate(shippingManifestItem.getDispatchDate());
            shipment.setLocationId(FlipkartRequestContext.current().getLocationId());
            shipment.setTrackingId(shippingManifestItem.getTrackingNumber());
            shipment.setTentativeDeliveryDate((DateUtils.addDaysToDate(DateUtils.getCurrentDate(),5)));
            shipment.setDeliveryPartner(closeShippingManifestRequest.getShippingProvider());
            dispatchSelfShipmentV3Request.addShipmentsItem(shipment);
        }
        return dispatchSelfShipmentV3Request;
    }

    private String getVendorGroupCode(String shippingProviderCode) {
        String vendorGroupCode = shippingProviderCode;
        if ( shippingProviderCode.contains("Delhivery") )
            vendorGroupCode = "Delhivery";
        else if ( shippingProviderCode.contains("E-Kart Logistics"))
            vendorGroupCode = "Ekart Logistics";
        else if ( shippingProviderCode.contains("Ecom"))
            vendorGroupCode = "Ecom Express";

        return vendorGroupCode;
    }

}

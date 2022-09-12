package com.uniware.integrations.client.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.unifier.core.fileparser.DelimitedFileParser;
import com.unifier.core.fileparser.ExcelSheetParser;
import com.unifier.core.fileparser.Row;
import com.unifier.core.utils.DateUtils;
import com.unifier.core.utils.JsonUtils;
import com.unifier.core.utils.NumberUtils;
import com.unifier.core.utils.StringUtils;
import com.uniware.integrations.client.annotation.FlipkartClient;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.constants.EnvironmentPropertiesConstant;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.dto.Address;
import com.uniware.integrations.client.dto.Dimensions;
import com.uniware.integrations.client.dto.DispatchShipmentStatus;
import com.uniware.integrations.client.dto.Filter;
import com.uniware.integrations.client.dto.Invoice;
import com.uniware.integrations.client.dto.OrderItem;
import com.uniware.integrations.client.dto.OrderItems;
import com.uniware.integrations.client.dto.PackRequest;
import com.uniware.integrations.client.dto.SerialNumber;
import com.uniware.integrations.client.dto.Shipment;
import com.uniware.integrations.client.dto.ShipmentDetails;
import com.uniware.integrations.client.dto.SubShipments;
import com.uniware.integrations.client.dto.TaxItem;
import com.uniware.integrations.client.dto.api.requestDto.DispatchStandardShipmentV3Request;
import com.uniware.integrations.client.dto.api.requestDto.UpdateInventoryV3Request;
import com.uniware.integrations.client.dto.api.responseDto.DispatchStandardShipmentV3Response;
import com.uniware.integrations.client.dto.api.responseDto.InvoiceDetailsResponseV3;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentPackV3Response;
import com.uniware.integrations.client.dto.api.responseDto.StockFileDownloadNUploadHistoryResponse;
import com.uniware.integrations.client.dto.api.responseDto.StockFileDownloadRequestStatusResponse;
import com.uniware.integrations.client.dto.api.responseDto.UpdateInventoryV3Response;
import com.uniware.integrations.client.dto.uniware.CatalogPreProcessorRequest;
import com.uniware.integrations.client.dto.uniware.CatalogSyncRequest;
import com.uniware.integrations.client.dto.uniware.CatalogSyncResponse;
import com.uniware.integrations.client.dto.uniware.ChannelItemType;
import com.uniware.integrations.client.dto.uniware.CloseShippingManifestRequest;
import com.uniware.integrations.client.dto.uniware.CloseShippingManifestResponse;
import com.uniware.integrations.client.dto.uniware.CreateInvoiceResponse;
import com.uniware.integrations.client.dto.uniware.DispatchShipmentRequest;
import com.uniware.integrations.client.dto.uniware.Error;
import com.uniware.integrations.client.dto.uniware.FetchOrderRequest;
import com.uniware.integrations.client.dto.api.requestDto.ShipmentPackV3Request;
import com.uniware.integrations.client.dto.api.responseDto.AuthTokenResponse;
import com.uniware.integrations.client.dto.api.responseDto.SearchShipmentResponseV3;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentDetailsSearchResponseV3;
import com.uniware.integrations.client.dto.uniware.AddressDetail;
import com.uniware.integrations.client.dto.uniware.AddressRef;
import com.uniware.integrations.client.dto.uniware.FetchOrderResponse;
import com.uniware.integrations.client.dto.uniware.FetchPendencyRequest;
import com.uniware.integrations.client.dto.uniware.GenerateInvoiceRequest;
import com.uniware.integrations.client.dto.uniware.GenerateLabelRequest;
import com.uniware.integrations.client.dto.uniware.Pendency;
import com.uniware.integrations.client.dto.uniware.SaleOrder;
import com.uniware.integrations.client.dto.uniware.SaleOrderItem;
import com.uniware.integrations.client.dto.uniware.UpdateInventoryRequest;
import com.uniware.integrations.client.dto.uniware.UpdateInventoryResponse;
import com.uniware.integrations.client.service.FlipkartSellerApiService;
import com.uniware.integrations.client.service.FlipkartSellerPanelService;
import com.uniware.integrations.core.dto.api.Response;
import com.uniware.integrations.client.dto.uniware.PreConfigurationResponse;
import com.uniware.integrations.uniware.invoice.response.dto.InvoiceTaxDetailsFromChannel;
import com.uniware.integrations.utils.ResponseUtil;
import com.uniware.integrations.web.context.TenantRequestContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


/**
 * Created by vipin on 20/05/22.
 */

@Service(value = "flipkartDropshipServiceImpl")
@FlipkartClient(version = "v1",channelSource = { ChannelSource.FLIPKART_DROPSHIP, ChannelSource.FLIPKART_WHOLESALE})
public class FlipkartDropshipServiceImpl extends AbstractSalesFlipkartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlipkartDropshipServiceImpl.class);

    @Autowired
    private Environment environment;

    @Autowired
    private FlipkartSellerApiService flipkartSellerApiService;

    @Autowired
    private FlipkartSellerPanelService flipkartSellerPanelService;

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
        return ResponseUtil.success("SUCCESS", channelPreConfigurationResponse);
    }

    @Override public Response postConfiguration(Map<String, String> headers, String payload, String connectorName) {

        AuthTokenResponse authTokenResponse = flipkartSellerApiService.getAuthToken(headers,payload);

        if ( authTokenResponse !=null && authTokenResponse.getAccessToken() != null ) {
            String authToken = authTokenResponse.getTokenType() + " " + authTokenResponse.getAccessToken();
            String authTokenExpireIn = authTokenResponse.getExpiresIn();
            String refreshToken = authTokenResponse.getRefreshToken();

            HashMap<String, String> responseParam = new HashMap<>();
            responseParam.put("authToken", authToken);
            responseParam.put("refreshToken", refreshToken);
            responseParam.put("authTokenExpiresIn", authTokenExpireIn);
            return ResponseUtil.success("SUCCESS", responseParam);
        }

        return ResponseUtil.failure("Unable to generate AuthToken");
    }

    @Override public Response connectorVerification(Map<String, String> headers, String payload) {

        boolean loginSuccess = flipkartSellerPanelService.sellerPanelLogin(FlipkartRequestContext.current().getUserName(), FlipkartRequestContext.current().getPassword(), false);
        if (!loginSuccess) {
            return ResponseUtil.failure("Unable to login on Flipkart panel.");
        }
        return ResponseUtil.success("Logged in Successfully");
    }


    public List<String> getFullfimentByListSourceCode(String sourceCode) {
        List<String> fullfilmentByList = new ArrayList<>();
        if ( StringUtils.equalsIngoreCaseAny(sourceCode,"FLIPKART","2GUD","FLIPKART_OMNI")){
            fullfilmentByList.add("seller");
        }
        else if ( StringUtils.equalsIngoreCaseAny(sourceCode,"FLIPKART_LITE","FLIPKART_SMART")) {
            fullfilmentByList.add("SellerSmart");
            fullfilmentByList.add("Seller Smart");
            fullfilmentByList.add("FA & SellerSmart");
            fullfilmentByList.add("Flipkart and Seller Smart");
        }
        else if ( StringUtils.equalsIngoreCaseAny(sourceCode,"FLIPKART_FA") ) {
            fullfilmentByList.add("FA");
            fullfilmentByList.add("FA & SellerSmart");
        }
        return  fullfilmentByList;
    }

    /*
        Description - Returns latest file of same day with following properties operationType = GENERATED, errorRowExist = false, fileFormat - CSV or XLS, uploadedOn is today date.
        Note - Order of files in StockFileResponseList is from PRESENT TO PAST
     */
    public String getStockFile(StockFileDownloadNUploadHistoryResponse stockFileDownloadNUploadHistoryResponse) {

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

    private CatalogSyncResponse fetchCatalogInternal(Iterator<Row> rows, int pageSize) {

        CatalogSyncResponse catalogSyncResponse = null;
        while ( rows.hasNext() && pageSize-- > 0) {
            Row row = rows.next();
            boolean isValid = validateRow(row);
            if (isValid) {
                ChannelItemType channelItemType = new ChannelItemType.Builder()
                        .setChannelCode(TenantRequestContext.current().getChannelCode())
                        .setChannelProductId(getChannelProductIdBySourceCode(row,TenantRequestContext.current().getSourceCode()))
                        .setSellerSkuCode(row.getColumnValue("Seller SKU Id"))
                        .setProductName(row.getColumnValue("Product Title"))
                        .setSellingPrice(new BigDecimal(row.getColumnValue("Your Selling Price")))
                        .setMrp(new BigDecimal(row.getColumnValue("MRP")))
                        .setCurrencyCode("INR")
                        .build();

                channelItemType.addAttribute(new ChannelItemType.Attribute.Builder()
                        .setName("FSN")
                        .setValue(row.getColumnValue("Flipkart Serial Number"))
                        .build());

                catalogSyncResponse.addChannelItemType(channelItemType);
            }
        }
        if ( rows.hasNext())
            catalogSyncResponse.setHasMore(true);

        return catalogSyncResponse;
    }

    private boolean validateRow(Row row) {
        if ( "FLIPKART_OMNI".equalsIgnoreCase(TenantRequestContext.current().getSourceCode())
                && "SELLER".equalsIgnoreCase(row.getColumnValue("Shipping Provider"))) {
            LOGGER.info("Skipped row {}, For FLIPKART_OMNI we don't fetch listing have SHIPPING_PROVDER : SELLER",row.toString());
            return false;
        }
        return true;
    }

    private String getChannelProductIdBySourceCode(Row row, String sourceCode) {
        if ( "FLIPKART_FA".equalsIgnoreCase(sourceCode))
            return row.getColumnValue("Flipkart Serial Number");
        if ( "FLIPKART_LITE".equalsIgnoreCase(sourceCode))
            return row.getColumnValue("Seller SKU Id");

        return row.getColumnValue("Listing ID");
    }

    public Response fetchCatalog(Map<String, String> headers, CatalogSyncRequest catalogSyncRequest) {

        CatalogSyncResponse catalogSyncResponse = null;

        String stockFilePath = catalogSyncRequest.getStockFilePath();
        String fileFormat = stockFilePath.substring(stockFilePath.lastIndexOf('.'));
        int skipIntialLines = catalogSyncRequest.getPageNumber() * catalogSyncRequest.getPageSize();
        Iterator<Row> rows = null;
        if ( ("CSV").equalsIgnoreCase(fileFormat)){
            rows = new DelimitedFileParser("stockFilePath").parse(skipIntialLines,true);
        }
        else if (("XLS").equalsIgnoreCase(fileFormat)) {
            rows = new ExcelSheetParser("stockFilePath").parse(skipIntialLines,true);
        }

        catalogSyncResponse = fetchCatalogInternal(rows, catalogSyncRequest.getPageSize());

        if ( catalogSyncResponse != null )
            ResponseUtil.success("Catalog synced successfully", catalogSyncResponse);

        return null;
    }



    @Override public Response catalogSyncPreProcessor(Map<String, String> headers, CatalogPreProcessorRequest catalogPreProcessorRequest) {

        boolean loginSuccess = flipkartSellerPanelService.sellerPanelLogin(FlipkartRequestContext.current().getUserName(), FlipkartRequestContext.current().getPassword(), false);
        if (!loginSuccess) {
            return ResponseUtil.failure("Login Session could not be established");
        }

        String stockfilePath = null;

        // check if already a file is under generation process or not.
        StockFileDownloadRequestStatusResponse stockFileDownloadRequestStatusResponse = flipkartSellerPanelService.getStockFileDownloadRequestStatus();
        if ( "PROCESSING".equalsIgnoreCase(stockFileDownloadRequestStatusResponse.getDownloadState())){
            LOGGER.info("File generation is in PROCESSING state. Will retry after sometime...");
            ResponseUtil.success("File generation is in PROCESSING state. Wait for sometime...", stockFileDownloadRequestStatusResponse);
        }
        else if ( "COMPLETED".equalsIgnoreCase(stockFileDownloadRequestStatusResponse.getDownloadState())) {
            LOGGER.info("Stock file generation completed.");
        }
        else {
            LOGGER.error("ALERT - CASE NOT HANDLED");
        }

        // Check if the stock file exist in StockFileDownloadNUploadHistory if yes then download the it otherwise generate a new one
        StockFileDownloadNUploadHistoryResponse stockFileDownloadNUploadHistoryResponse = flipkartSellerPanelService.getStockFileDownloadNUploadHistory();
        stockfilePath = getStockFile(stockFileDownloadNUploadHistoryResponse);

        // todo manual run check
        if ( stockfilePath == null ) {
            boolean isRequestStockFileSuccessful = flipkartSellerPanelService.requestStockFile();
            if (isRequestStockFileSuccessful) {
                StockFileDownloadRequestStatusResponse stockFileDownloadRequestStatusResponse1 = flipkartSellerPanelService.getStockFileDownloadRequestStatus();
                ResponseUtil.success("Requested a new stock file. Wait for sometime...", stockFileDownloadRequestStatusResponse1);
            } else {
                return ResponseUtil.failure("Getting Error while requesting report...");
            }
        }

        HashMap<String,String> fileDetails = new HashMap<>(1);
        fileDetails.put("filePath",stockfilePath);
        return ResponseUtil.success("File downloaded Successfully", fileDetails);

    }

    @Override public Response fetchPendency(Map<String, String> headers, FetchPendencyRequest fetchPendencyRequest) {

        SearchShipmentResponseV3 searchShipmentResponse = null;
        Map<String, Pendency> fsnToPendency =new HashMap<>();
        searchShipmentResponse = flipkartSellerApiService.searchPreDispatchShipmentPost(0,null,false,true );
        if ( searchShipmentResponse != null ) {
            fsnToPendency.putAll(fetchPendencyInternal(searchShipmentResponse));
            while ( searchShipmentResponse.isHasMore() ) {
                searchShipmentResponse = flipkartSellerApiService.searchPreDispatchShipmentGet(searchShipmentResponse.getNextPageUrl());
                if ( searchShipmentResponse != null ) {
                    fsnToPendency.putAll(fetchPendencyInternal(searchShipmentResponse));
                }
            }
        }

        // Todo - prepare fetchOnHoldOrderRequest, pagination
        fsnToPendency.putAll(flipkartSellerPanelService.getOnHoldOrdersFromPanel(null));
        return null;
    }

    // todo add dimensions map in additional info
    @Override  public Response fetchOrders(Map<String, String> headers, FetchOrderRequest orderSyncRequest) {

        FetchOrderResponse fetchOrderResponse = new FetchOrderResponse();
        List<SaleOrder> saleOrderList = new ArrayList<>();

        SearchShipmentResponseV3 searchNormalShipmentResponse = null;
        SearchShipmentResponseV3 searchExpressShipmentResponse = null;
        Map<String,Object> requestMetadata = orderSyncRequest.getMetdata();
        Map<String,Object> responseMetadata = new HashMap<>();
        // Fetch Normal shipments
        if (requestMetadata.get("nextPageUrlForNormalShipments") == null ) {
            int selfShipmentsAheadDaysToLookFor = 2;
            List<Filter.ShipmentTypesEnum> shipmentTypes = new ArrayList<>();
            shipmentTypes.add(Filter.ShipmentTypesEnum.NORMAL);
            shipmentTypes.add(Filter.ShipmentTypesEnum.SELF);
            searchNormalShipmentResponse = flipkartSellerApiService.searchPreDispatchShipmentPost(selfShipmentsAheadDaysToLookFor,shipmentTypes,false,false );
        } else {
            searchNormalShipmentResponse = flipkartSellerApiService.searchPreDispatchShipmentGet((String) requestMetadata.get("nextPageUrlForNormalShipments"));
        }

        // Fetch Express shipments
        if (requestMetadata.get("nextPageUrlForExpressShipments") == null ) {
            int selfShipmentsAheadDaysToLookFor = 0;
            List<Filter.ShipmentTypesEnum> shipmentTypes = new ArrayList<>();
            shipmentTypes.add(Filter.ShipmentTypesEnum.EXPRESS);
            searchExpressShipmentResponse = flipkartSellerApiService.searchPreDispatchShipmentPost(selfShipmentsAheadDaysToLookFor,shipmentTypes,true, false);

        } else {
            searchExpressShipmentResponse = flipkartSellerApiService.searchPreDispatchShipmentGet((String) requestMetadata.get("nextPageUrlForExpressShipments"));
        }

        if (searchNormalShipmentResponse != null ){
            List<SaleOrder> normalSaleOrderList = getOrdersInternal(headers,searchNormalShipmentResponse);
            saleOrderList.addAll(normalSaleOrderList);
            if ( searchNormalShipmentResponse.isHasMore())
                responseMetadata.put("nextPageUrlForNormalShipments",searchNormalShipmentResponse.getNextPageUrl());
        }


        if (searchExpressShipmentResponse != null ) {
            List<SaleOrder> expressSaleOrderList = getOrdersInternal(headers, searchNormalShipmentResponse);
            saleOrderList.addAll(expressSaleOrderList);
            if ( searchExpressShipmentResponse.isHasMore())
                responseMetadata.put("nextPageUrlForExpressShipments",searchExpressShipmentResponse.getNextPageUrl());
        }

        fetchOrderResponse.setOrders(saleOrderList);
        return ResponseUtil.success("ORDERS LIST FETCHED SUCCESSFULLY!", fetchOrderResponse);

    }

    // Todo Async
    @Override public Response generateInvoice(Map<String, String> headers, GenerateInvoiceRequest generateInvoiceRequest) {

        CreateInvoiceResponse createInvoiceResponse = null;
        GenerateInvoiceRequest.ShippingPackage shippingPackage = generateInvoiceRequest.getShippingPackage();
        ShipmentDetailsSearchResponseV3 shipmentDetails = flipkartSellerApiService.getShipmentDetails(shippingPackage.getSaleOrder().getCode());
        if (shipmentDetails.getShipments() == null) {
            //todo throw  error response
        }

        List<OrderItem> orderItems = shipmentDetails.getShipments().get(0).getOrderItems();
        boolean isLabelGenerated = !orderItems.stream().filter(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("approved")).findAny().isPresent();
        boolean isShipmentReadyToShip = orderItems.stream().filter(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("PACKED")).findAny().isPresent();
        boolean packingInProgress = orderItems.stream().filter(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("PACKING_IN_PROGRESS")).findAny().isPresent();
        orderItems.stream().forEach(orderItem -> LOGGER.info("Shipment:{} order item id:{}, listingId:{}, status :{}, quantity:{} ", shippingPackage.getSaleOrder().getCode(), orderItem.getOrderItemId(),orderItem.getListingId(), orderItem.getStatus(), orderItem.getQuantity()));

        if(packingInProgress){
            //todo throw  error response
        }

        if(!isLabelGenerated){
            boolean isPackConfirmed = packShipment(shippingPackage);
            if(isPackConfirmed){
                isShipmentReadyToShip = true;
                isLabelGenerated = true;
            }else{
                //todo throw  error response
            }
        }


//        Map<String,String> saleOrderItemCodeToChannelProductId = generateInvoiceRequest.getShippingPackage().getSaleOrderItems().stream().coll
        InvoiceDetailsResponseV3 invoiceDetailsResponseV3 = flipkartSellerApiService.getInvoicesInfo(generateInvoiceRequest.getShippingPackage().getSaleOrder().getCode());
        if ( invoiceDetailsResponseV3 != null ){
            createInvoiceResponse.setInvoiceCode(invoiceDetailsResponseV3.getInvoices().get(0).getInvoiceNumber());
            createInvoiceResponse.setDisplayCode(invoiceDetailsResponseV3.getInvoices().get(0).getInvoiceNumber());
            createInvoiceResponse.setChannelCreatedTime(invoiceDetailsResponseV3.getInvoices().get(0).getInvoiceDate().toDate());
            for (OrderItems orderItem: invoiceDetailsResponseV3.getInvoices().get(0).getOrderItems()) {
//             todo   createInvoiceResponse.set
            }
        }

        return null;
    }

    private boolean packShipment(GenerateInvoiceRequest.ShippingPackage shippingPackage) {
        ShipmentPackV3Request shipmentPackV3Request = preparePackShipmentRequest(shippingPackage);
        ShipmentPackV3Response shipmentPackV3Response = flipkartSellerApiService.packShipment(shipmentPackV3Request);
        if("SUCCESS".equalsIgnoreCase(shipmentPackV3Response.getShipments().get(0).getStatus())){
            return confirmIfShipmentPacked(shippingPackage.getSaleOrder().getCode());
        }
        return false;
    }

    private boolean confirmIfShipmentPacked(String shipmentId) {
        int retryCount = 10;
        while (--retryCount > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//      todo      if(flipkartSellerApiService.confirmShipmentPacked(shipmentId)){
//                return true;
//            }
        }
        return false;
    }

    private Map<String, Long> getSaleOrderItemToQtyMap(List<GenerateInvoiceRequest.SaleOrderItem> saleOrderItemList) {
        Set<String> combinationIdentifierSet = new HashSet<>();
        Map<String, Long> saleOrderItemToQty = saleOrderItemList.stream()
                .filter(saleOrderItem -> StringUtils.isBlank(saleOrderItem.getCombinationIdentifier()) || combinationIdentifierSet.add(saleOrderItem.getCombinationIdentifier()))
                .collect(Collectors.groupingBy(saleOrderItem -> saleOrderItem.getChannelProductId(), Collectors.counting()));
        return saleOrderItemToQty;
    }

    private ShipmentPackV3Request preparePackShipmentRequest(GenerateInvoiceRequest.ShippingPackage shippingPackage) {

        PackRequest packRequest = new PackRequest();

        Map<String,Long> saleOrderItemToQty = getSaleOrderItemToQtyMap(shippingPackage.getSaleOrderItems());

        Map<String,Integer> saleOrderItemToQtyMap = new HashMap<>();
        for (GenerateInvoiceRequest.SaleOrderItem saleOrderItem : shippingPackage.getSaleOrderItems()) {
            // Todo serial number handling
            List<String> serialNumbers = new ArrayList<>();
            if ( saleOrderItem.getItemDetails() != null ) {
                JsonObject itemDetailsJson = new Gson().fromJson(saleOrderItem.getItemDetails(), JsonObject.class);
                String imei = itemDetailsJson.getAsJsonObject().get("imei").getAsString();
                if ( StringUtils.isEmpty(imei)) {
                    imei = itemDetailsJson.getAsJsonObject().get("serialNumber").getAsString();
                }
                if ( StringUtils.isNotEmpty(imei) )
                    serialNumbers = Arrays.stream(imei.split(",")).collect(Collectors.toList());

//                packRequest.addSerialNumbersItem(new SerialNumber().orderItemId(saleOrderItem.getChannelSaleOrderItemCode()).serialNumbers(serialNumbers));
            }
        }

        SubShipments subShipment = new SubShipments().subShipmentId("SS-1").dimensions(getPackDimension(shippingPackage));
        Invoice invoice = new Invoice().orderId(shippingPackage.getSaleOrder().getDisplayOrderCode()).invoiceNumber("").invoiceDate(LocalDate.now());


        packRequest.shipmentId(shippingPackage.getSaleOrder().getCode());
        packRequest.setLocationId(FlipkartRequestContext.current().getLocationId());
//        packRequest.addSerialNumbersItem();
        packRequest.addSubShipmentsItem(subShipment);
        packRequest.addInvoicesItem(invoice);
        saleOrderItemToQtyMap.entrySet().stream().forEach(entry -> packRequest.addTaxItemsItem(new TaxItem().orderItemId(entry.getKey()).quantity(entry.getValue()).taxRate(BigDecimal.ZERO)));
        ShipmentPackV3Request shipmentPackV3Request = new ShipmentPackV3Request();
        shipmentPackV3Request.addShipmentsItem(packRequest);
        return shipmentPackV3Request;
    }

    private Dimensions getPackDimension(GenerateInvoiceRequest.ShippingPackage shippingPackage) {
        Dimensions dimensions = null;
        Map<String,String> additionalInfoMap = JsonUtils.jsonToMap(shippingPackage.getSaleOrder().getAdditionalInfo());
        if ( additionalInfoMap.get("dimensions") != null ) {
            dimensions = new Gson().fromJson(additionalInfoMap.get("dimensions"), Dimensions.class);
        }
        else if ( shippingPackage.getLength().compareTo(BigDecimal.ONE) > 1) {
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

    @Override public Response generateShipLabel(Map<String, String> headers, GenerateLabelRequest generateLabelRequest) {
        //Todo - business logic
        return null;
    }

    // Todo - handle the logic of this function by ManifestClose integration point
    @Override public Response dispatchShipments(Map<String, String> headers, DispatchShipmentRequest dispatchShipmentRequest) {
        return null;
    }

    @Override public Response closeShippingManifest(Map<String, String> headers, CloseShippingManifestRequest closeShippingManifestRequest) {

        CloseShippingManifestResponse closeShippingManifestResponse = null;
        Map<String,String> shippingPackageCodeToSaleOrderCode = new HashMap<>();
        DispatchStandardShipmentV3Request dispatchStandardShipmentRequestV3 = closeShippingManifestInternal(closeShippingManifestRequest, shippingPackageCodeToSaleOrderCode);
        DispatchStandardShipmentV3Response dispatchStandardShipmentV3Response = flipkartSellerApiService.markStandardFulfilmentShipmentsRTD(dispatchStandardShipmentRequestV3);

        if ( dispatchStandardShipmentV3Response != null ) {
            for (DispatchShipmentStatus shipments : dispatchStandardShipmentV3Response.getShipments()) {
                CloseShippingManifestResponse.ShipmentStatus shipmentStatus = new CloseShippingManifestResponse.ShipmentStatus();
                if ( "SUCCESS".equalsIgnoreCase(shipments.getStatus()) ){
                    shipmentStatus.setShipmentCode(shipments.getShipmentId());
                    shipmentStatus.setStatus("SUCCESS");
                } else if ( "FAILURE".equalsIgnoreCase(shipments.getStatus()) ) {
                    shipmentStatus.setShipmentCode(shipments.getShipmentId());
                    shipmentStatus.setStatus("FAILURE");
                    shipmentStatus.setErrorCode(shipments.getErrorCode());
                    shipmentStatus.setErrorMessage(shipments.getErrorMessage());
                    shipmentStatus.setSaleOrderCode(shippingPackageCodeToSaleOrderCode.get(shipments.getShipmentId()));
                    // Todo check whether we can identify if the shipment is cancelled or not ??
                    if ( "CANCELLED".equalsIgnoreCase(shipments.getErrorMessage()))
                        shipmentStatus.setIsCancelled(true);
                }
                closeShippingManifestResponse.addShipment(shipmentStatus);
            }

            closeShippingManifestResponse.setShippingManifestLink("");
            return ResponseUtil.success("Manifest Closed Successfully.",closeShippingManifestResponse);
        } else {
            return ResponseUtil.failure("Unable to close Manifest");
        }
    }

    @Override public Response updateInventory(Map<String, String> headers, UpdateInventoryRequest updateInventoryRequest) {

        UpdateInventoryV3Request updateInventoryV3Request = prepareFlipkartInventoryUpdateRequest(updateInventoryRequest);
        UpdateInventoryV3Response updateInventoryV3Response = flipkartSellerApiService.updateInventory(updateInventoryV3Request);
        UpdateInventoryResponse updateInventoryResponse = null;
        if ( updateInventoryV3Response.getResponseStatus().is2xxSuccessful()) {
            BeanUtils.copyProperties(updateInventoryRequest, updateInventoryResponse);
            for (UpdateInventoryResponse.Listing listing : updateInventoryResponse.getListings()) {
                String channelSkuCode = listing.getChannelSkuCode();
                UpdateInventoryV3Response.InventoryUpdateStatus listingInventoryUpdateStatus = updateInventoryV3Response.getSkus()
                        .get("channelSkuCode");
                if (listingInventoryUpdateStatus == null) {
                    listing.setStatus(UpdateInventoryResponse.Status.FAILED);
                    // TODD error code
                    listing.addError(new Error("", "unable to update inventory"));
                }
                else if ("FAILURE".equalsIgnoreCase(listingInventoryUpdateStatus.getStatus())) {
                    List<Error> errors = null;
                    List<Error> attributeErrors = null;
                    // Todo check is Beanutility usage is good practice or not
                    BeanUtils.copyProperties(listingInventoryUpdateStatus.getErrors(), errors);
                    BeanUtils.copyProperties(listingInventoryUpdateStatus.getAttributeErrors(), attributeErrors);
                    errors.addAll(attributeErrors);
                    listing.setStatus(UpdateInventoryResponse.Status.FAILED);
                    listing.setErrors(errors);
                }
                else if ("SUCCESS".equalsIgnoreCase(listingInventoryUpdateStatus.getStatus())) {
                    listing.setStatus(UpdateInventoryResponse.Status.SUCCESS);
                }
                // TODO - Check how to handle warning status
            }
            return ResponseUtil.success("SUCCESS",updateInventoryResponse);

        } else if ( updateInventoryV3Response.getResponseStatus().is4xxClientError() || updateInventoryV3Response.getResponseStatus().is5xxServerError()) {
            List<Error> errors = null;
            BeanUtils.copyProperties(updateInventoryV3Response.getErrors(), errors);
            updateInventoryResponse.setStatus(UpdateInventoryResponse.Status.FAILED);
            updateInventoryResponse.setErrors(errors);
            return ResponseUtil.success("SUCCESS",updateInventoryResponse);
        }

        return ResponseUtil.failure("Unable to update inventory");
    }


    public Response triggerInvoiceAndLabel(){
        ShipmentPackV3Request shipmentPackRequest = new ShipmentPackV3Request();

        List<PackRequest> shipments = new ArrayList<>();

        PackRequest shipmentsItem = new PackRequest();
        shipments.add(shipmentsItem);

        List<SubShipments> subShipments = new ArrayList<>();
        List<TaxItem> taxItems = new ArrayList<>();
        List<Invoice> invoices = new ArrayList<>();
        List<SerialNumber> serialNumbers = new ArrayList<>();


        shipmentPackRequest.setShipments(shipments);


        return null;
    }

    public List<SaleOrder> getOrdersInternal(Map<String, String> headers, SearchShipmentResponseV3 searchShipmentResponse) {

        List<SaleOrder> saleOrderList = new ArrayList<>();
        List<String> shipmentIds = searchShipmentResponse.getShipments().stream().map(Shipment::getShipmentId).collect(Collectors.toList());
        String commaSepratedShipmentIds = StringUtils.join(shipmentIds);
        ShipmentDetailsSearchResponseV3 shipmentDetailsSearchResponse = flipkartSellerApiService.getShipmentDetails(commaSepratedShipmentIds);

        Map<String,SaleOrder> shipmentIdToSaleOrder = new HashMap<>();
        for (Shipment shipment: searchShipmentResponse.getShipments()) {
            SaleOrder saleOrder = new SaleOrder();
            saleOrder.setCode(shipment.getShipmentId());
            saleOrder.setDisplayOrderCode(shipment.getOrderItems().get(0).getOrderId());
            saleOrder.setDisplayOrderDateTime(shipment.getOrderItems().get(0).getOrderDate().toDate());
            saleOrder.setFulfillmentTat(shipment.getDispatchByDate().toDate());
            saleOrder.setChannel(ChannelSource.FLIPKART_DROPSHIP.getChannelSourceCode());

            if ("COD".equalsIgnoreCase(shipment.getOrderItems().get(0).getPaymentType().name()))
                saleOrder.setCashOnDelivery(true);
            else
                saleOrder.setCashOnDelivery(false);

            List<SaleOrderItem> saleOrderItems = new ArrayList<>();
            for (OrderItem orderItem: shipment.getOrderItems()) {
                for (int count = 0; count < orderItem.getQuantity(); count++) {
                    SaleOrderItem saleOrderItem = new SaleOrderItem();
                    saleOrderItem.setCode(orderItem.getOrderItemId() + "-" + (count + 1));
                    BigDecimal quantity = BigDecimal.valueOf(orderItem.getQuantity());
                    saleOrderItem.setChannelSaleOrderItemCode(orderItem.getOrderItemId());
                    saleOrderItem.setCode(orderItem.getOrderItemId());
                    saleOrderItem.setItemSku(orderItem.getSku().replaceAll("&quot;", ""));
                    saleOrderItem.setChannelProductId(orderItem.getFsn().replaceAll("&quot;", ""));

                    // TODO incorrect handling detail script
                    String itemName = orderItem.getTitle() + orderItem.getSku().replaceAll("&quot;", "");
                    saleOrderItem.setItemName(itemName.length() > 200 ? itemName.substring(0,200) : itemName);
                    saleOrderItem.setShippingMethodCode("STD");

                    BigDecimal sellingPrice = orderItem.getPriceComponents().getSellingPrice();
                    BigDecimal customerPrice = orderItem.getPriceComponents().getCustomerPrice();
                    BigDecimal shippingCharges = orderItem.getPriceComponents().getShippingCharge();
                    BigDecimal totalPrice = orderItem.getPriceComponents().getTotalPrice();
                    BigDecimal discount = orderItem.getPriceComponents().getFlipkartDiscount();

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
            // TODO freebee items usecase
            saleOrder.setSaleOrderItems(saleOrderItems);
            shipmentIdToSaleOrder.put(shipment.getShipmentId(),saleOrder);
        }

        for (ShipmentDetails shipmentDetails: shipmentDetailsSearchResponse.getShipments()) {
            SaleOrder saleOrder = shipmentIdToSaleOrder.get(shipmentDetails.getShipmentId());
            saleOrder.setNotificationMobile(shipmentDetails.getDeliveryAddress().getContactNumber());
            List<AddressDetail> addresses = new ArrayList<>();
            addresses.add(getAddressDetails(shipmentDetails.getDeliveryAddress(),"DeliveryAddress"));

            saleOrder.setAddresses(addresses);

            AddressRef shippingAddressRef = new AddressRef();
            shippingAddressRef.setReferenceId(addresses.get(0).getId());
            saleOrder.setShippingAddress(shippingAddressRef);

            AddressRef billingAddressRef = new AddressRef();
            billingAddressRef.setReferenceId(addresses.get(0).getId());
            saleOrder.setBillingAddress(billingAddressRef);
        }

        return saleOrderList;
    }

    public AddressDetail getAddressDetails(Address address,String id) {
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
            addressDetail.setPhone("9999999999");
        }

        addressDetail.setId(id);
        return addressDetail;

    }

    public Map<String,Pendency> fetchPendencyInternal(SearchShipmentResponseV3 searchShipmentResponse) {
        Map<String, Pendency> fsnToPendency = new HashMap<>();
        for (Shipment shipment: searchShipmentResponse.getShipments()) {
            for (OrderItem orderItem: shipment.getOrderItems()) {
                String fsn = orderItem.getFsn();
                if ( fsnToPendency.containsKey(fsn)) {
                    int quantity = fsnToPendency.get(fsn).getRequiredInventory();
                    int updatedQuantity = quantity + orderItem.getQuantity();
                    fsnToPendency.get(fsn).setRequiredInventory(updatedQuantity);
                } else {
                    Pendency pendency = new Pendency();
                    pendency.setChannelProductId(fsn);
                    pendency.setProductName(orderItem.getTitle());
                    pendency.setRequiredInventory(orderItem.getQuantity());
                    pendency.setSellerSkuCode(orderItem.getSku());
                }
            }
        }
        return fsnToPendency;
    }

    public UpdateInventoryV3Request prepareFlipkartInventoryUpdateRequest(UpdateInventoryRequest updateInventoryRequest) {

        UpdateInventoryV3Request updateInventoryV3Request = new UpdateInventoryV3Request();

        for (UpdateInventoryRequest.Listing listing : updateInventoryRequest.getListings()) {
            UpdateInventoryV3Request.SkuDetails skuDetails = new UpdateInventoryV3Request.SkuDetails();
            skuDetails.setProductId(listing.getChannelProductId());
            skuDetails.addLocation(new UpdateInventoryV3Request.Location(FlipkartRequestContext.current().getLocationId(),listing.getTotalCount()));
            updateInventoryV3Request.addSku(listing.getChannelSkuCode(), skuDetails);
        }
        return updateInventoryV3Request;
    }

    // prepare flipkart mark shipment RTD request
    public DispatchStandardShipmentV3Request closeShippingManifestInternal(CloseShippingManifestRequest closeShippingManifestRequest, Map<String, String> shippingPackageCodeToSaleOrderCode) {

        DispatchStandardShipmentV3Request dispatchStandardShipmentV3Request = null;
        for (CloseShippingManifestRequest.ShippingManifestItems shippingManifestItem : closeShippingManifestRequest.getShippingManifestItems()) {
            dispatchStandardShipmentV3Request.addShipmentId(shippingManifestItem.getSaleOrderCode());
            shippingPackageCodeToSaleOrderCode.put(shippingManifestItem.getShippingPackageCode(),shippingManifestItem.getSaleOrderCode());
        }
        dispatchStandardShipmentV3Request.setLocationId(FlipkartRequestContext.current().getLocationId());

        return dispatchStandardShipmentV3Request;
    }



    public  static void main(String[] args) {
        System.out.println(LocalDate.now());
    }
}

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
import com.uniware.integrations.client.dto.api.requestDto.DispatchSelfShipmentRequestV3;
import com.uniware.integrations.client.dto.api.requestDto.DispatchStandardShipmentV3Request;
import com.uniware.integrations.client.dto.api.requestDto.FetchOnHoldOrderRequest;
import com.uniware.integrations.client.dto.api.requestDto.GetManifestRequest;
import com.uniware.integrations.client.dto.api.requestDto.SearchShipmentRequest;
import com.uniware.integrations.client.dto.api.requestDto.UpdateInventoryV3Request;
import com.uniware.integrations.client.dto.api.responseDto.DispatchShipmentV3Response;
import com.uniware.integrations.client.dto.api.responseDto.InvoiceDetailsResponseV3;
import com.uniware.integrations.client.dto.api.responseDto.LocationDetailsResponse;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentDetailsWithSubPackages;
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
import com.uniware.integrations.client.dto.uniware.Error;
import com.uniware.integrations.client.dto.uniware.FetchCurrentChannelManifestRequest;
import com.uniware.integrations.client.dto.uniware.FetchOrderRequest;
import com.uniware.integrations.client.dto.api.requestDto.ShipmentPackV3Request;
import com.uniware.integrations.client.dto.api.responseDto.AuthTokenResponse;
import com.uniware.integrations.client.dto.api.responseDto.SearchShipmentResponse;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentDetailsWithAddressResponseV3;
import com.uniware.integrations.client.dto.uniware.AddressDetail;
import com.uniware.integrations.client.dto.uniware.AddressRef;
import com.uniware.integrations.client.dto.uniware.FetchOrderResponse;
import com.uniware.integrations.client.dto.uniware.FetchPendencyRequest;
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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
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
@FlipkartClient(version = "v1",channelSource = { ChannelSource.FLIPKART_DROPSHIP})
public class FlipkartDropshipServiceImpl extends AbstractSalesFlipkartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlipkartDropshipServiceImpl.class);

    @Autowired
    private Environment environment;
    public static final String BUCKET_NAME = "unicommerce-channel-shippinglabels";
    public static final String SUCCESS = "success";

    @Autowired
    private FlipkartSellerApiService flipkartSellerApiService;
    @Autowired
    private FlipkartSellerPanelService flipkartSellerPanelService;
    private S3Service s3Service;

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
            responseParam.put("authTokenExpiresIn", String.valueOf(authTokenExpireIn));
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
                    ResponseUtil.failure("Unable to fetch auth token");
                authToken = authTokenResponse.getAccessToken();
                refreshToken = authTokenResponse.getRefreshToken();
                authTokenExpiresIn = authTokenResponse.getExpiresIn();
            }

            LocationDetailsResponse locationDetailsResponse = flipkartSellerApiService.getAllLocations();
            if ( locationDetailsResponse == null )
                ResponseUtil.failure("Getting error while fetching location details");
            boolean isValidLocation = locationDetailsResponse.getLocations().stream().anyMatch(location -> locationId.equalsIgnoreCase(location.getLocationId()));

            if ( isValidLocation ) {
                responseParams.put("authToken",authToken);
                responseParams.put("refreshToken",refreshToken);
                responseParams.put("authTokenExpiresIn", String.valueOf(authTokenExpiresIn));
                responseParams.put("locationId",locationId);
                ResponseUtil.success("Connector verified successfully");
            }
            else {
                ResponseUtil.failure("Invalid locationId");
            }
        }

        return ResponseUtil.failure("Unable to verify connector");
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

    @Override public Response fetchCatalog(Map<String, String> headers, CatalogSyncRequest catalogSyncRequest) {

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

        if (rows != null) {
            catalogSyncResponse = fetchCatalogInternal(rows, catalogSyncRequest.getPageSize());
        } else {
            ResponseUtil.failure("Unable to parse listing report");
        }


        if ( catalogSyncResponse != null )
            ResponseUtil.success("Catalog fetched successfully", catalogSyncResponse);

        return null;
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
        if ( "PROCESSING".equalsIgnoreCase(stockFileDownloadRequestStatusResponse.getDownloadState())){
            LOGGER.info("File generation is in PROCESSING state. Will retry after sometime...");
            fillAsyncDetails(stockFileDownloadRequestStatusResponse, catalogPreProcessorResponse, catalogPreProcessorRequest.isAsync());
            ResponseUtil.success("File generation is in PROCESSING state. Wait for sometime...", catalogPreProcessorResponse);
        }
        else if ( "COMPLETED".equalsIgnoreCase(stockFileDownloadRequestStatusResponse.getDownloadState())) {
            catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.COMPLETE);
            LOGGER.info("Stock file generation completed.");
        }
        else {
            catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.FAILED);
            LOGGER.error("ALERT - CASE NOT HANDLED");
        }

        // Check if the stock file exist in StockFileDownloadNUploadHistory if yes then download the it otherwise generate a new one
        StockFileDownloadNUploadHistoryResponse stockFileDownloadNUploadHistoryResponse = flipkartSellerPanelService.getStockFileDownloadNUploadHistory();
        stockfilePath = getStockFile(stockFileDownloadNUploadHistoryResponse);

        if ( stockfilePath == null && !catalogPreProcessorRequest.isAsync()) {
            boolean isRequestStockFileSuccessful = flipkartSellerPanelService.requestStockFile();
            if (isRequestStockFileSuccessful) {
                stockFileDownloadRequestStatusResponse = flipkartSellerPanelService.getStockFileDownloadRequestStatus();
                fillAsyncDetails(stockFileDownloadRequestStatusResponse, catalogPreProcessorResponse, catalogPreProcessorRequest.isAsync());
                ResponseUtil.success("Requested a new stock file. Wait for sometime...", catalogPreProcessorResponse);
            } else {
                return ResponseUtil.failure("Getting Error while requesting report...");
            }
        }

        catalogPreProcessorResponse.setFilePath(stockfilePath);
        return ResponseUtil.success("File downloaded Successfully", catalogPreProcessorResponse);
    }

    @Override public Response fetchPendency(Map<String, String> headers, FetchPendencyRequest fetchPendencyRequest) {

        boolean loginSuccess = flipkartSellerPanelService.sellerPanelLogin(FlipkartRequestContext.current().getUserName(), FlipkartRequestContext.current().getPassword(), false);
        if (!loginSuccess) {
            return ResponseUtil.failure("Login Session could not be established");
        }

        Map<String, Pendency> channelProductIdToPendency =new HashMap<>();

        boolean isPendencyFetchedForApprovedShipments = getPendencyOfApprovedShipments(fetchPendencyRequest, channelProductIdToPendency);
        if ( !isPendencyFetchedForApprovedShipments) {
            ResponseUtil.failure("Unable to fetch pendency of Approved shipments");
        }

        boolean isPendencyFetchedForOnHoldsShipments = getPendencyOfOnHoldsOrders(fetchPendencyRequest, channelProductIdToPendency);
        if ( !isPendencyFetchedForOnHoldsShipments) {
            ResponseUtil.failure("Unable to fetch pendency of OnHold Orders");
        }

        return ResponseUtil.success("Pendency Fetched Successfully",channelProductIdToPendency.values());
    }

    private boolean getNormalShipments(String nextPageUrl, int orderWindow, FetchOrderResponse fetchOrderResponse) {

        SearchShipmentResponse searchShipmentResponse ;

        if ( nextPageUrl == null ) {

            DateFilter dateFilter = new DateFilter();
            dateFilter.from((new DateTime(DateUtils.addToDate(DateUtils.getCurrentDate(), Calendar.DATE, orderWindow))));
            dateFilter.to((new DateTime(DateUtils.getCurrentTime())));

            Filter filter = new Filter();
            filter.type(Filter.TypeEnum.PREDISPATCH);
            filter.dispatchAfterDate(dateFilter);
            filter.locationId(FlipkartRequestContext.current().getLocationId());
            filter.addStatesItem(Filter.StatesEnum.APPROVED);
            filter.addShipmentTypesItem(Filter.ShipmentTypesEnum.NORMAL);

            Sort sort = new Sort();
            sort.setField(Sort.FieldEnum.ORDERDATE);
            sort.setOrder(Sort.OrderEnum.DESC);

            Pagination pagination = new Pagination();
            pagination.pageSize(20);

            SearchShipmentRequest searchShipmentRequest = new SearchShipmentRequest();
            searchShipmentRequest.setFilter(filter);
            searchShipmentRequest.setPagination(pagination);
            searchShipmentRequest.setSort(sort);

            searchShipmentResponse = flipkartSellerApiService.searchPreDispatchShipmentPost(searchShipmentRequest);
        } else {
            searchShipmentResponse = flipkartSellerApiService.searchPreDispatchShipmentGet(nextPageUrl);
        }

        if (searchShipmentResponse != null ){
            List<SaleOrder> saleOrderList = createSaleOrder(searchShipmentResponse);
            fetchOrderResponse = fetchOrderResponse.addOrders(saleOrderList);
            if ( searchShipmentResponse.isHasMore()){
                fetchOrderResponse.getMetadata().put("nextPageUrlForNormalShipments",searchShipmentResponse.getNextPageUrl());
                fetchOrderResponse.getMetadata().put("hasMoreNormalShipments",true);
            } else{
                fetchOrderResponse.getMetadata().put("hasMoreNormalShipments",false);
            }
        }

        return true;
    }

    private boolean getSelfShipments(String nextPageUrl, int orderWindow, FetchOrderResponse fetchOrderResponse) {
        SearchShipmentResponse searchShipmentResponse;

        if ( nextPageUrl == null ) {

            DateFilter dateFilter = new DateFilter();
            dateFilter.from((new DateTime(DateUtils.addToDate(DateUtils.getCurrentDate(), Calendar.DATE, orderWindow))));
            dateFilter.to((new DateTime(DateUtils.getCurrentTime())));

            Filter filter = new Filter();
            filter.type(Filter.TypeEnum.PREDISPATCH);
            filter.dispatchAfterDate(dateFilter);
            filter.locationId(FlipkartRequestContext.current().getLocationId());
            filter.addStatesItem(Filter.StatesEnum.APPROVED);
            filter.addShipmentTypesItem(Filter.ShipmentTypesEnum.SELF);

            Sort sort = new Sort();
            sort.setField(Sort.FieldEnum.ORDERDATE);
            sort.setOrder(Sort.OrderEnum.DESC);

            Pagination pagination = new Pagination();
            pagination.pageSize(20);

            SearchShipmentRequest searchShipmentRequest = new SearchShipmentRequest();
            searchShipmentRequest.setFilter(filter);
            searchShipmentRequest.setPagination(pagination);
            searchShipmentRequest.setSort(sort);

            searchShipmentResponse = flipkartSellerApiService.searchPreDispatchShipmentPost(searchShipmentRequest);
        } else {
            searchShipmentResponse = flipkartSellerApiService.searchPreDispatchShipmentGet(nextPageUrl);
        }

        if (searchShipmentResponse != null ){
            List<SaleOrder> saleOrderList = createSaleOrder(searchShipmentResponse);
            fetchOrderResponse = fetchOrderResponse.addOrders(saleOrderList);
            if ( searchShipmentResponse.isHasMore()){
                fetchOrderResponse.getMetadata().put("nextPageUrlForSelfShipments",searchShipmentResponse.getNextPageUrl());
                fetchOrderResponse.getMetadata().put("hasMoreSelfShipments",true);
            } else{
                fetchOrderResponse.getMetadata().put("hasMoreSelfShipments",false);
            }

        }

        return true;
    }

    private boolean getExpressShipments(String nextPageUrl, int orderWindow, FetchOrderResponse fetchOrderResponse) {
        SearchShipmentResponse searchShipmentResponse;

        if ( nextPageUrl == null ) {

            DateFilter dateFilter = new DateFilter();
            dateFilter.from((new DateTime(DateUtils.addToDate(DateUtils.getCurrentDate(), Calendar.DATE, orderWindow))));
            dateFilter.to((new DateTime(DateUtils.getCurrentTime())));

            Filter filter = new Filter();
            filter.type(Filter.TypeEnum.PREDISPATCH);
            filter.dispatchAfterDate(dateFilter);
            filter.locationId(FlipkartRequestContext.current().getLocationId());
            filter.addStatesItem(Filter.StatesEnum.APPROVED);
            filter.addShipmentTypesItem(Filter.ShipmentTypesEnum.NORMAL);
            filter.addDispatchServiceTiersItem(Filter.DispatchServiceTiersEnum.EXPRESS);

            Sort sort = new Sort();
            sort.setField(Sort.FieldEnum.ORDERDATE);
            sort.setOrder(Sort.OrderEnum.DESC);

            Pagination pagination = new Pagination();
            pagination.pageSize(20);

            SearchShipmentRequest searchShipmentRequest = new SearchShipmentRequest();
            searchShipmentRequest.setFilter(filter);
            searchShipmentRequest.setPagination(pagination);
            searchShipmentRequest.setSort(sort);

            searchShipmentResponse = flipkartSellerApiService.searchPreDispatchShipmentPost(searchShipmentRequest);
        } else {
            searchShipmentResponse = flipkartSellerApiService.searchPreDispatchShipmentGet(nextPageUrl);
        }

        if (searchShipmentResponse != null ){
            List<SaleOrder> saleOrderList = createSaleOrder(searchShipmentResponse);
            fetchOrderResponse = fetchOrderResponse.addOrders(saleOrderList);
            if ( searchShipmentResponse.isHasMore()){
                fetchOrderResponse.getMetadata().put("nextPageUrlForExpressShipments",searchShipmentResponse.getNextPageUrl());
                fetchOrderResponse.getMetadata().put("hasMoreExpressShipments",true);
            } else{
                fetchOrderResponse.getMetadata().put("hasMoreExpressShipments",false);
            }
        }

        return true;
    }

    // todo add dimensions map in additional info
    @Override  public Response fetchOrders(Map<String, String> headers, FetchOrderRequest orderSyncRequest) {

        FetchOrderResponse fetchOrderResponse = new FetchOrderResponse();

        if (orderSyncRequest.getMetdata().get("hasMoreNormalShipments") == null || (boolean) orderSyncRequest.getMetdata().get("hasMoreNormalShipments")) {
            getNormalShipments((String) orderSyncRequest.getMetdata().get("nextPageUrlForNormalShipments"), orderSyncRequest.getOrderWindow(), fetchOrderResponse);
        }
        if (orderSyncRequest.getMetdata().get("hasMoreSelfShipments") == null || (boolean) orderSyncRequest.getMetdata().get("hasMoreSelfShipments")) {
            getSelfShipments((String) orderSyncRequest.getMetdata().get("nextPageUrlForSelfShipments"), orderSyncRequest.getOrderWindow(), fetchOrderResponse);
        }
        if (orderSyncRequest.getMetdata().get("hasMoreExpressShipments") == null || (boolean) orderSyncRequest.getMetdata().get("hasMoreExpressShipments")) {
            getExpressShipments((String) orderSyncRequest.getMetdata().get("nextPageUrlForExpressShipments"), orderSyncRequest.getOrderWindow(), fetchOrderResponse);
        }

        return ResponseUtil.success("ORDERS LIST FETCHED SUCCESSFULLY!", fetchOrderResponse);

    }

    @Override public Response generateInvoice(Map<String, String> headers, GenerateInvoiceRequest generateInvoiceRequest) {

        CreateInvoiceResponse createInvoiceResponse = new CreateInvoiceResponse();

        ShippingPackage shippingPackage = generateInvoiceRequest.getShippingPackage();
        ShipmentDetailsWithSubPackages shipmentDetails = flipkartSellerApiService.getShipmentDetailsWithSubPackages(shippingPackage.getSaleOrder().getCode());
        if (shipmentDetails.getShipments() == null) {
            ResponseUtil.failure("Unable to fetch shipment details");
        }

        List<OrderItem> orderItems = shipmentDetails.getShipments().get(0).getOrderItems();
        boolean isLabelGenerated = orderItems.stream().noneMatch(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("approved"));
        boolean isShipmentReadyToShip = orderItems.stream().anyMatch(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("PACKED"));
        boolean packingInProgress = orderItems.stream().anyMatch(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("PACKING_IN_PROGRESS"));
        orderItems.forEach(orderItem -> LOGGER.info("Shipment:{} order item id:{}, listingId:{}, status :{}, quantity:{} ", shippingPackage.getSaleOrder().getCode(), orderItem.getOrderItemId(),orderItem.getListingId(), orderItem.getStatus(), orderItem.getQuantity()));

        if(packingInProgress){
            ResponseUtil.failure("Packing in process. Kinldy retry after sometime");
        }

        if(!isLabelGenerated){
            boolean isPackConfirmed = packShipment(shippingPackage);
            if(isPackConfirmed){
                isShipmentReadyToShip = true;
                isLabelGenerated = true;
            }else{
                ResponseUtil.failure("Unable to pack shipment");
            }
        }

        Map<String,String> channelSaleOrderItemCodeToChannelProductId = new HashMap<>();
        shippingPackage.getSaleOrderItems().forEach(entry -> channelSaleOrderItemCodeToChannelProductId.put(entry.getChannelSaleOrderItemCode(),entry.getChannelProductId()));

        InvoiceDetailsResponseV3 invoiceDetailsResponseV3 = flipkartSellerApiService.getInvoicesInfo(shippingPackage.getSaleOrder().getCode());
        if ( invoiceDetailsResponseV3 != null ){
            if (StringUtils.isNotBlank(shippingPackage.getInvoiceCode()))
                createInvoiceResponse.setInvoiceCode(shippingPackage.getInvoiceCode());
            else
                createInvoiceResponse.setInvoiceCode(invoiceDetailsResponseV3.getInvoices().get(0).getInvoiceNumber());
            createInvoiceResponse.setDisplayCode(invoiceDetailsResponseV3.getInvoices().get(0).getInvoiceNumber());
            createInvoiceResponse.setChannelCreatedTime(invoiceDetailsResponseV3.getInvoices().get(0).getInvoiceDate().toDate());
            CreateInvoiceResponse.TaxInformation taxInformation = new CreateInvoiceResponse.TaxInformation();
            for ( Invoice.OrderItem orderItem : invoiceDetailsResponseV3.getInvoices().get(0).getOrderItems()) {
                CreateInvoiceResponse.ProductTax productTax = new CreateInvoiceResponse.ProductTax();
                productTax.setChannelProductId(channelSaleOrderItemCodeToChannelProductId.get(orderItem.getOrderItemId().replaceAll("\n","")));
                productTax.setCentralGst(orderItem.getTaxDetails().getCgstRate());
                productTax.setStateGst(orderItem.getTaxDetails().getSgstRate());
                productTax.setIntegratedGst(orderItem.getTaxDetails().getIgstRate());
                productTax.setCompensationCess(orderItem.getTaxDetails().getCessRate());
                productTax.setUnionTerritoryGst(orderItem.getTaxDetails().getUtgstRate());
                taxInformation.addProductTax(productTax);
            }

            String filePath = "";
            boolean isInvoiceLabelDownloaded = flipkartSellerApiService.downloadInvoiceAndLabel(shippingPackage.getSaleOrder().getCode(),filePath);

            String labelSize   = headers.get("labelSize");
            String invoiceSize = headers.get("InvoiceSize");
            String invoiceOutFilePath = "/tmp/" +
                    TenantRequestContext.current().getHttpSenderIdentifier()  + "-" + UUID.randomUUID()+ ".pdf";
            String labelOutFilePath = "/tmp/" +
                    TenantRequestContext.current().getHttpSenderIdentifier() + "-" + UUID.randomUUID() + ".pdf";
            List<String> paths = new ArrayList<>();
            paths.add(filePath);

            if(StringUtils.isNotBlank(labelSize) && !labelSize.equals("A4_FK_Label+Invoice")){
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
                createInvoiceResponse.getShippingProviderInfo().setShippingLabelLink(labelS3URL);
            }else{
                String labelS3URL = s3Service.uploadFile(new File(filePath),BUCKET_NAME);
                createInvoiceResponse.getShippingProviderInfo().setShippingLabelLink(labelS3URL);
            }

            if(StringUtils.isNotBlank(invoiceSize) && !invoiceSize.equals("Default_UC_Invoice")){
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
                createInvoiceResponse.setInvoiceUrl(invoiceS3URL);
            }

        }

        return ResponseUtil.failure("Unable to generate invoice");
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
            DispatchSelfShipmentRequestV3 dispatchSelfShipmentRequestV3 = prepareDispatchSelfShipmentRequest(closeShippingManifestRequest, saleOrderCodeToShippingPackageCode);
            dispatchShipmentV3Response = flipkartSellerApiService.markSelfShipDispatch(dispatchSelfShipmentRequestV3);
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
            return ResponseUtil.success("Manifest Closed Successfully.",closeShippingManifestResponse);
        } else {
            return ResponseUtil.failure("Unable to close Manifest");
        }
    }

    @Override public Response fetchCurrentChannelManifest(Map<String,String> headers, FetchCurrentChannelManifestRequest fetchCurrentChannelManifestRequest) {

        GetManifestRequest getManifestRequest = new GetManifestRequest.Builder()
                .setParams(new GetManifestRequest.Params.Builder()
                        .setVendorGroupCode(getVendorGroupCode(fetchCurrentChannelManifestRequest.getShippingProviderCode()))
                        .setIsMps(false)
                        .setLocationId(FlipkartRequestContext.current().getLocationId())
                        .build())
                .build();

        String manifestFilePath = com.uniware.integrations.utils.StringUtils.join('_', TenantRequestContext.current().getTenantCode(), EncryptionUtils.md5Encode(String.valueOf(System.currentTimeMillis())), UUID.randomUUID().toString(), fetchCurrentChannelManifestRequest.getShippingManifestCode()) + ".pdf";

        boolean isManifestDownloaded = flipkartSellerApiService.getCurrentChannelManifest(getManifestRequest, manifestFilePath);

        if (isManifestDownloaded) {
            Map<String,String> manifestDetails = new HashMap<>();
            manifestDetails.put("filePath", manifestFilePath);
            ResponseUtil.success("Manifest downloaded successfully", manifestDetails);
        }

        return ResponseUtil.failure("Unable to download Manifest");
    }

    @Override public Response updateInventory(Map<String, String> headers, UpdateInventoryRequest updateInventoryRequest) {

        UpdateInventoryV3Request updateInventoryV3Request = prepareFlipkartInventoryUpdateRequest(updateInventoryRequest);
        UpdateInventoryV3Response updateInventoryV3Response = flipkartSellerApiService.updateInventory(updateInventoryV3Request);
        UpdateInventoryResponse updateInventoryResponse = new UpdateInventoryResponse();
        if ( updateInventoryV3Response.getResponseStatus().is2xxSuccessful()) {
            BeanUtils.copyProperties(updateInventoryRequest, updateInventoryResponse);
            for (UpdateInventoryResponse.Listing listing : updateInventoryResponse.getListings()) {
                UpdateInventoryV3Response.InventoryUpdateStatus listingInventoryUpdateStatus = updateInventoryV3Response.getSkus()
                        .get("channelSkuCode");
                if (listingInventoryUpdateStatus == null) {
                    listing.setStatus(UpdateInventoryResponse.Status.FAILED);
                    // TODD error code
                    listing.addError(new Error("", "unable to update inventory"));
                }
                else if ("FAILURE".equalsIgnoreCase(listingInventoryUpdateStatus.getStatus())) {
                    List<Error> errors = new ArrayList<>();
                    List<Error> attributeErrors = new ArrayList<>();
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
            List<Error> errors = new ArrayList<>();
            BeanUtils.copyProperties(updateInventoryV3Response.getErrors(), errors);
            updateInventoryResponse.setStatus(UpdateInventoryResponse.Status.FAILED);
            updateInventoryResponse.setErrors(errors);
            return ResponseUtil.success("SUCCESS",updateInventoryResponse);
        }

        return ResponseUtil.failure("Unable to update inventory");
    }

    private boolean packShipment(ShippingPackage shippingPackage) {

        ShipmentPackV3Request shipmentPackV3Request = preparePackShipmentRequest(shippingPackage);
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
            ShipmentDetailsWithSubPackages shipmentDetails = flipkartSellerApiService.getShipmentDetailsWithSubPackages(shipmentId);
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

    private ShipmentPackV3Request preparePackShipmentRequest(ShippingPackage shippingPackage) {

        PackRequest packRequest = new PackRequest();

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
                .dimensions(getPackDimension(shippingPackage));
        Invoice invoice = new Invoice()
                .orderId(shippingPackage.getSaleOrder()
                .getDisplayOrderCode())
                .invoiceNumber(shippingPackage.getInvoiceCode())
                .invoiceDate(LocalDate.now());


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

    private CatalogSyncResponse fetchCatalogInternal(Iterator<Row> rows, int pageSize) {

        CatalogSyncResponse catalogSyncResponse = new CatalogSyncResponse();
        while ( rows.hasNext() && pageSize-- > 0) {
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

                if ("FLIPKART".equalsIgnoreCase(TenantRequestContext.current().getSourceCode())){
                    channelItemType.addAttribute(new ChannelItemType.Attribute.Builder()
                            .setName("FSN")
                            .setValue(row.getColumnValue("Flipkart Serial Number"))
                            .build());

                    catalogSyncResponse.addChannelItemType(channelItemType);
                }
            }
        }
        if ( rows.hasNext())
            catalogSyncResponse.setHasMore(true);

        return catalogSyncResponse;
    }

    private List<String> getFullfimentByListSourceCode(String sourceCode) {
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
    private boolean validateRow(Row row) {

        if ( StringUtils.isNotBlank(row.getColumnValue("Inactive Reason"))){
            LOGGER.info("Skipped row {}, due to {}",row,row.getColumnValue("Inactive Reason"));
            return false;
        }   else if ( row.getColumnValue("Listing Archival") != null
                || ("ARCHIVED").equalsIgnoreCase(row.getColumnValue("Listing Archival"))) {
            LOGGER.info("Skipped row {}, as either its  Listing Archival is null or has value ARCHIVED",row);
            return false;
        }   else if ( row.getColumnValue("Fulfillment By") != null
                || getFullfimentByListSourceCode(TenantRequestContext.current().getSourceCode()).contains(row.getColumnValue("Fulfillment By"))) {
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
            CatalogPreProcessorResponse.AsyncInstruction asyncInstruction = new CatalogPreProcessorResponse.AsyncInstruction();
            asyncInstruction.setNextRunInMs(1000*(stockFileDownloadRequestStatusResponse.getTotalCount() - stockFileDownloadRequestStatusResponse.getProcessed_count())/50);
            asyncInstruction.setRetryCount(2);
            catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.PROCESSING);
            catalogPreProcessorResponse.setAsync(asyncInstruction);
        }

    }

    private String getChannelProductIdBySourceCode(Row row, String sourceCode) {
        if ( "FLIPKART_FA".equalsIgnoreCase(sourceCode))
            return row.getColumnValue("Flipkart Serial Number");

        return row.getColumnValue("Listing ID");
    }

    private SearchShipmentRequest prepareFetchPendencyRequestForApprovedShipments() {

        Filter filter = new Filter();
        filter.addStatesItem(Filter.StatesEnum.APPROVED);
        filter.locationId(FlipkartRequestContext.current().getLocationId());

        DateFilter dateFilter = new DateFilter();
        dateFilter.from((new DateTime(DateUtils.getCurrentTime())));
        filter.dispatchAfterDate(dateFilter);

        Sort sort = new Sort();
        sort.setField(Sort.FieldEnum.DISPATCHBYDATE);
        sort.setOrder(Sort.OrderEnum.DESC);

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

    private boolean getPendencyOfOnHoldsOrders(FetchPendencyRequest fetchPendencyRequest, Map<String,Pendency> channelProductIdToPendency) {

        int pageNumber = 0;
        boolean hasMore = false;

        do {
            FetchOnHoldOrderRequest fetchOnHoldOrderRequest = prepareFetchOnHoldOrderRequest(pageNumber);
            String fetchPendencyResponse = flipkartSellerPanelService.getOnHoldOrdersFromPanel(fetchOnHoldOrderRequest);
            if ( fetchPendencyResponse != null ) {
                try {
                    JsonObject jsonObject = new Gson().fromJson(fetchPendencyResponse, JsonObject.class);
                    hasMore = jsonObject.get("has_more").getAsBoolean();
                    JsonArray orders = jsonObject.getAsJsonArray("items");
                    while (orders.iterator().hasNext()) {
                        JsonObject order = (JsonObject) orders.iterator().next();
                        List<JsonElement> orderItems = (List<JsonElement>) order.get("orderitems");
                        for (JsonElement orderItem: orderItems ) {
                            String listingId = orderItem.getAsJsonObject().get("listing_id").getAsString();
                            if (channelProductIdToPendency.computeIfPresent(listingId, (key, val) -> val.addRequiredInventory(orderItem.getAsJsonObject().get("quantity").getAsInt())) == null) {
                                Pendency pendency = new Pendency();
                                pendency.setChannelProductId(listingId);
                                pendency.setProductName(orderItem.getAsJsonObject().get("title").getAsString());
                                pendency.setRequiredInventory(orderItem.getAsJsonObject().get("quantity").getAsInt());
                                pendency.setSellerSkuCode(orderItem.getAsJsonObject().get("sku").getAsString());
                            }
                        }
                    }
                } catch (JsonSyntaxException e) {
                    LOGGER.error("Unable to parse response", fetchPendencyResponse);
                    return false;
                }
            }
            pageNumber++;
        } while (hasMore);

        return true;
    }

    private boolean getPendencyOfApprovedShipments(FetchPendencyRequest fetchPendencyRequest, Map<String, Pendency> channelProductIdToPendency) {

        SearchShipmentResponse searchShipmentResponse = null;

        boolean postApiCall = true;
        SearchShipmentRequest searchShipmentRequest = prepareFetchPendencyRequestForApprovedShipments();

        do {
            if (postApiCall) {
                searchShipmentResponse = flipkartSellerApiService.searchPreDispatchShipmentPost(searchShipmentRequest);
            }
            else {
                searchShipmentResponse = flipkartSellerApiService.searchPreDispatchShipmentGet(searchShipmentResponse.getNextPageUrl());
            }
            if ( searchShipmentResponse != null ) {
                for (Shipment shipment : searchShipmentResponse.getShipments()) {
                    for (OrderItem orderItem : shipment.getOrderItems()) {
                        String listingId = orderItem.getListingId();
                        if (channelProductIdToPendency.computeIfPresent(listingId, (key, val) -> val.addRequiredInventory(orderItem.getQuantity())) == null) {
                            Pendency pendency = new Pendency();
                            pendency.setChannelProductId(listingId);
                            pendency.setProductName(orderItem.getTitle());
                            pendency.setRequiredInventory(orderItem.getQuantity());
                            pendency.setSellerSkuCode(orderItem.getSku());
                        }
                    }
                }
            } else {
                LOGGER.error("Getting null response from Api");
                return false;
            }
            postApiCall = false;
        } while (searchShipmentResponse.isHasMore());

        return true;
    }

    private List<SaleOrder> createSaleOrder(SearchShipmentResponse searchShipmentResponse) {

        List<SaleOrder> saleOrderList = new ArrayList<>();
        List<String> shipmentIds = searchShipmentResponse.getShipments().stream().map(Shipment::getShipmentId).collect(Collectors.toList());
        String commaSepratedShipmentIds = StringUtils.join(shipmentIds);
        ShipmentDetailsWithAddressResponseV3 shipmentDetailsSearchResponse = flipkartSellerApiService.getShipmentDetailsWithAddress(commaSepratedShipmentIds);

        Map<String,SaleOrder> shipmentIdToSaleOrder = new HashMap<>();
        for (Shipment shipment: searchShipmentResponse.getShipments()) {
            SaleOrder saleOrder = new SaleOrder();
            saleOrder.setCode(shipment.getShipmentId());
            saleOrder.setDisplayOrderCode(shipment.getOrderItems().get(0).getOrderId());
            saleOrder.setDisplayOrderDateTime(shipment.getOrderItems().get(0).getOrderDate().toDate());
            saleOrder.setFulfillmentTat(shipment.getDispatchByDate().toDate());
            saleOrder.setChannel(ChannelSource.FLIPKART_DROPSHIP.getChannelSourceCode());

            saleOrder.setCashOnDelivery("COD".equalsIgnoreCase(shipment.getOrderItems().get(0).getPaymentType().name()));

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
            addressDetail.setPhone("9999999999");
        }

        addressDetail.setId(id);
        return addressDetail;

    }

    private UpdateInventoryV3Request prepareFlipkartInventoryUpdateRequest(UpdateInventoryRequest updateInventoryRequest) {

        UpdateInventoryV3Request updateInventoryV3Request = new UpdateInventoryV3Request();

        for (UpdateInventoryRequest.Listing listing : updateInventoryRequest.getListings()) {
            UpdateInventoryV3Request.SkuDetails skuDetails = new UpdateInventoryV3Request.SkuDetails();
            skuDetails.setProductId(listing.getChannelProductId());
            skuDetails.addLocation(new UpdateInventoryV3Request.Location(FlipkartRequestContext.current().getLocationId(),listing.getTotalCount()));
            updateInventoryV3Request.addSku(listing.getChannelSkuCode(), skuDetails);
        }
        return updateInventoryV3Request;
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

    private DispatchSelfShipmentRequestV3 prepareDispatchSelfShipmentRequest(CloseShippingManifestRequest closeShippingManifestRequest, Map<String, String> shippingPackageCodeToSaleOrderCode) {
        DispatchSelfShipmentRequestV3 dispatchSelfShipmentRequestV3 = new DispatchSelfShipmentRequestV3();
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
            dispatchSelfShipmentRequestV3.addShipmentsItem(shipment);
        }
        return dispatchSelfShipmentRequestV3;
    }

    private String getVendorGroupCode(String shippingProviderCode) {
        String vendorGroupCode = shippingProviderCode;
        if ( shippingProviderCode.contains("Delhivery") )
            vendorGroupCode = "Delhivery";
        else if ( shippingProviderCode.contains("E-Kart Logistics"))
            vendorGroupCode = "Ekart Logistics";
        else if ( shippingProviderCode.contains("Ecom"))
            vendorGroupCode = "Ekart Logistics";

        return vendorGroupCode;
    }

}

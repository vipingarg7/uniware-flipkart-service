package com.uniware.integrations.client.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.unifier.core.fileparser.Row;
import com.unifier.core.utils.DateUtils;
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
import com.uniware.integrations.client.dto.PackShipmentStatus;
import com.uniware.integrations.client.dto.Pagination;
import com.uniware.integrations.client.dto.PriceComponent;
import com.uniware.integrations.client.dto.Shipment;
import com.uniware.integrations.client.dto.ShipmentDetails;
import com.uniware.integrations.client.dto.Sort;
import com.uniware.integrations.client.dto.SubShipments;
import com.uniware.integrations.client.dto.TaxItem;
import com.uniware.integrations.client.dto.api.requestDto.DispatchSelfShipmentV3Request;
import com.uniware.integrations.client.dto.api.requestDto.DispatchStandardShipmentV3Request;
import com.uniware.integrations.client.dto.api.requestDto.EnqueDownloadRequest;
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
import com.uniware.integrations.client.dto.uniware.GenerateLabelRequest;
import com.uniware.integrations.client.dto.uniware.ShippingProviderInfo;
import com.uniware.integrations.client.utils.FKDelimitedFileParser;
import com.uniware.integrations.client.utils.FKExcelSheetParser;
import com.uniware.integrations.locking.ILockingService;
import com.uniware.integrations.locking.Level;
import com.uniware.integrations.locking.Namespace;
import com.uniware.integrations.uniware.catalog.request.dto.CatalogPreProcessorRequest;
import com.uniware.integrations.uniware.catalog.response.dto.CatalogPreProcessorResponse;
import com.uniware.integrations.uniware.catalog.request.dto.CatalogSyncRequest;
import com.uniware.integrations.uniware.catalog.response.dto.CatalogSyncResponse;
import com.uniware.integrations.uniware.catalog.response.dto.ChannelItemType;
import com.uniware.integrations.client.dto.uniware.CloseShippingManifestRequest;
import com.uniware.integrations.uniware.authentication.connector.request.dto.ConnectorVerificationRequest;
import com.uniware.integrations.uniware.authentication.connector.response.dto.ConnectorVerificationResponse;
import com.uniware.integrations.client.dto.uniware.CreateInvoiceResponse;
import com.uniware.integrations.uniware.Dispatch.response.dto.DispatchShipmentRequest;
import com.uniware.integrations.uniware.Dispatch.request.dto.DispatchShipmentResponse;
import com.uniware.integrations.uniware.dto.rest.api.Error;
import com.uniware.integrations.uniware.manifest.currentChannel.request.dto.CurrentChannelManifestRequest;
import com.uniware.integrations.uniware.manifest.currentChannel.response.dto.CurrentChannelManifestResponse;
import com.uniware.integrations.uniware.order.request.dto.FetchOrderRequest;
import com.uniware.integrations.client.dto.api.requestDto.ShipmentPackV3Request;
import com.uniware.integrations.client.dto.api.responseDto.AuthTokenResponse;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentDetailsV3WithAddressResponse;
import com.uniware.integrations.uniware.dto.rest.api.AddressDetail;
import com.uniware.integrations.uniware.dto.rest.api.AddressRef;
import com.uniware.integrations.uniware.order.response.dto.FetchOrderResponse;
import com.uniware.integrations.uniware.pendency.response.dto.FetchPendencyResponse;
import com.uniware.integrations.client.dto.uniware.GenerateInvoiceRequest;
import com.uniware.integrations.uniware.pendency.response.dto.Pendency;
import com.uniware.integrations.uniware.authentication.postConfig.request.dto.PostConfigurationRequest;
import com.uniware.integrations.uniware.authentication.postConfig.response.dto.PostConfigurationResponse;
import com.uniware.integrations.uniware.authentication.preConfig.request.dto.PreConfigurationRequest;
import com.uniware.integrations.uniware.dto.rest.api.SaleOrder;
import com.uniware.integrations.uniware.dto.rest.api.SaleOrderItem;
import com.uniware.integrations.uniware.dto.rest.api.ShippingPackage;
import com.uniware.integrations.uniware.inventory.request.dto.UpdateInventoryRequest;
import com.uniware.integrations.uniware.inventory.response.dto.UpdateInventoryResponse;
import com.uniware.integrations.client.service.FlipkartSellerApiService;
import com.uniware.integrations.client.service.FlipkartSellerPanelService;
import com.uniware.integrations.core.dto.api.Response;
import com.uniware.integrations.uniware.authentication.preConfig.response.dto.PreConfigurationResponse;
import com.uniware.integrations.utils.ResponseUtil;
import com.uniware.integrations.web.context.TenantRequestContext;
import com.uniware.integrations.web.exception.BadRequest;
import com.uniware.integrations.web.exception.FailureResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
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
    public static final String AUTH_TOKEN = "authToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String LOCATION_ID = "locationId";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FLIPKART_SELLER_PANEL = "FLIPKART_SELLER_PANEL";
    public static final String FLIPKART_INVENTORY_PANEL = "FLIPKART_INVENTORY_PANEL";
    public static final String DOWNLOADING = "DOWNLOADING";
    public static final String COMPLETED = "COMPLETED";
    public static final String FAILED = "FAILED";
    @Autowired
    private Environment environment;
    @Autowired
    private FlipkartSellerApiService flipkartSellerApiService;
    @Autowired
    private FlipkartSellerPanelService flipkartSellerPanelService;

    @Autowired
    private ILockingService lockingService;
    private static final Logger LOGGER = LoggerFactory.getLogger(FlipkartDropshipServiceImpl.class);
    @Autowired
    private S3Service s3Service;
    private static final String BUCKET_NAME = "unicommerce-channel-shippinglabels";
    private static final String SUCCESS = "success";
    private static final String CODE = "code";
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS+05:30";
    private static final String DEFAULT_PHONE_NUMBER = "9999999999";
    private static final String UNIWARE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ssXXX";
    private static final String FK_CODE        =  "fk_code";
    private static final Map<String,List<String>> sourceCodeToFulfilmentModeList = new HashMap();
    private static final Map<String,List<String>> sourceCodeToFulfimentProfileList = new HashMap();
    private static final List<String> disabledInventoryErrorMessageList = new ArrayList<>();

    public enum FulfilmentProfile {
        FBF,
        NON_FBF,
        FBF_LITE,
        FBF_AND_NON_FBF,
        FBF_AND_FBF_LITE
    }

    static  {
        for ( ChannelSource channelSource: ChannelSource.values() ) {

            if ( StringUtils.equalsIngoreCaseAny(channelSource.getChannelSourceCode(),"FLIPKART","2GUD","FLIPKART_OMNI")){
                sourceCodeToFulfilmentModeList.put(channelSource.getChannelSourceCode(),Arrays.asList("seller","Flipkart and Seller"));
                sourceCodeToFulfimentProfileList.put(channelSource.getChannelSourceCode(), Arrays.asList(FulfilmentProfile.NON_FBF.name(),FulfilmentProfile.FBF_AND_NON_FBF.name()));
            }
            else if ( StringUtils.equalsIngoreCaseAny(channelSource.getChannelSourceCode(),"FLIPKART_LITE","FLIPKART_SMART")) {
                sourceCodeToFulfilmentModeList.put(channelSource.getChannelSourceCode(),Arrays.asList("SellerSmart","Seller Smart","Flipkart and Seller Smart"));
                sourceCodeToFulfimentProfileList.put(channelSource.getChannelSourceCode(), Arrays.asList(FulfilmentProfile.FBF_AND_FBF_LITE.name(),FulfilmentProfile.FBF_LITE.name()));
            }
            else if ( StringUtils.equalsIngoreCaseAny(channelSource.getChannelSourceCode(),"FLIPKART_FA") ) {
                sourceCodeToFulfilmentModeList.put(channelSource.getChannelSourceCode(),Arrays.asList("Flipkart","Flipkart and Seller Smart","Flipkart and Seller"));
                sourceCodeToFulfimentProfileList.put(channelSource.getChannelSourceCode(), Arrays.asList(FulfilmentProfile.FBF_AND_NON_FBF.name(),FulfilmentProfile.FBF.name(),FulfilmentProfile.FBF_AND_FBF_LITE.name()));
            }
        }

        disabledInventoryErrorMessageList.add("Invalid location provided");

    }

    @Override public Response preConfiguration(Map<String, String> headers, PreConfigurationRequest preConfigurationRequest) {

        String state = preConfigurationRequest.getParams() != null ? preConfigurationRequest.getParams().get("state") : null;
        if ( StringUtils.isBlank(state )) {
            throw new BadRequest("State is missing in request payload");
        }

        state = state + "?locationId=" + FlipkartRequestContext.current().getLocationId();

        PreConfigurationResponse preConfigurationResponse = new PreConfigurationResponse();
        HashMap<String, String> params = new HashMap<>();
        params.put("client_id", environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_APPLICATION_ID));
        params.put("response_type", CODE);
        params.put("scope", environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_SCOPE));
        params.put("state", state);
        params.put("redirect_uri", environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_REDIRECT_URL));
        preConfigurationResponse.setUrl(flipkartSellerApiService.getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource()) + "/oauth-service/oauth/authorize");
        preConfigurationResponse.setMethod("Get");
        preConfigurationResponse.setParams(params);
        return ResponseUtil.success(SUCCESS, preConfigurationResponse);
    }

    @Override public Response postConfiguration(Map<String, String> headers, PostConfigurationRequest postConfigurationRequest) {

        Map<String,String> params = postConfigurationRequest.getParams();
        AuthTokenResponse authTokenResponse = flipkartSellerApiService.getAuthToken(params.get(FK_CODE));

        if ( authTokenResponse ==null || authTokenResponse.getAccessToken() == null ) {
            return ResponseUtil.failure("Unable to generate AuthToken");
        }

        String authToken = authTokenResponse.getTokenType() + " " + authTokenResponse.getAccessToken();
        Long authTokenExpireIn = authTokenResponse.getExpiresIn();
        authTokenExpireIn = (authTokenExpireIn * 1000) + DateUtils.getCurrentTime().getTime();
        String refreshToken = authTokenResponse.getRefreshToken();

        HashMap<String, String> responseParams = new HashMap<>();
        responseParams.put(AUTH_TOKEN, authToken);
        responseParams.put(REFRESH_TOKEN, refreshToken);
        responseParams.put(AUTH_TOKEN_EXPIRES_IN, String.valueOf(authTokenExpireIn));

        String locationId = params.get(LOCATION_ID);
        FlipkartRequestContext.current().setAuthToken(authToken);
        LocationDetailsResponse locationDetailsResponse = flipkartSellerApiService.getAllLocations();
        if ( locationDetailsResponse == null )
            return ResponseUtil.failure("Getting error while fetching location details");
        boolean isValidLocation = locationDetailsResponse.getLocations().stream().anyMatch(location -> locationId.equalsIgnoreCase(location.getLocationId()));
        if ( isValidLocation ) {
            responseParams.put(LOCATION_ID, locationId);
        } else {
            return ResponseUtil.failure("Invalid locationId");
        }

        PostConfigurationResponse postConfigurationResponse = new PostConfigurationResponse();
        postConfigurationResponse.setParams(responseParams);
        return ResponseUtil.success(SUCCESS, postConfigurationResponse);

    }

    @Override public Response connectorVerification(Map<String, String> headers, ConnectorVerificationRequest connectorVerificationRequest) {

        ConnectorVerificationResponse connectorVerificationResponse = new ConnectorVerificationResponse();
        Map<String,String> connectorParameters = connectorVerificationRequest.getConnectorParameters();
        Map<String,String> responseParams = new HashMap<>();

        if (FLIPKART_SELLER_PANEL.equalsIgnoreCase(connectorVerificationRequest.getConnectorName())){
            String username = connectorParameters.get(USERNAME);
            String password = connectorParameters.get(PASSWORD);
            boolean loginSuccess = flipkartSellerPanelService.sellerPanelLogin(username, password, false);
            if (!loginSuccess) {
                return ResponseUtil.failure("Unable to login on Flipkart panel.");
            }
            if ( connectorVerificationRequest.getUserTriggeredVerification()) {
                String sellerId = flipkartSellerPanelService.getFeaturesForSeller().getFirst();
                responseParams.put(USERNAME, username);
                responseParams.put(PASSWORD, password);
                responseParams.put("sellerId", sellerId);
            }
        }
        else if (FLIPKART_INVENTORY_PANEL.equalsIgnoreCase(connectorVerificationRequest.getConnectorName())) {
            String authToken = connectorParameters.get(AUTH_TOKEN);
            String refreshToken = connectorParameters.get(REFRESH_TOKEN);
            Long authTokenExpiresIn = Long.valueOf(connectorParameters.get(AUTH_TOKEN_EXPIRES_IN));
            String locationId = connectorParameters.get(LOCATION_ID);

            boolean isAuthTokenExpiryNear = isAuthTokenExpiryNear(authTokenExpiresIn);
            if ( isAuthTokenExpiryNear ) {
                AuthTokenResponse authTokenResponse = flipkartSellerApiService.refreshAuthToken(refreshToken);
                if ( authTokenResponse == null)
                    return ResponseUtil.failure("Unable to fetch auth token");
                authToken = authTokenResponse.getAccessToken();
                refreshToken = authTokenResponse.getRefreshToken();
                authTokenExpiresIn = authTokenResponse.getExpiresIn();
                authTokenExpiresIn = (authTokenExpiresIn * 1000) + DateUtils.getCurrentTime().getTime();
                FlipkartRequestContext.current().setAuthToken(authToken);
                responseParams.put(AUTH_TOKEN,authToken);
                responseParams.put(REFRESH_TOKEN,refreshToken);
                responseParams.put(AUTH_TOKEN_EXPIRES_IN, String.valueOf(authTokenExpiresIn));
            }

            LocationDetailsResponse locationDetailsResponse = flipkartSellerApiService.getAllLocations();
            if ( locationDetailsResponse == null )
                return ResponseUtil.failure("Getting error while fetching location details");
            boolean isValidLocation = locationDetailsResponse.getLocations().stream().anyMatch(location -> locationId.equalsIgnoreCase(location.getLocationId()));
            if ( !isValidLocation ) {
                return ResponseUtil.failure("Invalid locationId. Kindly check");
            }

        }
        connectorVerificationResponse.setConnectorParamters(responseParams);
        return ResponseUtil.success("Connector verified successfully ", connectorVerificationResponse);
    }

    @Override public Response fetchCatalog(Map<String, String> headers, CatalogSyncRequest catalogSyncRequest) {

        CatalogSyncResponse catalogSyncResponse;

        String stockFilePath = catalogSyncRequest.getStockFilePath();
        if ( StringUtils.isBlank(stockFilePath) ) {
            LOGGER.error("stockFilePath is either blank or null");
            return ResponseUtil.failure("stock filePath is either blank or null");
        }
        
        String fileFormat = stockFilePath.substring(stockFilePath.lastIndexOf('.')+1);
        int pageSize = catalogSyncRequest.getPageSize();
        int pageNumber = catalogSyncRequest.getPageNumber();
        int skipIntialLines = (catalogSyncRequest.getPageNumber()-1) * pageSize;

        Iterator<Row> rows = null;
        try {
            if ( stockFilePath.endsWith(".csv")){
                rows = new FKDelimitedFileParser(stockFilePath).parse(skipIntialLines,true);
            }
            else if ( stockFilePath.endsWith(".xls") ) {
                rows = new FKExcelSheetParser(stockFilePath).parse(skipIntialLines,true);
            } else {
                LOGGER.error("file format not supported {}",fileFormat);
                return ResponseUtil.failure("file format not supported, fileFormat : " +fileFormat);
            }
        } catch ( Exception ex ) {
            LOGGER.error("Exception occur while parsing stock file, exception : {}", ex);
            return ResponseUtil.failure("Exception occur while parsing stock file " + ex.getMessage());
        }

        if ( rows.hasNext() ) {
            // Doing pageSize++ ->  in hasNext() implementation we skip the first row always. So Consume the first line of the following
            // page in previous page.
            catalogSyncResponse = fetchCatalogInternal(rows, pageSize++);
        }
        else {
            return ResponseUtil.failure("Invalid Listing report found, path : " + stockFilePath);
        }
        if ( rows.hasNext() ){
            catalogSyncResponse.setHasMore(true);
            catalogSyncResponse.setTotalPages(pageNumber + 1);
        } else {
            catalogSyncResponse.setTotalPages(pageNumber);
        }

        return ResponseUtil.success("Catalog fetched successfully", catalogSyncResponse);
    }

    private String checkIfStockFileExistAtLocal() {
        String filePathWithCSVFormat = getStockFilePath("csv");
        File file = new File(filePathWithCSVFormat);
        if( file.isFile() ) {
            return filePathWithCSVFormat;
        } else {
            String filePathWithXLSFormat = getStockFilePath("xls");
            file = new File(filePathWithXLSFormat);
            if ( file.isFile() ){
                return filePathWithXLSFormat;
            }
        }
        return null;
    }

    private String getStockFilePath(String fileFormat){
        String currentDate = DateUtils.dateToString(DateUtils.getCurrentTime(),"yyyy-MM-dd");
        String fileName = TenantRequestContext.current().getTenantCode() + "_" + FlipkartRequestContext.current().getSellerId() + "_" + currentDate + "." + fileFormat;
        String filePath = "/tmp/" + fileName;
        return filePath;
    }

    @Override public Response catalogSyncPreProcessor(Map<String, String> headers, CatalogPreProcessorRequest catalogPreProcessorRequest) {

        CatalogPreProcessorResponse catalogPreProcessorResponse = new CatalogPreProcessorResponse();
        String stockfilePath = null;
        stockfilePath = checkIfStockFileExistAtLocal();

        if ( StringUtils.isNotBlank(stockfilePath) ) {
            catalogPreProcessorResponse.setFilePath(stockfilePath);
            catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.COMPLETE);
            return ResponseUtil.success("File exist on local", catalogPreProcessorResponse);
        }
        else {

            boolean loginSuccess = flipkartSellerPanelService.sellerPanelLogin(FlipkartRequestContext.current().getUserName(), FlipkartRequestContext.current().getPassword(), false);
            if (!loginSuccess) {
                return ResponseUtil.failure("Login Session could not be established");
            }

            // check if already a file is under generation process or not.
            StockFileDownloadRequestStatusResponse stockFileDownloadRequestStatusResponse = flipkartSellerPanelService.getStockFileDownloadRequestStatus();
            if ( DOWNLOADING.equalsIgnoreCase(stockFileDownloadRequestStatusResponse.getDownloadState())){
                LOGGER.info("File generation is in DOWNLOADING state. Will retry after sometime...");
                fillAsyncDetails(stockFileDownloadRequestStatusResponse, catalogPreProcessorResponse, catalogPreProcessorRequest.isAsyncRun());
                return ResponseUtil.success("File generation is in PROCESSING state. Wait for sometime...", catalogPreProcessorResponse);
            }

            if ( !catalogPreProcessorRequest.isAsyncRun() ) {
                EnqueDownloadRequest enqueDownloadRequest = prepareEnqueStockFileRequest();
                boolean isRequestStockFileSuccessful = flipkartSellerPanelService.requestStockFile(enqueDownloadRequest);
                if (isRequestStockFileSuccessful) {
                    stockFileDownloadRequestStatusResponse = flipkartSellerPanelService.getStockFileDownloadRequestStatus();
                    catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.PROCESSING);
                    fillAsyncDetails(stockFileDownloadRequestStatusResponse, catalogPreProcessorResponse, catalogPreProcessorRequest.isAsyncRun());
                    return ResponseUtil.success("Requested a new stock file. Wait for sometime...", catalogPreProcessorResponse);
                } else {
                    return ResponseUtil.failure("Getting Error while requesting report...");
                }
            }
            else {

                if ( COMPLETED.equalsIgnoreCase(stockFileDownloadRequestStatusResponse.getDownloadState())) {
                    catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.COMPLETE);

                    stockfilePath = checkIfStockFileExistAtLocal();
                    if ( StringUtils.isBlank(stockfilePath) ) {
                        Lock lock = lockingService.getLock(Namespace.CHANNEL_CATALOG_SYNC_DO, FlipkartRequestContext.current().getSellerId(), Level.TENANT);
                        boolean lockAcquired = false;
                        try {
                            lockAcquired = lock.tryLock(10, TimeUnit.SECONDS);
                            if (lockAcquired) {
                                stockfilePath = checkIfStockFileExistAtLocal();
                                if ( StringUtils.isBlank(stockfilePath) ) {
                                    StockFileDownloadNUploadHistoryResponse stockFileDownloadNUploadHistoryResponse = flipkartSellerPanelService.getStockFileDownloadNUploadHistory();
                                    stockfilePath = getStockFile(stockFileDownloadNUploadHistoryResponse);
                                    LOGGER.info("Stock file generation completed.");
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            if (lockAcquired) {
                                try {
                                    lock.unlock();
                                } catch (Exception e) {
                                    LOGGER.error("Error releasing job level lock for job: ", e);
                                }
                            }
                        }
                    }
                }
                else if ( FAILED.equalsIgnoreCase(stockFileDownloadRequestStatusResponse.getDownloadState())) {
                    LOGGER.info("Status of Last file requested is FAILED");
                }
            }
        }

        if ( stockfilePath == null ) {
            return ResponseUtil.failure("Unable to generate report on panel");
        } else {
            catalogPreProcessorResponse.setFilePath(stockfilePath);
            return ResponseUtil.success("File downloaded Successfully", catalogPreProcessorResponse);
        }

    }

    private EnqueDownloadRequest prepareEnqueStockFileRequest() {

        EnqueDownloadRequest.Refiner refiner = new EnqueDownloadRequest.Refiner();

        EnqueDownloadRequest.InternalState internalState = new EnqueDownloadRequest.InternalState.Builder().setExactValue(new EnqueDownloadRequest.ExactValue.Builder().setValue("ACTIVE").build()).setValueType("EXACT").build();
        refiner.addInternalState(internalState);

        EnqueDownloadRequest enqueDownloadRequest = new EnqueDownloadRequest.Builder().setState("LISTING_UI_GROUP").setRefiner(refiner).setVerticalGroup(new EnqueDownloadRequest.VerticalGroup()).build();
        return enqueDownloadRequest;
    }

    @Override
    public Response fetchPendency(Map<String, String> headers) {

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

        if (ShippingPackage.ShippingManager.UNIWARE.equals(generateInvoiceRequest.getShippingPackage().getShippingManager())) {
            createInvoiceResponse.setThirdPartyInvoicingNotAvailable(true);
            return ResponseUtil.success("Shipping Manager is uniware. Generating Uniware Invoicing.", createInvoiceResponse);
        }

        ShippingPackage shippingPackage = generateInvoiceRequest.getShippingPackage();
        ShipmentDetailsV3WithSubPackages shipmentDetails = flipkartSellerApiService.getShipmentDetailsWithSubPackages(shippingPackage.getSaleOrder().getCode());
        if (shipmentDetails.getShipments() == null) {
            return ResponseUtil.failure("Unable to fetch shipment details");
        }

        // backward compatibility -- will remove this code later
        if ( "SELF".equalsIgnoreCase(shipmentDetails.getShipments().get(0).getShipmentType()) ) {
            createInvoiceResponse.setThirdPartyInvoicingNotAvailable(true);
            return ResponseUtil.success("Shipping Manager is uniware. Generating Uniware Invoicing.", createInvoiceResponse);
        }

        List<OrderItem> orderItems = shipmentDetails.getShipments().get(0).getOrderItems();
        boolean isLabelGenerated = orderItems.stream().noneMatch(orderItem -> orderItem.getStatus().name().equalsIgnoreCase("approved"));
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

        ShippingProviderInfo shippingProviderInfo = getCourierInfo(shippingPackage.getSaleOrder().getCode());
        if ( shippingProviderInfo == null ){
            return ResponseUtil.failure("Unable to fetch courier details");
        }

        createInvoiceResponse.setShippingProviderInfo(shippingProviderInfo);

        String filePath = "/tmp/" + TenantRequestContext.current().getHttpSenderIdentifier() + "-" + UUID.randomUUID() + "-invoice_label" + ".pdf";;
        boolean isInvoiceLabelDownloaded = flipkartSellerApiService.downloadInvoiceAndLabel(shippingPackage.getSaleOrder().getCode(),filePath);

        if (isInvoiceLabelDownloaded) {
            String labelSize   = headers.get("labelsize");
            String invoiceSize = headers.get("invoicesize");

            String labelS3URL = null;
            if(StringUtils.isNotBlank(labelSize) && !("A4_FK_Label+Invoice").equals(labelSize)){
                labelS3URL = formatLabel(labelSize, filePath);
            }else{
                labelS3URL = s3Service.uploadFile(new File(filePath),BUCKET_NAME);
            }
            createInvoiceResponse.getShippingProviderInfo().setLabelFormat("PDF");
            createInvoiceResponse.getShippingProviderInfo().setShippingLabelLink(labelS3URL);

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

    private ShippingProviderInfo getCourierInfo(String shipmentId) {
        ShipmentDetailsV3WithAddressResponse shipmentDetailsWithAddress = flipkartSellerApiService.getShipmentDetailsWithAddress(shipmentId);
        if (shipmentDetailsWithAddress.getShipments() == null) {
            LOGGER.info("Unable to fetch shipment details");
            return null;
        }

        Optional<ShipmentDetails.SubShipment> subShipment = shipmentDetailsWithAddress.getShipments().get(0).getSubShipments()
                .stream().filter(ss -> ("SS-1").equalsIgnoreCase(ss.getSubShipmentId())).findFirst();
        if ( subShipment.isPresent() ) {
            ShippingProviderInfo shippingProviderInfo = new ShippingProviderInfo();
            ShipmentDetails.SubShipment subShipmentDetails = subShipment.get();
            String trackingNumber = subShipmentDetails.getCourierDetails().getPickupDetails().getTrackingId();
            String courierName = subShipmentDetails.getCourierDetails().getPickupDetails().getVendorName();
            shippingProviderInfo.setShippingProvider(courierName);
            shippingProviderInfo.setTrackingNumber(trackingNumber);
            return shippingProviderInfo;
        } else {
            LOGGER.error("Unable to found subShipment details");
            return null;
        }
    }

    private boolean getInvoiceDetails(ShippingPackage shippingPackage,CreateInvoiceResponse createInvoiceResponse) {

        Map<String,String> channelSaleOrderItemCodeToChannelProductId = new HashMap<>();
        shippingPackage.getSaleOrderItems().forEach(entry -> channelSaleOrderItemCodeToChannelProductId.put(entry.getChannelSaleOrderItemCode(),entry.getChannelProductId()));

        InvoiceDetailsV3Response invoiceDetailsV3Response = flipkartSellerApiService.getInvoicesInfo(shippingPackage.getSaleOrder().getCode());

        if (invoiceDetailsV3Response != null) {
            if (StringUtils.isNotBlank(shippingPackage.getInvoiceCode()))
                createInvoiceResponse.setInvoiceCode(shippingPackage.getInvoiceCode().split("-")[1]);
            else
                createInvoiceResponse.setInvoiceCode(invoiceDetailsV3Response.getInvoices().get(0).getInvoiceNumber());

            createInvoiceResponse.setDisplayCode(invoiceDetailsV3Response.getInvoices().get(0).getInvoiceNumber());
            createInvoiceResponse.setChannelCreatedTime(DateUtils.stringToDate(invoiceDetailsV3Response.getInvoices().get(0).getInvoiceDate(),"yyyy-MM-dd"));
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
        if (ShippingPackage.ShippingManager.CHANNEL.equals(dispatchShipmentRequest.getShippingPackage().getShippingManager())){
            DispatchStandardShipmentV3Request dispatchStandardShipmentV3Request = prepareDispatchStandardShipmentRequest(dispatchShipmentRequest);
            dispatchShipmentV3Response = flipkartSellerApiService.markStandardFulfilmentShipmentsRTD(dispatchStandardShipmentV3Request);
        }
        else if (ShippingPackage.ShippingManager.UNIWARE.equals(dispatchShipmentRequest.getShippingPackage().getShippingManager())) {
            DispatchSelfShipmentV3Request dispatchSelfShipmentV3Request = prepareDispatchSelfShipmentRequest(dispatchShipmentRequest);
            dispatchShipmentV3Response = flipkartSellerApiService.markSelfShipDispatch(dispatchSelfShipmentV3Request);
        }

        if ( dispatchShipmentV3Response != null ) {
            DispatchShipmentStatus shipment = dispatchShipmentV3Response.getShipments().get(0);
            dispatchShipmentResponse.setSaleOrderCode(shipment.getShipmentId());
            if ( SUCCESS.equalsIgnoreCase(shipment.getStatus())) {
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

    // Doing this operation via dispatch shipments call
    @Override
    public Response closeShippingManifest(Map<String, String> headers, CloseShippingManifestRequest closeShippingManifestRequest) {

//        CloseShippingManifestResponse closeShippingManifestResponse = new CloseShippingManifestResponse();
//        Map<String,String> saleOrderCodeToShippingPackageCode = new HashMap<>();
//
//        DispatchShipmentV3Response dispatchShipmentV3Response = null;
//
//        if ("CHANNEL".equalsIgnoreCase(closeShippingManifestRequest.getShippingManager())){
//            DispatchStandardShipmentV3Request dispatchStandardShipmentRequestV3 = prepareDispatchStandardShipmentRequest(closeShippingManifestRequest, saleOrderCodeToShippingPackageCode);
//            dispatchShipmentV3Response = flipkartSellerApiService.markStandardFulfilmentShipmentsRTD(dispatchStandardShipmentRequestV3);
//        }
//        else if ("SELF".equalsIgnoreCase(closeShippingManifestRequest.getShippingManager())) {
//            DispatchSelfShipmentV3Request dispatchSelfShipmentV3Request = prepareDispatchSelfShipmentRequest(closeShippingManifestRequest, saleOrderCodeToShippingPackageCode);
//            dispatchShipmentV3Response = flipkartSellerApiService.markSelfShipDispatch(dispatchSelfShipmentV3Request);
//        }
//
//        if ( dispatchShipmentV3Response != null ) {
//            for (DispatchShipmentStatus shipments : dispatchShipmentV3Response.getShipments()) {
//                if ( "FAILURE".equalsIgnoreCase(shipments.getStatus()) ) {
//                    CloseShippingManifestResponse.FailedShipment failedShipment = new CloseShippingManifestResponse.FailedShipment();
//                    failedShipment.setShipmentCode(saleOrderCodeToShippingPackageCode.get(shipments.getShipmentId()));
//                    failedShipment.setFailureReason(shipments.getErrorMessage());
//                    failedShipment.setSaleOrderCode(shipments.getShipmentId());
//                    // Todo check whether we can identify if the shipment is cancelled or not ??
//                    if ( "CANCELLED".equalsIgnoreCase(shipments.getErrorMessage()))
//                        failedShipment.setCancelled(true);
//
//                    closeShippingManifestResponse.addFailedShipment(failedShipment);
//                }
//            }
//            closeShippingManifestResponse.setShippingManifestLink("");
//        } else {
//            return ResponseUtil.failure("Unable to close Manifest");
//        }
//
//        return ResponseUtil.success("Manifest Closed Successfully.",closeShippingManifestResponse);
        return ResponseUtil.failure("Operation not live yet");
    }

    @Override public Response fetchCurrentChannelManifest(Map<String,String> headers, CurrentChannelManifestRequest currentChannelManifestRequest) {

        GetManifestRequest getManifestRequest = new GetManifestRequest.Builder()
                .setParams(new GetManifestRequest.Params.Builder()
                        .setVendorGroupCode(getVendorGroupCode(currentChannelManifestRequest.getShippingProviderCode()))
                        .setIsMps(false)
                        .setLocationId(FlipkartRequestContext.current().getLocationId())
                        .build())
                .build();

        String manifestFilePath = "/tmp/"+ com.uniware.integrations.utils.StringUtils.join('_', TenantRequestContext.current().getTenantCode(), UUID.randomUUID().toString(), currentChannelManifestRequest.getShippingManifestCode()) + ".pdf";

        boolean isManifestDownloaded = flipkartSellerApiService.getCurrentChannelManifest(getManifestRequest, manifestFilePath);

        if (isManifestDownloaded) {
            String manifestLink = s3Service.uploadFile(new File(manifestFilePath),BUCKET_NAME);
            CurrentChannelManifestResponse currentChannelManifestResponse = new CurrentChannelManifestResponse();
            currentChannelManifestResponse.setManifestLink(manifestLink);
            return ResponseUtil.success("Manifest downloaded successfully", currentChannelManifestResponse);
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
                    inventoryUpdateStatus.getErrors().stream().forEach(error -> {
                        listing.addError(new Error(error.getCode(), error.getDescription()));
                        if (disabledInventoryErrorMessageList.stream().anyMatch(error.getDescription()::contains))
                            listing.setStatus(UpdateInventoryResponse.Status.DISABLED);
                    });
                    if (inventoryUpdateStatus.getAttributeErrors() != null)
                        inventoryUpdateStatus.getAttributeErrors().stream().forEach(error -> {
                            listing.addError(new Error(error.getCode(), error.getDescription()));
                            if (disabledInventoryErrorMessageList.stream().anyMatch(error.getDescription()::contains))
                                listing.setStatus(UpdateInventoryResponse.Status.DISABLED);
                        });
                }
            }
            updateInventoryResponse.addListing(listing);
            channelSkuCodeToChannelProductId.remove(channelSkuCode);
        }
        channelSkuCodeToChannelProductId.entrySet().stream().forEach(entry -> {
            UpdateInventoryResponse.Listing listing = new UpdateInventoryResponse.Listing();
            listing.addError(new Error(FAILED,"Not getting update confirmation in api response from Flipkart"));
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
        LOGGER.info("Number Of days are left in auth token expiry : {}",numberOfDaysLeftBeforeExpiry);
        if ( numberOfDaysLeftBeforeExpiry < 2 ){
            LOGGER.info("AuthToken will expire in within 2 days refetching new authToken");
            return true;
        }
        else{
            return false;
        }
    }

    private SearchShipmentRequest prepareSearchShimentRequest(Filter.ShipmentTypesEnum shipmentType, int orderWindow, Date orderDateOfLastOrderOfLastPage) {

        DateFilter DispatchAfterDateFilter = new DateFilter();
        DispatchAfterDateFilter.from(DateUtils.dateToString((DateUtils.addToDate(DateUtils.getCurrentDate(), Calendar.DATE, -orderWindow)),DATE_PATTERN));
        if ( Filter.ShipmentTypesEnum.SELF.equals(shipmentType) ) {
            DispatchAfterDateFilter.to(DateUtils.dateToString(DateUtils.addToDate(DateUtils.getCurrentTime(),Calendar.DATE,2 ),DATE_PATTERN));
        } else {
            DispatchAfterDateFilter.to(DateUtils.dateToString(DateUtils.getCurrentTime(),DATE_PATTERN));
        }

        DateFilter orderDateFilter = new DateFilter();
        orderDateFilter.from(DateUtils.dateToString(DateUtils.addToDate(DateUtils.getCurrentDate(), Calendar.DATE, -100),DATE_PATTERN));
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

        int packShipmentFailureCount = 0;
        boolean retryableError = false;
        ShipmentPackV3Request shipmentPackV3Request = preparePackShipmentRequest(shippingPackage, shipmentDetails);
        ShipmentPackV3Response shipmentPackV3Response = null;
        do {
            shipmentPackV3Response = flipkartSellerApiService.packShipment(shipmentPackV3Request);
            PackShipmentStatus shipmentResponse = shipmentPackV3Response.getShipments().get(0);
            if ( "FAILURE".equalsIgnoreCase(shipmentResponse.getStatus())
                    && (shipmentResponse.getErrorMessage().contains("Please re-check your packaging") || shipmentResponse.getErrorMessage().contains("dimensions mentioned in My Listings tab at the time of order placement")) ) {
                packShipmentFailureCount += 1;
                retryableError = true;
                Dimensions dimensions = null;
                if ( packShipmentFailureCount == 1) {
                    dimensions = getPackDimension(shippingPackage);
                }
                else {
                    dimensions = new Dimensions().defaultDimensions();
                }
                shipmentPackV3Request.getShipments().get(0).getSubShipments().get(0).setDimensions(dimensions);
                flipkartSellerApiService.packShipment(shipmentPackV3Request);
            }
            else if( "SUCCESS".equalsIgnoreCase(shipmentPackV3Response.getShipments().get(0).getStatus())) {
                return confirmIfShipmentPacked(shippingPackage.getSaleOrder().getCode());
            }

        } while ( retryableError );

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
            if ( StringUtils.isBlank(saleOrderItem.getCombinationIdentifier())   ) {
                if (channelSaleOrderItemCodeToQty.computeIfPresent(saleOrderItem.getChannelSaleOrderItemCode(), (key, val) -> val + 1) == null ) {
                    channelSaleOrderItemCodeToQty.put(saleOrderItem.getChannelSaleOrderItemCode(),1);
                }
            }
            else if ( combinationIdentifierSet.add(saleOrderItem.getCombinationIdentifier()) ) {
                if (channelSaleOrderItemCodeToQty.computeIfPresent(saleOrderItem.getChannelSaleOrderItemCode(), (key, val) -> val + 1) == null ) {
                    channelSaleOrderItemCodeToQty.put(saleOrderItem.getChannelSaleOrderItemCode(),1);
                }
            }

        }

        channelSaleOrderItemCodeToQty.entrySet().stream().forEach(entry -> { TaxItem taxItem = new TaxItem();
            taxItem.orderItemId(entry.getKey());
            taxItem.setQuantity(entry.getValue());
            taxItem.setTaxRate(BigDecimal.ZERO);
            packRequest.addTaxItemsItem(taxItem);
        });

        shippingPackage.getSaleOrderItems().forEach(saleOrderItem -> {
            String orderItemId = saleOrderItem.getChannelSaleOrderItemCode();
            List<String> serialNumberList=getSerialList(saleOrderItem);
            if(!serialNumberList.isEmpty()){
                LOGGER.info("Serial Number handling not done properly in previous integration. Will handle it as per the case");
                throw new FailureResponse("Serial Number handling not done. Please contact support team");
//                SerialNumber serialNumber = new SerialNumber();
//                serialNumber.addSerialNumbersItem(serialNumberList);
//                serialNumber.setOrderItemId(orderItemId);
//                packRequest.addSerialNumbersItem(serialNumber);
            }

        });

        SubShipments subShipment = new SubShipments().subShipmentId("SS-1").dimensions(dimensions != null ? dimensions : getPackDimension(shippingPackage));
        Invoice invoice = new Invoice()
                .orderId(shippingPackage.getSaleOrder().getDisplayOrderCode())
                .invoiceNumber("")
                .invoiceDate(DateUtils.dateToString(DateUtils.getCurrentDate(),"yyyy-MM-dd"));

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

            String downloadFilePath = getStockFilePath(fileFormat);

            String stockFilePath = flipkartSellerPanelService.downloadStockFile(fileName,fileLink,downloadFilePath);
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
        while ( rows.hasNext() && pageSize > 0 ) {
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

                if (StringUtils.equalsIngoreCaseAny(TenantRequestContext.current().getSourceCode(), "FLIPKART", "FLIPKART_OMNI") ){
                    channelItemType.addAttribute(new ChannelItemType.Attribute.Builder()
                            .setName("FSN")
                            .setValue(row.getColumnValue("Flipkart Serial Number"))
                            .build());
                }
                catalogSyncResponse.addChannelItemType(channelItemType);
            }
            pageSize--;
        }
        return catalogSyncResponse;
    }


    private boolean validateRow(Row row) {

        if ( StringUtils.isNotBlank(row.getColumnValue("Inactive Reason"))){
            LOGGER.info("Skipped row : {}, due to inactive reason : {}",row,row.getColumnValue("Inactive Reason"));
            return false;
        }   else if ( row.getColumnValue("Listing Archival") != null
                && ("ARCHIVED").equalsIgnoreCase(row.getColumnValue("Listing Archival"))) {
            LOGGER.info("Skipped row : {}, as either its  Listing Archival is null or has value ARCHIVED",row);
            return false;
        }   else if ( row.getColumnValue("Fulfillment By") != null
                && !sourceCodeToFulfilmentModeList.get(TenantRequestContext.current().getSourceCode()).stream().anyMatch(row.getColumnValue("Fulfillment By")::equalsIgnoreCase)) {
            LOGGER.info("Skipped row : {}, Fulfiment mode not match",row);
            return false;
        }

        if ( "FLIPKART_OMNI".equalsIgnoreCase(TenantRequestContext.current().getSourceCode())
                && !"SELLER".equalsIgnoreCase(row.getColumnValue("Shipping Provider"))) {
            LOGGER.info("Skipped row : {}, For FLIPKART_OMNI we only fetch listings where SHIPPING_PROVDER : SELLER",row);
            return false;
        }
        return true;
    }

    private void fillAsyncDetails(StockFileDownloadRequestStatusResponse stockFileDownloadRequestStatusResponse,CatalogPreProcessorResponse catalogPreProcessorResponse, boolean isAsyncRun) {

        if ( !isAsyncRun ) {
            CatalogPreProcessorResponse.AsyncDetails asyncDetails = new CatalogPreProcessorResponse.AsyncDetails();
            asyncDetails.setNextRunInMs(1000*(stockFileDownloadRequestStatusResponse.getTotalCount() - stockFileDownloadRequestStatusResponse.getProcessed_count())/50);
            asyncDetails.setRetryCount(2);
            catalogPreProcessorResponse.setAsyncDetails(asyncDetails);
        }
        catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.PROCESSING);

    }

    private String getChannelProductIdBySourceCode(Row row, String sourceCode) {
        if ( "FLIPKART_FA".equalsIgnoreCase(sourceCode) ){
            return row.getColumnValue("Flipkart Serial Number");
        }
        else if ( "FLIPKART_LITE".equalsIgnoreCase(sourceCode) ) {
            return row.getColumnValue("Seller SKU Id");
        }

        return row.getColumnValue("Listing ID");
    }

    @Override
    public Response generateShipLabel(Map<String, String> headers, GenerateLabelRequest generateLabelRequest)
    {

        String shipmentId = generateLabelRequest.getSaleOrderCode();
        ShippingProviderInfo shippingProviderInfo = getCourierInfo(shipmentId);
        if ( shippingProviderInfo == null ){
            return ResponseUtil.failure("Unable to fetch courier details");
        }

        String filePath = "/tmp/" + TenantRequestContext.current().getHttpSenderIdentifier() + "-" + UUID.randomUUID() + "-invoice_label" + ".pdf";;
        boolean isInvoiceLabelDownloaded = flipkartSellerApiService.downloadInvoiceAndLabel(shipmentId,filePath);

        if (isInvoiceLabelDownloaded) {
            String labelSize   = headers.get("labelsize");

            String labelS3URL = null;
            if(StringUtils.isNotBlank(labelSize) && !("A4_FK_Label+Invoice").equals(labelSize)){
                labelS3URL = formatLabel(labelSize, filePath);
            }else{
                labelS3URL = s3Service.uploadFile(new File(filePath),BUCKET_NAME);
            }
            shippingProviderInfo.setLabelFormat("PDF");
            shippingProviderInfo.setShippingLabelLink(labelS3URL);
        } else {
            return ResponseUtil.failure("Unable to download InvoiceLabel document ");
        }

        return ResponseUtil.success("Fetch Shipping Details Successfully", shippingProviderInfo );
    }

    private SearchShipmentRequest prepareFetchPendencyRequestForApprovedShipments(Date fromOrderDate) {

        Filter filter = new Filter();
        filter.addStatesItem(Filter.StatesEnum.APPROVED);
        filter.locationId(FlipkartRequestContext.current().getLocationId());
        filter.type(Filter.TypeEnum.PREDISPATCH);
        filter.addShipmentTypesItem(Filter.ShipmentTypesEnum.SELF);
        filter.addShipmentTypesItem(Filter.ShipmentTypesEnum.NORMAL);
        filter.addShipmentTypesItem(Filter.ShipmentTypesEnum.EXPRESS);

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
        boolean hasMore;

        do {
            hasMore = false;

            FetchOnHoldOrderRequest fetchOnHoldOrderRequest = prepareFetchOnHoldOrderRequest(pageNumber);
            String pendencyForOnHoldOrders = flipkartSellerPanelService.getOnHoldOrdersFromPanel(fetchOnHoldOrderRequest);
            if ( pendencyForOnHoldOrders != null ) {
                try {
                    JsonObject jsonObject = new Gson().fromJson(pendencyForOnHoldOrders, JsonObject.class);
                    hasMore = jsonObject.get("has_more").getAsBoolean();
                    JsonArray orders = jsonObject.getAsJsonArray("items");
                    Iterator<JsonElement> ordersIterator = orders.iterator();
                    while (ordersIterator.hasNext()) {
                        JsonObject order = ordersIterator.next().getAsJsonObject();
                        JsonArray orderItems = order.get("order_items").getAsJsonArray();
                        Iterator<JsonElement> orderItemsIterator = orderItems.iterator();
                        int qty = 0;
                        while (orderItemsIterator.hasNext()) {
                            JsonElement orderItem = orderItemsIterator.next();
                            String listingId = orderItem.getAsJsonObject().get("listing_id").getAsString();
                            qty += orderItem.getAsJsonObject().get("quantity").getAsInt();
                            if (channelProductIdToPendency.computeIfPresent(listingId, (key, val) -> val.addRequiredInventory(orderItem.getAsJsonObject().get("quantity").getAsInt())) == null) {
                                Pendency pendency = new Pendency();
                                pendency.setChannelProductId(listingId);
                                pendency.setProductName(orderItem.getAsJsonObject().get("product_details").getAsJsonObject().get("title").getAsString());
                                pendency.setRequiredInventory(orderItem.getAsJsonObject().get("quantity").getAsInt());
                                pendency.setSellerSkuCode(orderItem.getAsJsonObject().get("sku").getAsString().replaceAll("&quot;", ""));
                                channelProductIdToPendency.put(listingId,pendency);
                            }
                        }
                        LOGGER.info("Pendency count for order : {} is : {}", order.get("order_items").getAsJsonArray().iterator().next(), qty);
                    }
                } catch (JsonSyntaxException e) {
                    LOGGER.error("Unable to parse response or Invalid Json, json ", pendencyForOnHoldOrders);
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
        Date skipSelfShipOrdersBeforeDate = DateUtils.addToDate(DateUtils.getCurrentTime(),Calendar.DATE,2);

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

                    // fetching these shipments as orders in uniware.
                    if ( "SELF".equalsIgnoreCase(shipment.getShipmentType()) && DateUtils.diff(shipment.getDispatchAfterDate() ,skipSelfShipOrdersBeforeDate,
                            DateUtils.Resolution.MILLISECOND) > 0)
                        continue;

                    // Adding shipments in a HashSet to avoid duplicity of shipments in api response
                    if ( shipmentIdSet.add(shipment.getShipmentId())) {
                        int qty = 0;
                        for (OrderItem orderItem : shipment.getOrderItems()) {
                            String listingId = orderItem.getListingId();
                            qty += orderItem.getQuantity();
                            if (channelProductIdToPendency.computeIfPresent(listingId, (key, val) -> val.addRequiredInventory(orderItem.getQuantity())) == null) {
                                Pendency pendency = new Pendency();
                                pendency.setChannelProductId(listingId);
                                pendency.setProductName(orderItem.getTitle());
                                pendency.setRequiredInventory(orderItem.getQuantity());
                                pendency.setSellerSkuCode(orderItem.getSku());
                                channelProductIdToPendency.put(listingId,pendency);
                            }
                        }
                        LOGGER.info("Pendency count for order : {} is : {}", shipment.getOrderItems().get(0).getOrderId(), qty);
                    }
                    if ( (pageNumber % 249 ) == 0) {
                        Date orderDate = shipment.getOrderItems().get(0).getOrderDate();
                        orderDateOfLastOrderOfLastpage = orderDateOfLastOrderOfLastpage != null ? orderDateOfLastOrderOfLastpage : orderDate;
                        if ( (orderDate.getTime() - orderDateOfLastOrderOfLastpage.getTime()) > 0){
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

        LOGGER.info("Fetched pendency till page number :{}", --pageNumber);

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

            String orderId = null;
            for (OrderItem orderItem : shipment.getOrderItems()) {
                PriceComponent priceComponent = orderItem.getPriceComponents();
                if ( NumberUtils.greaterThan(priceComponent.getSellingPrice(),BigDecimal.ZERO)
                    || NumberUtils.greaterThan(priceComponent.getTotalPrice(),BigDecimal.ZERO)
                    || NumberUtils.greaterThan(priceComponent.getShippingCharge(),BigDecimal.ZERO)
                    || NumberUtils.greaterThan(priceComponent.getCustomerPrice(),BigDecimal.ZERO)
                    || NumberUtils.greaterThan(priceComponent.getFlipkartDiscount(),BigDecimal.ZERO) ){
                    orderId = orderItem.getOrderItemId();
                    break;
                }
            }

            SaleOrder saleOrder = new SaleOrder();
            saleOrder.setCode(orderId);
            saleOrder.setDisplayOrderCode(shipment.getOrderItems().get(0).getOrderId());
            saleOrder.setDisplayOrderDateTime(shipment.getOrderItems().get(0).getOrderDate());
            saleOrder.setFulfillmentTat(shipment.getDispatchByDate());
            saleOrder.setChannel(TenantRequestContext.current().getChannelCode());
            if ( StringUtils.isNotBlank(shipment.getOrderItems().get(0).getPaymentType().name())) {
                saleOrder.setCashOnDelivery("COD".equalsIgnoreCase(shipment.getOrderItems().get(0).getPaymentType().name()));
            } else {
                saleOrder.setCashOnDelivery(true);
            }
            String customerPhoneNumber = shipmentDetailsWithAddress.getDeliveryAddress().getContactNumber();
            saleOrder.setNotificationMobile(StringUtils.isNotBlank(customerPhoneNumber) ? customerPhoneNumber : DEFAULT_PHONE_NUMBER);
            Map<String, Object> shipmentIdJson = new HashMap<>();
            Map<String,String> tempMap = new HashMap<>();
            tempMap.put("value",shipment.getShipmentId());
            shipmentIdJson.put("shipmentId",tempMap);
            saleOrder.setAdditionalInfo(JsonUtils.objectToString(shipmentIdJson));

            if ( "EXPRESS".equalsIgnoreCase(shipment.getShipmentType()) ) {
                saleOrder.setPriority(1);
            }
            if ( "SELF".equalsIgnoreCase(shipment.getShipmentType()) ) {
                saleOrder.setThirdPartyShipping(false);
            }

            List<SaleOrderItem> saleOrderItems = new ArrayList<>();
            for (OrderItem orderItem: shipment.getOrderItems()) {
                if ( "CANCELLED".equalsIgnoreCase(orderItem.getStatus().name())) {
                    LOGGER.info("Skipping orderItem : {}, orderCode : {}, since it's status is CANCELLED",orderItem.getOrderItemId(),orderItem.getOrderId());
                    continue;
                }
                for (int count = 1; count <= orderItem.getQuantity(); count++) {
                    SaleOrderItem saleOrderItem = new SaleOrderItem();
                    saleOrderItem.setCode(orderItem.getOrderItemId() + "-" + count);
                    saleOrderItem.setChannelSaleOrderItemCode(orderItem.getOrderItemId());
                    saleOrderItem.setItemSku(orderItem.getSku().replaceAll("&quot;", ""));
                    saleOrderItem.setChannelProductId(orderItem.getListingId().replaceAll("&quot;", ""));

                    String itemName = orderItem.getTitle() + orderItem.getSku().replaceAll("&quot;", "");
                    saleOrderItem.setItemName(itemName.length() > 200 ? itemName.substring(0,200) : itemName);
                    saleOrderItem.setShippingMethodCode("STD");

                    BigDecimal quantity = BigDecimal.valueOf(orderItem.getQuantity());
                    PriceComponent priceComponent = orderItem.getPriceComponents();
                    BigDecimal sellingPrice = priceComponent.getSellingPrice();
                    BigDecimal customerPrice = priceComponent.getCustomerPrice();
                    BigDecimal shippingCharges = priceComponent.getShippingCharge();
                    BigDecimal totalPrice = priceComponent.getTotalPrice();
                    BigDecimal discount = priceComponent.getFlipkartDiscount();

                    //in v3, customer price includes shipping charges, so to get the true customer price, we always need to subtract shipping charges from it.
                    discount = customerPrice.subtract(shippingCharges).subtract(sellingPrice);

                    if (NumberUtils.lessThan(discount,new BigDecimal(0)))
                        discount =  discount.multiply(new BigDecimal(-1));

                    if (NumberUtils.lessThan(customerPrice.subtract(shippingCharges),sellingPrice))
                        sellingPrice = customerPrice.subtract(shippingCharges);

                    saleOrderItem.setSellingPrice(NumberUtils.divide(sellingPrice,quantity));
                    saleOrderItem.setDiscount(NumberUtils.divide(discount,quantity));
                    saleOrderItem.setShippingCharges(NumberUtils.divide(shippingCharges,quantity));
                    saleOrderItem.setTotalPrice(NumberUtils.divide(totalPrice,quantity));
                    saleOrderItem.setPacketNumber(1);
                    saleOrderItems.add(saleOrderItem);
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

        if ( StringUtils.isNotBlank(address.getAddressLine2())) {
            addressDetail.setAddressLine2(address.getAddressLine2());
        }

        addressDetail.setCity(address.getCity());
        addressDetail.setState(address.getState());
        addressDetail.setCountry("IN");
        addressDetail.setPincode(address.getPinCode());

        if ( StringUtils.isNotBlank(address.getContactNumber())) {
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
        Optional<UpdateInventoryRequest.Attributes> attribute = attributes == null ? null : attributes.stream().filter(lineItem -> "fsn".equalsIgnoreCase(lineItem.getName())).findFirst();

        if (attribute != null && attribute.isPresent()) {
            fsn = attribute.get().getValue();
        } else {
            fsn = channelProductId.substring(3, channelProductId.length() - 6);
        }

        return fsn;
    }

    private DispatchStandardShipmentV3Request prepareDispatchStandardShipmentRequest(DispatchShipmentRequest dispatchShipmentRequest) {

        ShippingPackage shippingPackage = dispatchShipmentRequest.getShippingPackage();
        DispatchStandardShipmentV3Request dispatchStandardShipmentV3Request = new DispatchStandardShipmentV3Request();
        dispatchStandardShipmentV3Request.addShipmentId(shippingPackage.getSaleOrder().getCode())
                .setLocationId(FlipkartRequestContext.current().getLocationId());
        return dispatchStandardShipmentV3Request;
    }

    private DispatchSelfShipmentV3Request prepareDispatchSelfShipmentRequest(DispatchShipmentRequest dispatchShipmentRequest ) {

        ShippingPackage shippingPackage = dispatchShipmentRequest.getShippingPackage();
        DispatchSelfShipmentV3Request dispatchSelfShipmentV3Request = new DispatchSelfShipmentV3Request();

        DispatchRequest.Invoice invoice = new DispatchRequest.Invoice();
        invoice.setInvoiceNumber(shippingPackage.getInvoiceCode());
        invoice.setInvoiceDate(DateUtils.dateToString(shippingPackage.getInvoiceDate(),"yyyy-MM-dd"));
        HashSet<String> combinationIdentifierSet = new HashSet<>();
        HashMap<String,ConfirmItemRow> channelSaleOrderItemCodeToConfirtItemRow = new HashMap<>();
        for (ShippingPackage.SaleOrderItem saleOrderItem : shippingPackage.getSaleOrderItems()) {

            ConfirmItemRow confirmItemRow = channelSaleOrderItemCodeToConfirtItemRow.get(saleOrderItem.getChannelSaleOrderItemCode());
            if ( confirmItemRow == null ) {
                confirmItemRow = new ConfirmItemRow();
                confirmItemRow.orderItemId(saleOrderItem.getChannelSaleOrderItemCode());
                confirmItemRow.quantity(0);
                confirmItemRow.serialNumbers(new ArrayList<>());
                channelSaleOrderItemCodeToConfirtItemRow.put(saleOrderItem.getChannelSaleOrderItemCode(),confirmItemRow);
            }

            if ( StringUtils.isNotBlank(saleOrderItem.getCombinationIdentifier())) {
                if ( combinationIdentifierSet.add(saleOrderItem.getCombinationIdentifier())){
                    confirmItemRow.setQuantity(confirmItemRow.getQuantity()+1);
                }
            } else {
                confirmItemRow.setQuantity(confirmItemRow.getQuantity()+1);
            }
        }

        invoice.orderItems(channelSaleOrderItemCodeToConfirtItemRow.values().stream().collect(Collectors.toList()));
        DispatchRequest shipment = new DispatchRequest();
        shipment.setInvoice(invoice);
        shipment.setShipmentId(shippingPackage.getSaleOrder().getCode());
        shipment.setDispatchDate(DateUtils.dateToString(DateUtils.clearTime(shippingPackage.getSaleOrder().getDisplaySaleOrderDateTime()),"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        shipment.setLocationId(FlipkartRequestContext.current().getLocationId());
        shipment.setTrackingId(shippingPackage.getShippingProviderInfo().getTrackingNumber());
        shipment.setTentativeDeliveryDate((DateUtils.dateToString(DateUtils.addDaysToDate(DateUtils.getCurrentTime(),5),"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));
        if (shippingPackage.getShippingProviderInfo().isShippingProviderIsAggregator())
            shipment.setDeliveryPartner(shippingPackage.getShippingProviderInfo().getAggregatorAllocatedCourier());
        else
            shipment.setDeliveryPartner(shippingPackage.getShippingProviderInfo().getShippingProvider());
        dispatchSelfShipmentV3Request.addShipmentsItem(shipment);
        return dispatchSelfShipmentV3Request;
    }

    private String getVendorGroupCode(String shippingProviderCode) {
        // Should lowercase the string then check contains method
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

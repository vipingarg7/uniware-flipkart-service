package com.uniware.integrations.client.service;

import com.unifier.core.fileparser.Row;
import com.unifier.core.utils.DateUtils;
import com.unifier.core.utils.StringUtils;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.constants.EnvironmentPropertiesConstant;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.dto.api.requestDto.EnqueDownloadRequest;
import com.uniware.integrations.client.dto.api.responseDto.AuthTokenResponse;
import com.uniware.integrations.client.dto.api.responseDto.LocationDetailsResponse;
import com.uniware.integrations.client.dto.api.responseDto.StockFileDownloadNUploadHistoryResponse;
import com.uniware.integrations.client.dto.api.responseDto.StockFileDownloadRequestStatusResponse;
import com.uniware.integrations.client.utils.FKDelimitedFileParser;
import com.uniware.integrations.client.utils.FKExcelSheetParser;
import com.uniware.integrations.core.dto.api.Response;
import com.uniware.integrations.locking.ILockingService;
import com.uniware.integrations.locking.Level;
import com.uniware.integrations.locking.Namespace;
import com.uniware.integrations.uniware.authentication.connector.request.dto.ConnectorVerificationRequest;
import com.uniware.integrations.uniware.authentication.connector.response.dto.ConnectorVerificationResponse;
import com.uniware.integrations.uniware.authentication.postConfig.request.dto.PostConfigurationRequest;
import com.uniware.integrations.uniware.authentication.postConfig.response.dto.PostConfigurationResponse;
import com.uniware.integrations.uniware.authentication.preConfig.request.dto.PreConfigurationRequest;
import com.uniware.integrations.uniware.authentication.preConfig.response.dto.PreConfigurationResponse;
import com.uniware.integrations.uniware.catalog.request.dto.CatalogPreProcessorRequest;
import com.uniware.integrations.uniware.catalog.request.dto.CatalogSyncRequest;
import com.uniware.integrations.uniware.catalog.response.dto.CatalogPreProcessorResponse;
import com.uniware.integrations.uniware.catalog.response.dto.CatalogSyncResponse;
import com.uniware.integrations.uniware.catalog.response.dto.ChannelItemType;
import com.uniware.integrations.utils.ResponseUtil;
import com.uniware.integrations.web.context.TenantRequestContext;
import com.uniware.integrations.web.exception.BadRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import static com.uniware.integrations.client.constants.flipkartConstants.AUTH_TOKEN;
import static com.uniware.integrations.client.constants.flipkartConstants.AUTH_TOKEN_EXPIRES_IN;
import static com.uniware.integrations.client.constants.flipkartConstants.CATALOG;
import static com.uniware.integrations.client.constants.flipkartConstants.CLIENT_ID;
import static com.uniware.integrations.client.constants.flipkartConstants.CODE;
import static com.uniware.integrations.client.constants.flipkartConstants.COMPLETED;
import static com.uniware.integrations.client.constants.flipkartConstants.CURRENT_CHANNEL_MANIFEST;
import static com.uniware.integrations.client.constants.flipkartConstants.DOWNLOADING;
import static com.uniware.integrations.client.constants.flipkartConstants.FAILED;
import static com.uniware.integrations.client.constants.flipkartConstants.FK_CODE;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_AND_SELLER;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_AND_SELLER_SMART;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_FA;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_INVENTORY_PANEL;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_LITE;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_OMNI;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_SELLER_PANEL;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_SMART;
import static com.uniware.integrations.client.constants.flipkartConstants.GET;
import static com.uniware.integrations.client.constants.flipkartConstants.INVOICE;
import static com.uniware.integrations.client.constants.flipkartConstants.INVOICE_LABEL;
import static com.uniware.integrations.client.constants.flipkartConstants.LABEL;
import static com.uniware.integrations.client.constants.flipkartConstants.LOCATION_ID;
import static com.uniware.integrations.client.constants.flipkartConstants.PASSWORD;
import static com.uniware.integrations.client.constants.flipkartConstants.PENDENCY;
import static com.uniware.integrations.client.constants.flipkartConstants.REDIRECT_URI;
import static com.uniware.integrations.client.constants.flipkartConstants.REFRESH_TOKEN;
import static com.uniware.integrations.client.constants.flipkartConstants.RESPONSE_TYPE;
import static com.uniware.integrations.client.constants.flipkartConstants.SCOPE;
import static com.uniware.integrations.client.constants.flipkartConstants.SELLER;
import static com.uniware.integrations.client.constants.flipkartConstants.SELLERSMART;
import static com.uniware.integrations.client.constants.flipkartConstants.SELLER_ID;
import static com.uniware.integrations.client.constants.flipkartConstants.SELLER_SMART;
import static com.uniware.integrations.client.constants.flipkartConstants.STATE;
import static com.uniware.integrations.client.constants.flipkartConstants.SUCCESS;
import static com.uniware.integrations.client.constants.flipkartConstants.TMP_DIR;
import static com.uniware.integrations.client.constants.flipkartConstants.USERNAME;
import static com.uniware.integrations.client.constants.flipkartConstants._2_GUD;

@Service(value = "FlipkartHelperService")
public class FlipkartHelperService {

    @Autowired private Environment environment;
    @Autowired private ILockingService lockingService;
    @Autowired private FlipkartSellerApiService flipkartSellerApiService;
    @Autowired FlipkartSellerPanelService flipkartSellerPanelService;

    public enum FulfilmentProfile {
        FBF,
        NON_FBF,
        FBF_LITE,
        FBF_AND_NON_FBF,
        FBF_AND_FBF_LITE
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(FlipkartHelperService.class);
    private static final Map<String, List<String>> sourceCodeToFulfilmentModeList = new HashMap();
    private static final Map<String,List<String>> sourceCodeToFulfimentProfileList = new HashMap();

    static {
        for ( ChannelSource channelSource: ChannelSource.values() ) {

            if ( StringUtils.equalsIngoreCaseAny(channelSource.getChannelSourceCode(),FLIPKART,_2_GUD,FLIPKART_OMNI)){
                sourceCodeToFulfilmentModeList.put(channelSource.getChannelSourceCode(),
                        Arrays.asList(SELLER,FLIPKART_AND_SELLER));
                sourceCodeToFulfimentProfileList.put(channelSource.getChannelSourceCode(), Arrays.asList(
                        FulfilmentProfile.NON_FBF.name(),
                        FulfilmentProfile.FBF_AND_NON_FBF.name()));
            }
            else if ( StringUtils.equalsIngoreCaseAny(channelSource.getChannelSourceCode(),FLIPKART_LITE,FLIPKART_SMART)) {
                sourceCodeToFulfilmentModeList.put(channelSource.getChannelSourceCode(),Arrays.asList(SELLERSMART,SELLER_SMART,
                        FLIPKART_AND_SELLER_SMART));
                sourceCodeToFulfimentProfileList.put(channelSource.getChannelSourceCode(), Arrays.asList(
                        FulfilmentProfile.FBF_AND_FBF_LITE.name(),
                        FulfilmentProfile.FBF_LITE.name()));
            }
            else if ( StringUtils.equalsIngoreCaseAny(channelSource.getChannelSourceCode(),FLIPKART_FA) ) {
                sourceCodeToFulfilmentModeList.put(channelSource.getChannelSourceCode(),Arrays.asList(FLIPKART,FLIPKART_AND_SELLER_SMART,FLIPKART_AND_SELLER));
                sourceCodeToFulfimentProfileList.put(channelSource.getChannelSourceCode(), Arrays.asList(
                        FulfilmentProfile.FBF_AND_NON_FBF.name(),
                        FulfilmentProfile.FBF.name(),
                        FulfilmentProfile.FBF_AND_FBF_LITE.name()));
            }
        }
    }

    public Response preConfiguration( PreConfigurationRequest preConfigurationRequest ) {

        String state = preConfigurationRequest.getParams() != null ? preConfigurationRequest.getParams().get(STATE) : null;
        if ( StringUtils.isBlank(state )) {
            throw new BadRequest("State is missing in request payload");
        }

        state = state + "?locationId=" + FlipkartRequestContext.current().getLocationId();

        PreConfigurationResponse preConfigurationResponse = new PreConfigurationResponse();
        HashMap<String, String> params = new HashMap<>();
        params.put(CLIENT_ID, environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_APPLICATION_ID));
        params.put(RESPONSE_TYPE, CODE);
        params.put(SCOPE, environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_SCOPE));
        params.put(STATE, state);
        params.put(REDIRECT_URI, environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_REDIRECT_URL));
        preConfigurationResponse.setUrl(flipkartSellerApiService.getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource()) + "/oauth-service/oauth/authorize");
        preConfigurationResponse.setMethod(GET);
        preConfigurationResponse.setParams(params);

        return ResponseUtil.success(SUCCESS, preConfigurationResponse);
    }

    public Response postConfiguration(PostConfigurationRequest postConfigurationRequest) {

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

    public Response connectorVerification(ConnectorVerificationRequest connectorVerificationRequest) {

        ConnectorVerificationResponse connectorVerificationResponse = new ConnectorVerificationResponse();
        Map<String, String> connectorParameters = connectorVerificationRequest.getConnectorParameters();
        Map<String, String> responseParams = new HashMap<>();

        switch ( connectorVerificationRequest.getConnectorName() ) {

            case FLIPKART_SELLER_PANEL:
                String username = connectorParameters.get(USERNAME);
                String password = connectorParameters.get(PASSWORD);
                boolean loginSuccess = flipkartSellerPanelService.sellerPanelLogin(username, password, false);
                if (!loginSuccess) {
                    return ResponseUtil.failure("Unable to login on Flipkart panel.");
                }
                if (connectorVerificationRequest.getUserTriggeredVerification()) {
                    String sellerId = flipkartSellerPanelService.getFeaturesForSeller().getFirst();
                    responseParams.put(USERNAME, username);
                    responseParams.put(PASSWORD, password);
                    responseParams.put(SELLER_ID, sellerId);
                    connectorVerificationResponse.setConnectorParamters(responseParams);
                }
                break;

            case FLIPKART_INVENTORY_PANEL:
                String refreshToken = connectorParameters.get(REFRESH_TOKEN);
                Long authTokenExpiresIn = Long.valueOf(connectorParameters.get(AUTH_TOKEN_EXPIRES_IN));
                String locationId = connectorParameters.get(LOCATION_ID);

                boolean isAuthTokenExpiryNear = isAuthTokenExpiryNear(authTokenExpiresIn);
                if (isAuthTokenExpiryNear) {
                    AuthTokenResponse authTokenResponse = flipkartSellerApiService.refreshAuthToken(refreshToken);
                    if (authTokenResponse == null)
                        return ResponseUtil.failure("Unable to fetch auth token");
                    String authToken = authTokenResponse.getAccessToken();
                    refreshToken = authTokenResponse.getRefreshToken();
                    authTokenExpiresIn = authTokenResponse.getExpiresIn();
                    authTokenExpiresIn = (authTokenExpiresIn * 1000) + DateUtils.getCurrentTime().getTime();
                    FlipkartRequestContext.current().setAuthToken(authToken);
                    responseParams.put(AUTH_TOKEN, authToken);
                    responseParams.put(REFRESH_TOKEN, refreshToken);
                    responseParams.put(AUTH_TOKEN_EXPIRES_IN, String.valueOf(authTokenExpiresIn));
                    connectorVerificationResponse.setConnectorParamters(responseParams);
                }

                LocationDetailsResponse locationDetailsResponse = flipkartSellerApiService.getAllLocations();
                if (locationDetailsResponse == null)
                    return ResponseUtil.failure("Getting error while fetching location details");
                boolean isValidLocation = locationDetailsResponse.getLocations().stream().anyMatch(location -> locationId.equalsIgnoreCase(location.getLocationId()));
                if (!isValidLocation) {
                    return ResponseUtil.failure("Invalid locationId. Kindly check");
                }
                break;

            default:
                LOGGER.info("Invalid connector name");
                return ResponseUtil.failure("Invalid Connector");
        }

        return ResponseUtil.success("Connector verified successfully ", connectorVerificationResponse);
    }

    public Response catalogSyncPreProcessor(Map<String, String> headers, CatalogPreProcessorRequest catalogPreProcessorRequest) {

        CatalogPreProcessorResponse catalogPreProcessorResponse = new CatalogPreProcessorResponse();
        String stockfilePath = null;
        stockfilePath = checkIfStockFileExistAtLocal();

        if (StringUtils.isNotBlank(stockfilePath)) {
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
            if (DOWNLOADING.equalsIgnoreCase(stockFileDownloadRequestStatusResponse.getDownloadState())) {
                LOGGER.info("File generation is in DOWNLOADING state. Will retry after sometime...");
                fillAsyncDetails(stockFileDownloadRequestStatusResponse, catalogPreProcessorResponse,
                        catalogPreProcessorRequest.isAsyncRun());
                return ResponseUtil.success("File generation is in PROCESSING state. Wait for sometime...",
                        catalogPreProcessorResponse);
            }

            if (!catalogPreProcessorRequest.isAsyncRun()) {
                boolean fetchCsrfToken = flipkartSellerPanelService.fetchCsrfToken();
                if (!fetchCsrfToken) {
                    return ResponseUtil.failure("Unable to fetch CsrfToken");
                }
                EnqueDownloadRequest enqueDownloadRequest = prepareEnqueStockFileRequest();
                boolean isRequestStockFileSuccessful = flipkartSellerPanelService.requestStockFile(enqueDownloadRequest);
                if (isRequestStockFileSuccessful) {
                    stockFileDownloadRequestStatusResponse = flipkartSellerPanelService.getStockFileDownloadRequestStatus();
                    catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.PROCESSING);
                    fillAsyncDetails(stockFileDownloadRequestStatusResponse, catalogPreProcessorResponse,
                            catalogPreProcessorRequest.isAsyncRun());
                    return ResponseUtil.success("Requested a new stock file. Wait for sometime...",
                            catalogPreProcessorResponse);
                }
                else {
                    return ResponseUtil.failure("Getting Error while requesting report...");
                }
            }
            else {

                if (COMPLETED.equalsIgnoreCase(stockFileDownloadRequestStatusResponse.getDownloadState())) {
                    catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.COMPLETE);

                    stockfilePath = checkIfStockFileExistAtLocal();
                    if (StringUtils.isBlank(stockfilePath)) {
                        Lock lock = lockingService.getLock(Namespace.CHANNEL_CATALOG_SYNC_DO,
                                FlipkartRequestContext.current().getSellerId(), Level.TENANT);
                        boolean lockAcquired = false;
                        try {
                            lockAcquired = lock.tryLock(100, TimeUnit.MILLISECONDS);
                            if (lockAcquired) {
                                stockfilePath = checkIfStockFileExistAtLocal();
                                if (StringUtils.isBlank(stockfilePath)) {
                                    StockFileDownloadNUploadHistoryResponse stockFileDownloadNUploadHistoryResponse = flipkartSellerPanelService.getStockFileDownloadNUploadHistory();
                                    stockfilePath = getStockFile(stockFileDownloadNUploadHistoryResponse);
                                    LOGGER.info("Stock file generation completed, filePath {}", stockfilePath);
                                }
                            }
                            else {
                                catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.PROCESSING);
                                return ResponseUtil.success("Unable to aquire lock, will sync catalog in next async run",
                                        catalogPreProcessorResponse);
                            }
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finally {
                            if (lockAcquired) {
                                try {
                                    lock.unlock();
                                }
                                catch (Exception e) {
                                    LOGGER.error("Error releasing job level lock for job: ", e);
                                }
                            }
                        }
                    }
                }
                else if (FAILED.equalsIgnoreCase(stockFileDownloadRequestStatusResponse.getDownloadState())) {
                    LOGGER.info("Status of Last file requested is FAILED");
                }
            }
        }

        if (stockfilePath == null) {
            return ResponseUtil.failure("Unable to generate report on panel");
        }
        else {
            catalogPreProcessorResponse.setFilePath(stockfilePath);
            return ResponseUtil.success("File downloaded Successfully", catalogPreProcessorResponse);
        }
    }

    public Response fetchCatalog(Map<String, String> headers, CatalogSyncRequest catalogSyncRequest) {

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

        Iterator<Row> rows;
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
            LOGGER.error("Exception occur while parsing stock file, exception : {}", ex.getMessage());
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
                        .setLive(StringUtils.isBlank(row.getColumnValue("Inactive Reason")))
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
    private EnqueDownloadRequest prepareEnqueStockFileRequest() {

        EnqueDownloadRequest.Refiner refiner = new EnqueDownloadRequest.Refiner();

        EnqueDownloadRequest.InternalState internalState = new EnqueDownloadRequest.InternalState.Builder().setExactValue(new EnqueDownloadRequest.ExactValue.Builder().setValue("ACTIVE").build()).setValueType("EXACT").build();
        refiner.addInternalState(internalState);

        EnqueDownloadRequest enqueDownloadRequest = new EnqueDownloadRequest.Builder().setState("LISTING_UI_GROUP").setRefiner(refiner).setVerticalGroup(new EnqueDownloadRequest.VerticalGroup()).build();
        return enqueDownloadRequest;
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

            String downloadFilePath = getFilePath(CATALOG,fileFormat);

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

    private boolean isAuthTokenExpiryNear(Long authTokenExpiresIn)  {

        Date currentTime = DateUtils.getCurrentTime();
        Date authTokenExpiresInDate = new Date(authTokenExpiresIn) ;
        int numberOfDaysLeftBeforeExpiry = DateUtils.diff(currentTime, authTokenExpiresInDate, DateUtils.Resolution.DAY);
        LOGGER.info("Number Of days are left in auth token expiry : {}",numberOfDaysLeftBeforeExpiry);
        if ( numberOfDaysLeftBeforeExpiry < 1 ){
            LOGGER.info("AuthToken will expire in 1 day refetching new authToken");
            return true;
        }
        else{
            return false;
        }
    }

    private String checkIfStockFileExistAtLocal() {
        String filePathWithCSVFormat = getFilePath(CATALOG,"csv");
        File file = new File(filePathWithCSVFormat);
        if( file.isFile() ) {
            return filePathWithCSVFormat;
        } else {
            String filePathWithXLSFormat = getFilePath(CATALOG,"xls");
            file = new File(filePathWithXLSFormat);
            if ( file.isFile() ){
                return filePathWithXLSFormat;
            }
        }
        return null;
    }
    private void fillAsyncDetails(StockFileDownloadRequestStatusResponse stockFileDownloadRequestStatusResponse,CatalogPreProcessorResponse catalogPreProcessorResponse, boolean isAsyncRun) {

        if ( !isAsyncRun ) {
            CatalogPreProcessorResponse.AsyncDetails asyncDetails = new CatalogPreProcessorResponse.AsyncDetails();
            // after analysing request on flipkart panel, it seems like flipkart process approx 30 listings in 1 sec
            asyncDetails.setNextRunInMs(1000*(stockFileDownloadRequestStatusResponse.getTotalCount() - stockFileDownloadRequestStatusResponse.getProcessed_count())/30);
            asyncDetails.setRetryCount(2);
            catalogPreProcessorResponse.setAsyncDetails(asyncDetails);
        }
        catalogPreProcessorResponse.setReportStatus(CatalogPreProcessorResponse.Status.PROCESSING);

    }

    private boolean validateRow(Row row) {

        if ( row.getColumnValue("Listing Archival") != null
                && ("ARCHIVED").equalsIgnoreCase(row.getColumnValue("Listing Archival"))) {
            LOGGER.info("Skipped row : {}, as either its  Listing Archival is null or has value ARCHIVED",row);
            return false;
        }   else if ( row.getColumnValue("Fulfillment By") != null
                && !sourceCodeToFulfilmentModeList.get(TenantRequestContext.current().getSourceCode()).stream().anyMatch(row.getColumnValue("Fulfillment By")::equalsIgnoreCase)) {
            LOGGER.info("Skipped row : {}, Fulfiment mode not match",row);
            return false;
        }

        if ( FLIPKART_OMNI.equalsIgnoreCase(TenantRequestContext.current().getSourceCode())
                && !SELLER.equalsIgnoreCase(row.getColumnValue("Shipping Provider"))) {
            LOGGER.info("Skipped row : {}, For FLIPKART_OMNI we only fetch listings where SHIPPING_PROVDER : SELLER",row);
            return false;
        }
        return true;
    }

    private String getChannelProductIdBySourceCode(Row row, String sourceCode) {

        switch ( sourceCode ) {
            case FLIPKART_FA:
                return row.getColumnValue("Flipkart Serial Number");
            case FLIPKART_LITE:
                return row.getColumnValue("Seller SKU Id");
            default:
                return row.getColumnValue("Listing ID");
        }

    }

    public String getFilePath(String fileIdentifier, String fileFormat) {
        String currentDate = DateUtils.dateToString(DateUtils.getCurrentTime(),"yyyy-MM-dd");
        switch (fileIdentifier) {
        // For pendency we kept file for 1/2 hour on local machine
        case PENDENCY:
            return TMP_DIR + com.uniware.integrations.utils.StringUtils.join('_', TenantRequestContext.current().getHttpSenderIdentifier(), UUID.randomUUID().toString(), PENDENCY) + ".csv";

        // For catalog we kept file for 3 hour on local machine
        case CATALOG:
            return TMP_DIR + com.uniware.integrations.utils.StringUtils.join('_', TenantRequestContext.current().getTenantCode(), FlipkartRequestContext.current().getSellerId(), currentDate, CATALOG) + "." + fileFormat;

        // For invoice we kept file for 1/2 hour on local machine
        case INVOICE:
            return TMP_DIR + com.uniware.integrations.utils.StringUtils.join('_', TenantRequestContext.current().getHttpSenderIdentifier(), UUID.randomUUID().toString(), INVOICE) + ".pdf";

        // For label we kept file for 1/2 hour on local machine
        case LABEL:
            return TMP_DIR + com.uniware.integrations.utils.StringUtils.join('_', TenantRequestContext.current().getHttpSenderIdentifier(), UUID.randomUUID().toString(), LABEL) + ".pdf";

        // For invoice_label we kept file for 1/2 hour on local machine
        case INVOICE_LABEL:
            return TMP_DIR + com.uniware.integrations.utils.StringUtils.join('_', TenantRequestContext.current().getHttpSenderIdentifier(), UUID.randomUUID().toString(), INVOICE_LABEL) + ".pdf";

        // For current_channel_manifest we kept file for 1/2 hour on local machine
        case CURRENT_CHANNEL_MANIFEST:
            return TMP_DIR + com.uniware.integrations.utils.StringUtils.join('_', TenantRequestContext.current().getHttpSenderIdentifier(), UUID.randomUUID().toString(), CURRENT_CHANNEL_MANIFEST) + ".pdf";

        default:
                LOGGER.error("Invalid File Identifier");
                return null;
        }
    }

}

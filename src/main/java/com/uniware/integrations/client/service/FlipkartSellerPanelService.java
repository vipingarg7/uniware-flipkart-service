package com.uniware.integrations.client.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.unifier.core.transport.http.HttpResponseWrapper;
import com.unifier.core.transport.http.HttpSender;
import com.unifier.core.transport.http.HttpTransportException;
import com.unifier.core.utils.DateUtils;
import com.unifier.core.utils.StringUtils;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.dto.api.requestDto.EnqueDownloadRequest;
import com.uniware.integrations.client.dto.api.requestDto.FetchOnHoldOrderRequest;
import com.uniware.integrations.client.dto.api.responseDto.StockFileDownloadNUploadHistoryResponse;
import com.uniware.integrations.client.dto.api.responseDto.StockFileDownloadRequestStatusResponse;
import com.uniware.integrations.client.dto.uniware.Pendency;
import com.uniware.integrations.core.dto.api.Response;
import com.uniware.integrations.utils.http.HttpSenderFactory;
import com.uniware.integrations.web.context.TenantRequestContext;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class FlipkartSellerPanelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlipkartSellerPanelService.class);

    private static final String SELLER_PANEL_URL = "https://seller.flipkart.com";

    private Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    /*
        Description     - Checking if session is expired or not of expired then try to login on flipkart panel
        Method          - GET
     */
    public boolean sellerPanelLogin(String userName, String password, boolean refreshLogin) {
        if(verifySession()){
            return true;
        }
        String lockKey = TenantRequestContext.current().getHttpSenderIdentifier()+"-"+ userName;
        Lock lock = lockMap.computeIfAbsent(lockKey, key-> new ReentrantLock());
        try {
            lock.lock();
            if(verifySession()){
                return true;
            }
            if (doLogin(userName, password)) {
                return verifySession();
            }
        }finally {
            lock.unlock();
        }
        return false;
    }

    /*
        Description     - Checking if session is logged in or not
        Method          - GET
     */
    public boolean verifySession(){
        Pair<String,String> sellerIdCSRFTokenPair  = getFeaturesForSeller();
        if (sellerIdCSRFTokenPair!=null) {
            LOGGER.info("SellerID:{}, CSRF Token:{}", sellerIdCSRFTokenPair.getFirst(), sellerIdCSRFTokenPair.getSecond());
            FlipkartRequestContext.current().setSellerId(sellerIdCSRFTokenPair.getFirst());
            FlipkartRequestContext.current().getSellerPanelHeaders().put("fk-csrf-token", sellerIdCSRFTokenPair.getSecond());
            return true;
        }
        return false;
    }

    /*
        Description     - Login on fipkart panel with Client credentials ( username, password)
        Method          - POST
     */
    private static boolean doLogin(String username, String password) {
        LOGGER.info("Sending login request");
        try {
            HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
            Map<String, String> loginRequestParameter = new HashMap<>();
            loginRequestParameter.put("username", username);
            loginRequestParameter.put("password", password);
            loginRequestParameter.put("authName", "flipkart");
            HttpSender httpSender = HttpSenderFactory.getHttpSender(FlipkartRequestContext.current().getUserName());
            String loginResponse = httpSender.executePost(SELLER_PANEL_URL + "/login", loginRequestParameter, Collections.EMPTY_MAP, httpResponseWrapper);
            LOGGER.info("Login response : {}", loginResponse);
            if (StringUtils.isBlank(loginResponse)) {
                LOGGER.error("unable to login, blank response received");
                return false;
            }
            if (loginResponse.contains("The Seller were authenticated successfully")) {
                try {
                    JsonObject loginJson = new Gson().fromJson(loginResponse, JsonObject.class);
                    String sellerId = loginJson.get("data").getAsJsonObject().get("sellerId").getAsString();
                    LOGGER.info("User logged in successfully| Seller Id received :{}", sellerId);
                    return true;
                } catch (JsonSyntaxException jse) {
                    LOGGER.error("User not logged in| response {}", loginResponse,jse);
                    return false;
                }
            } else {
                if (loginResponse.contains("Username and password do not match")) {
                    LOGGER.error("Username and password did not match, please verify.");
                    return false;
                }
            }
        } catch (HttpTransportException | JsonSyntaxException e) {
            LOGGER.error("Exception while login", e);
        }
        return false;
    }

    /*
        Description     - Using this Api to check the session on seller panel and to get CSRF token used for scraping
        Method          - GET
        Sample Response - Returns Json containing client details
     */
    public Pair<String, String> getFeaturesForSeller() {
        LOGGER.info("Fetching seller feature page for CSRF token");
        String username = FlipkartRequestContext.current().getUserName();
        HttpSender httpSender = null;
        if ( StringUtils.isNotBlank(username)){
            httpSender = HttpSenderFactory.getHttpSender(username);
        } else {
            httpSender = HttpSenderFactory.getHttpSender();
        }

        try {
            HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
            String sellerFeaturePageResponse = httpSender.executeGet(SELLER_PANEL_URL + "/getFeaturesForSeller", Collections.emptyMap(), Collections.EMPTY_MAP, httpResponseWrapper);
            if (httpResponseWrapper.getResponseStatus().equals(HttpStatus.TOO_MANY_REQUESTS)) {
                LOGGER.error("Got http error for TOO_MANY_REQUESTS, resposne:{} ", sellerFeaturePageResponse);
                return null;
            } else {
                if (StringUtils.isBlank(sellerFeaturePageResponse)) {
                    LOGGER.error("Error in checking login");
                    return null;
                } else {
                    try {
                        JsonObject jsonObject = new Gson().fromJson(sellerFeaturePageResponse, JsonObject.class);
                        String sellerId = jsonObject.get("sellerId").getAsString();
                        String csrfToken = jsonObject.get("csrfToken").getAsString();
                        LOGGER.info("Fetched SellerId: {}, CSRF Token:{}", sellerId, csrfToken);
                        return Pair.of(sellerId,csrfToken);
                    } catch (JsonSyntaxException e) {
                        LOGGER.error("User not logged in| getFeaturesForSeller Json not received response:{}, e.message:{}", sellerFeaturePageResponse, e.getMessage());
                    }
                }
            }
        } catch (HttpTransportException | JsonSyntaxException e) {
            LOGGER.error("Exception while fetching seller feature", e);
        }
        return null;
    }

    /*
        Description     - Fetch orders with onhold status from flipkart
        Method          - GET
     */
    public String getOnHoldOrdersFromPanel(FetchOnHoldOrderRequest fetchOnHoldOrderRequest)  {

        String fetchOnHoldOrderRequestJson = new Gson().toJson(fetchOnHoldOrderRequest);

        HttpSender httpSender = HttpSenderFactory.getHttpSender(FlipkartRequestContext.current().getUserName());
        try {
            HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
            String response = httpSender.executePost(SELLER_PANEL_URL + "/napi/my-orders/fetch",fetchOnHoldOrderRequestJson,FlipkartRequestContext.current()
                    .getSellerPanelHeaders(), httpResponseWrapper );
            return response;
        }
        catch (HttpTransportException | JsonSyntaxException e) {
            LOGGER.error("Something went wrong", e);
        }
        return null;
    }

    /*
        Description - List the last five stock file details requested.
        Method      - GET
        Request     - get request
        Response    - {  "stock_file_response_list": [    {      "file_link": "/operations/downloadFile/?TenantId=spslm&Namespace=spslm_seller_processed_feeds&Id=6f6ac62d56304bf7_listing-ui-group_PRICING_STOCK_default_uploaded_1662437942622",      "file_operation_type": "GENERATED",      "file_name": "S_listing--ui--group_6f6ac62d56304bf7_0609-095129_default.csv",      "error_count": null,      "total_count": null,      "file_format": "csv",      "uploaded_on": 1662437943156,      "feed_state": null,      "error_rows_exists": false    }  ]   }
     */
    public StockFileDownloadNUploadHistoryResponse getStockFileDownloadNUploadHistory() {

        StockFileDownloadNUploadHistoryResponse stockFileDownloadNUploadHistoryResponse;
        String apiEndpoint = "/napi/listing/stockFileDownloadNUploadHistory";

        try {
            HttpSender httpSender = HttpSenderFactory.getHttpSender(FlipkartRequestContext.current().getUserName());
            HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
            String response = httpSender.executeGet(SELLER_PANEL_URL + apiEndpoint, Collections.emptyMap(), FlipkartRequestContext.current()
                    .getSellerPanelHeaders(), httpResponseWrapper);
            stockFileDownloadNUploadHistoryResponse = new Gson().fromJson(response, StockFileDownloadNUploadHistoryResponse.class);
            return stockFileDownloadNUploadHistoryResponse;
        } catch (HttpTransportException | JsonSyntaxException e) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, e);
        }
        return null;
    }

    /*
        Description         - To check the status of requested stock file
        Method              - GET
        Sample Response            - {"sellerId":"6f6ac62d56304bf7","lastDownloadTimestamp":"2022-09-06 09:35:37.0","downloadState":"DOWNLOADING","processed_count":12620,"totalCount":36084}
     */
    public StockFileDownloadRequestStatusResponse getStockFileDownloadRequestStatus() {

        StockFileDownloadRequestStatusResponse stockFileDownloadRequestStatusResponse;
        String apiEndpoint = "napi/listing/stockFileDownloadRequestStatus";

        try {
            HttpSender httpSender = HttpSenderFactory.getHttpSender(FlipkartRequestContext.current().getUserName());
            HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
            String response = httpSender.executeGet(SELLER_PANEL_URL + apiEndpoint, Collections.emptyMap(), FlipkartRequestContext.current()
                    .getSellerPanelHeaders(), httpResponseWrapper);
            stockFileDownloadRequestStatusResponse = new Gson().fromJson(response, StockFileDownloadRequestStatusResponse.class);
            return stockFileDownloadRequestStatusResponse;
        } catch (HttpTransportException | JsonSyntaxException e) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, e);
        }
        return null;
    }

    /*
        Description        - Request a new stock file on seller panel ( path => Seller panel -> My Listings -> Request Download -> Listing File )
        Method             - POST
        Sample Request     - {"state":"LISTING_UI_GROUP","refiners":{"internal_state":[{"exactValue":{"value":"ACTIVE"},"valueType":"EXACT"}]},"verticalGroups":{}}
        Sample Response    - {}
     */
    public boolean requestStockFile() {

        EnqueDownloadRequest.ExactValue exactValue = new EnqueDownloadRequest.ExactValue.Builder().setValue("ACTIVE").build();
        EnqueDownloadRequest.InternalState internalState = new EnqueDownloadRequest.InternalState.Builder().setExactValue(exactValue).build();
        EnqueDownloadRequest.Refiner refiner = new EnqueDownloadRequest.Refiner().addInternalState(internalState);
        EnqueDownloadRequest enqueDownloadRequest = new EnqueDownloadRequest.Builder().setState("LISTING_UI_GROUP").setRefiner(refiner).setVerticalGroup(new EnqueDownloadRequest.VerticalGroup()).build();
        String enqueDownloadRequestJson = new Gson().toJson(enqueDownloadRequest);

        String apiEndpoint = "/napi/listing/enqueueDownload";
        try {
            HttpSender httpSender = HttpSenderFactory.getHttpSender(FlipkartRequestContext.current().getUserName());
            HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
            String response = httpSender.executePost(SELLER_PANEL_URL + apiEndpoint,enqueDownloadRequestJson,FlipkartRequestContext.current()
                    .getSellerPanelHeaders() );
            JsonObject responseJson = new Gson().fromJson(response, JsonObject.class);
            return true;
        } catch (HttpTransportException | JsonSyntaxException e) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, e);
        }
        return false;
    }

    /*
        Description       - Download stock file
        Method            - GET
        Request Params    - SellerId, FileId received in stockFileDownloadAndUpload History request, FileName
        Response          - stock file
     */
    public String downloadStockFile(String fileName, String fileLink, String fileFormat) {

        String currentDate = DateUtils.dateToString(DateUtils.getCurrentTime(),"yyyy-MM-dd");
        String downloadFilePath = "/tmp/" +TenantRequestContext.current().getTenantCode() + "_" + currentDate + "." + fileFormat;
        String apiEndpoint = "/napi/listing/stockFileDownload";
        try {

            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("sellerId", FlipkartRequestContext.current().getSellerId());
            requestParams.put("fileId", URLEncoder.encode(fileLink,"UTF-8") );
            requestParams.put("fileName", fileName);
            HttpSender httpSender = HttpSenderFactory.getHttpSender(FlipkartRequestContext.current().getUserName());
            httpSender.downloadToFile(SELLER_PANEL_URL + apiEndpoint, requestParams, HttpSender.MethodType.GET, downloadFilePath);
            return downloadFilePath;
        } catch (HttpTransportException | JsonSyntaxException e) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, e);
        }
        catch (UnsupportedEncodingException e) {
            LOGGER.error("Getting error while encoding fileLink", e);
        }
        return null;
    }

}

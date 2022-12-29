package com.uniware.integrations.client.service;

import com.google.gson.Gson;
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
import com.uniware.integrations.client.dto.api.requestDto.ListingFilterRequest;
import com.uniware.integrations.client.dto.api.responseDto.SearchShipmentV3Response;
import com.uniware.integrations.client.dto.api.responseDto.StockFileDownloadNUploadHistoryResponse;
import com.uniware.integrations.client.dto.api.responseDto.StockFileDownloadRequestStatusResponse;
import com.uniware.integrations.utils.http.HttpSenderFactory;
import com.uniware.integrations.web.context.TenantRequestContext;
import com.uniware.integrations.web.exception.FailureResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
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
       Description - Checking if session is expired or not of expired then try to login on flipkart panel

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
       Description - Checking if session is logged in or not
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
       Description - Login on fipkart panel with Client credentials ( username, password)
     */
    private boolean doLogin(String username, String password) {
        try {
            HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("username", username);
            requestParams.put("password", password);
            requestParams.put("authName", "flipkart");
            HttpSender httpSender = HttpSenderFactory.getHttpSender(FlipkartRequestContext.current().getUserName());
            String apiEndpoint = "/login";

            String loginResponse = httpSender.executePost(SELLER_PANEL_URL + apiEndpoint, requestParams, Collections.EMPTY_MAP, httpResponseWrapper);
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
        } catch (HttpTransportException ex) {
            LOGGER.error("Exception while login", ex);
        }
        return false;
    }

    /*
       Description - Using this Api to check the session on seller panel and to get CSRF token used for scraping
     */
    public Pair<String, String> getFeaturesForSeller() {

        HttpSender httpSender = HttpSenderFactory.getHttpSender(FlipkartRequestContext.current().getUserName());
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        String apiEndpoint = "/getFeaturesForSeller";

        try {
            String sellerFeaturePageResponse = httpSender.executeGet(SELLER_PANEL_URL + apiEndpoint, Collections.emptyMap(), Collections.EMPTY_MAP, httpResponseWrapper);
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
       Description - Fetch orders with onhold status from flipkart
     */
    public String getOnHoldOrdersFromPanel(FetchOnHoldOrderRequest fetchOnHoldOrderRequest)  {

        String fetchOnHoldOrderRequestJson = new Gson().toJson(fetchOnHoldOrderRequest);
        HttpSender httpSender = HttpSenderFactory.getHttpSender(FlipkartRequestContext.current().getUserName());
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("X-LOCATION-ID",FlipkartRequestContext.current().getLocationId());
        headers.put("fk-csrf-token",FlipkartRequestContext.current().getSellerPanelHeaders().get("fk-csrf-token"));
        String apiEndpoint = "/napi/my-orders/fetch";

        try {
            String response = httpSender.executePost(SELLER_PANEL_URL + apiEndpoint,fetchOnHoldOrderRequestJson, headers, httpResponseWrapper);
            handleResponseCode(response, httpResponseWrapper);
            LOGGER.info("getOnHoldOrdersFromPanel Response : {}", response);
            return response;
        }
        catch (HttpTransportException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
        Description - List the last five stock file details requested.
     */
    public StockFileDownloadNUploadHistoryResponse getStockFileDownloadNUploadHistory() {

        StockFileDownloadNUploadHistoryResponse stockFileDownloadNUploadHistoryResponse;
        HttpSender httpSender = HttpSenderFactory.getHttpSender(FlipkartRequestContext.current().getUserName());
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        String apiEndpoint = "/napi/listing/stockFileDownloadNUploadHistory";

        try {
            String response = httpSender.executeGet(SELLER_PANEL_URL + apiEndpoint, Collections.emptyMap(), FlipkartRequestContext.current().getSellerPanelHeaders(), httpResponseWrapper);
            handleResponseCode(response, httpResponseWrapper);
            stockFileDownloadNUploadHistoryResponse = new Gson().fromJson(response, StockFileDownloadNUploadHistoryResponse.class);
            stockFileDownloadNUploadHistoryResponse.setResponseStatus(httpResponseWrapper.getResponseStatus());
            stockFileDownloadNUploadHistoryResponse.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            return stockFileDownloadNUploadHistoryResponse;

        } catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
        Description - To check the status of requested stock file
    */
    public StockFileDownloadRequestStatusResponse getStockFileDownloadRequestStatus() {

        StockFileDownloadRequestStatusResponse stockFileDownloadRequestStatusResponse;
        HttpSender httpSender = HttpSenderFactory.getHttpSender(FlipkartRequestContext.current().getUserName());
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        String apiEndpoint = "/napi/listing/stockFileDownloadRequestStatus";
        try {

            String response = httpSender.executeGet(SELLER_PANEL_URL + apiEndpoint, Collections.emptyMap(), FlipkartRequestContext.current().getSellerPanelHeaders(), httpResponseWrapper);
            handleResponseCode(response, httpResponseWrapper);
            stockFileDownloadRequestStatusResponse = new Gson().fromJson(response, StockFileDownloadRequestStatusResponse.class);
            stockFileDownloadRequestStatusResponse.setResponseStatus(httpResponseWrapper.getResponseStatus());
            stockFileDownloadRequestStatusResponse.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            return stockFileDownloadRequestStatusResponse;
        } catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
        Description - Request a new stock file on seller panel ( path => Seller panel -> My Listings -> Request Download -> Listing File )
     */
    public boolean requestStockFile(EnqueDownloadRequest enqueDownloadRequest) {

        String enqueDownloadRequestJson = new Gson().toJson(enqueDownloadRequest);
        HttpSender httpSender = HttpSenderFactory.getHttpSender(FlipkartRequestContext.current().getUserName());
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        String apiEndpoint = "/napi/listing/enqueueDownload";

        try {
            String response = httpSender.executePost(SELLER_PANEL_URL + apiEndpoint,enqueDownloadRequestJson,FlipkartRequestContext.current().getSellerPanelHeaders(), httpResponseWrapper );
            if ( response != null && response.contains("File already under download for seller") )
                return true;

            handleResponseCode(httpResponseWrapper);
            JsonObject responseJson = new Gson().fromJson(response, JsonObject.class);
            return true;
        } catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}", apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
        Description - Download stock file   [ path => Seller panel Home page -> MyListings -> Download ]
     */
    public String downloadStockFile(String fileName, String fileLink, String downloadFilePath) {

        HttpSender httpSender = HttpSenderFactory.getHttpSender(FlipkartRequestContext.current().getUserName());
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        String apiEndpoint = "/napi/listing/stockFileDownload";
        try {
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("sellerId", FlipkartRequestContext.current().getSellerId());
            requestParams.put("fileId", URLEncoder.encode(fileLink,"UTF-8") );
            requestParams.put("fileName", fileName);
            httpSender.downloadToFile(SELLER_PANEL_URL + apiEndpoint, requestParams, Collections.emptyMap(), HttpSender.MethodType.GET, downloadFilePath, httpResponseWrapper);
            handleResponseCode(null,httpResponseWrapper);
            return downloadFilePath;
        } catch (HttpTransportException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
        catch (UnsupportedEncodingException e) {
            LOGGER.error("Getting error while encoding fileLink", e);
        }
        return null;
    }
//
//    public void addFilterOnListing(ListingFilterRequest listingFilterRequest) {
//
//        String listingFilterRequestJson = new Gson().toJson(listingFilterRequest);
//
//        HttpSender httpSender = HttpSenderFactory.getHttpSender(FlipkartRequestContext.current().getUserName());
//        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
//        String apiEndpoint = "/napi/listing/listingsDataForStates";
//
//        try {
//            String response = httpSender.executePost(SELLER_PANEL_URL + apiEndpoint, listingFilterRequestJson, FlipkartRequestContext.current().getSellerPanelHeaders(), httpResponseWrapper);
//            if (HttpStatus.OK.name().equalsIgnoreCase(httpResponseWrapper.getResponseStatus().name())){
//                LOGGER.info("Successfully Added the filters on listing page");
//            } else {
//                LOGGER.error("Unable to add filters on listing page, response : {}",response);
//            }
//        } catch (HttpTransportException | JsonSyntaxException ex) {
//            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
//            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
//        }
//    }

    private void handleResponseCode(HttpResponseWrapper httpResponseWrapper) {
        String response = null;
        handleResponseCode(response, httpResponseWrapper);
    }
    private void handleResponseCode(String response, HttpResponseWrapper httpResponseWrapper) {
        HttpStatus status = httpResponseWrapper.getResponseStatus();
        String errorMessage = "Invalid response please contact support team";
        JsonObject responseJson = null;
        boolean isResponseTypeIsJson = true;
        if ( response != null ){
            try {
                responseJson = new Gson().fromJson(response,JsonObject.class);
            } catch (JsonSyntaxException ex) {
                isResponseTypeIsJson = false;
            }
        } else {
            isResponseTypeIsJson = false;
        }

        if ( status.is5xxServerError() ) {
            LOGGER.error("Flipkart Server error : {}, responseCode : {} ",response, httpResponseWrapper.getResponseStatus().name());
            if ( StringUtils.isNotBlank(response) && response.contains("DEPENDENT_SYSTEM_CALL_FAILED")){
                errorMessage = "DEPENDENT_SYSTEM_CALL_FAILED. Please try after sometime";
            }
            else if ( StringUtils.isNotBlank(response) && response.contains("Gateway Time")){
                errorMessage = "Gateway Time-out";
            }
            else{
                errorMessage = "Internal Server Error";
            }

            throw new FailureResponse("Flipkart Server error, " + errorMessage+ " responseCode " + httpResponseWrapper.getResponseStatus().name());
        }
        else if ( status.is4xxClientError()) {
            LOGGER.error("Client Error : {}, responseCode : {} ",response, httpResponseWrapper.getResponseStatus().name());
            if ( HttpStatus.BAD_REQUEST.name().equalsIgnoreCase(status.name()) ){
                errorMessage = "Bad Request";
            }
            else if ( HttpStatus.UNAUTHORIZED.name().equalsIgnoreCase(status.name()) || HttpStatus.FORBIDDEN.name().equalsIgnoreCase(status.name())) {
                errorMessage = isResponseTypeIsJson ? responseJson.get("error_description").getAsString() : errorMessage;
            }
            else if ( HttpStatus.TOO_MANY_REQUESTS.name().equalsIgnoreCase(status.name()) ) {
                errorMessage = "Request throttled Too many requests";
            }
            throw new FailureResponse("Client Error : response " + errorMessage + " responseCode  " + status.name());
        }
    }

}

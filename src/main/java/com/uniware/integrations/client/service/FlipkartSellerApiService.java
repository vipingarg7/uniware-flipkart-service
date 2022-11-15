package com.uniware.integrations.client.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.unifier.core.transport.http.HttpResponseWrapper;
import com.unifier.core.transport.http.HttpSender;
import com.unifier.core.transport.http.HttpTransportException;
import com.unifier.core.utils.PdfUtils;
import com.unifier.core.utils.StringUtils;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.constants.EnvironmentPropertiesConstant;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.dto.api.requestDto.DispatchStandardShipmentV3Request;
import com.uniware.integrations.client.dto.api.requestDto.GetManifestRequest;
import com.uniware.integrations.client.dto.api.requestDto.SearchShipmentRequest;
import com.uniware.integrations.client.dto.api.requestDto.ShipmentDeliveryRequestV3;
import com.uniware.integrations.client.dto.api.requestDto.ShipmentPackV3Request;
import com.uniware.integrations.client.dto.api.requestDto.UpdateInventoryV3Request;
import com.uniware.integrations.client.dto.api.responseDto.AuthTokenResponse;
import com.uniware.integrations.client.dto.api.requestDto.DispatchSelfShipmentV3Request;
import com.uniware.integrations.client.dto.api.responseDto.DispatchShipmentV3Response;
import com.uniware.integrations.client.dto.api.responseDto.InvoiceDetailsV3Response;
import com.uniware.integrations.client.dto.api.responseDto.LocationDetailsResponse;
import com.uniware.integrations.client.dto.api.responseDto.SearchShipmentV3Response;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentDetailsV3WithAddressResponse;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentDetailsV3WithSubPackages;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentPackV3Response;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentsDeliverV3Response;
import com.uniware.integrations.client.dto.api.responseDto.UpdateInventoryV3Response;
import com.uniware.integrations.utils.http.HttpSenderFactory;
import com.uniware.integrations.web.exception.FailureResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class FlipkartSellerApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlipkartSellerApiService.class);

    @Autowired private Environment environment;

    public String getChannelBaseUrl(ChannelSource channelSource) {
        String channelBaseUrl = channelSource.getChannelSourceCode().toLowerCase() + ".baseUrl";
        String baseUrl = environment.getProperty(channelBaseUrl);
        return baseUrl;
    }

    /*
        Description - Fetch all the location details for a seller
     */
    public LocationDetailsResponse getAllLocations() {

        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("cache-control", "no-cache");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/locations/allLocations";

        HttpSender httpSender = HttpSenderFactory.getHttpSender();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        try {
            String response = httpSender.executeGet(channelBaseUrl + apiEndpoint, Collections.emptyMap(), headersMap, httpResponseWrapper);
            handleResponseCode(response, httpResponseWrapper);
            LocationDetailsResponse locationDetailsResponse = new Gson().fromJson(response, LocationDetailsResponse.class);
            return locationDetailsResponse;
        }
        catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }

    }

    /*
        Description - Fetch a new authToken
     */
    public AuthTokenResponse getAuthToken(String fkAuthCode) {

        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("redirect_uri", environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_REDIRECT_URL));
        requestParams.put("code", fkAuthCode);
        requestParams.put("grant_type", "authorization_code");
        requestParams.put("state", "123");

        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        String apiEndpoint = "/oauth-service/oauth/token";

        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        try {
            httpSender.setBasicAuthentication(environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_APPLICATION_ID), environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_APPLICATION_SECRET));
            String response = httpSender.executeGet(channelBaseUrl + apiEndpoint, requestParams, headersMap, httpResponseWrapper);
            handleResponseCode(response, httpResponseWrapper);
            AuthTokenResponse authTokenResponse = new Gson().fromJson(response, AuthTokenResponse.class);
            authTokenResponse.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            authTokenResponse.setResponseStatus(httpResponseWrapper.getResponseStatus());
            return authTokenResponse;
        }
        catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }

    }

    /*
        Description - Fetch a new/refresh auth token with the help of refresh token
     */
    public AuthTokenResponse refreshAuthToken(String refreshToken) {

        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("redirect_uri", environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_REDIRECT_URL));
        requestParams.put("refresh_token", refreshToken);
        requestParams.put("grant_type", "refresh_token");

        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        String apiEndpoint =  "/oauth-service/oauth/token";

        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        try {
            httpSender.setBasicAuthentication(environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_APPLICATION_ID), environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_APPLICATION_SECRET));
            String response = httpSender.executeGet(channelBaseUrl + apiEndpoint, requestParams, headersMap, httpResponseWrapper);
            handleResponseCode(response, httpResponseWrapper);
            AuthTokenResponse authTokenResponse = new Gson().fromJson(response, AuthTokenResponse.class);
            authTokenResponse.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            authTokenResponse.setResponseStatus(httpResponseWrapper.getResponseStatus());
            return authTokenResponse;
        }
        catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
        Description - Fetch all the shipment with its subPackages details on the basis of filters in request payload
     */
    public SearchShipmentV3Response searchPreDispatchShipmentPost(SearchShipmentRequest searchShipmentRequest) {

        String searchShipmentRequestJson = new Gson().toJson(searchShipmentRequest);
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/v3/shipments/filter";

        try {
            String response = httpSender.executePost(channelBaseUrl + apiEndpoint, searchShipmentRequestJson, headersMap, httpResponseWrapper);
            handleResponseCode(response, httpResponseWrapper);
            SearchShipmentV3Response searchShipmentV3Response = new Gson().fromJson(response, SearchShipmentV3Response.class);
            searchShipmentV3Response.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            searchShipmentV3Response.setResponseStatus(httpResponseWrapper.getResponseStatus());
            return searchShipmentV3Response;
        }
        catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
        Description - Fetch the shipments with the help of next page url.
     */
    public SearchShipmentV3Response searchPreDispatchShipmentGet(String nextPageUrl) {

        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers" + nextPageUrl;
        try {
            String response = httpSender.executeGet(channelBaseUrl + apiEndpoint , Collections.emptyMap(), headersMap, httpResponseWrapper);
            handleResponseCode(response, httpResponseWrapper);
            SearchShipmentV3Response searchShipmentV3Response = new Gson().fromJson(response, SearchShipmentV3Response.class);
            searchShipmentV3Response.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            searchShipmentV3Response.setResponseStatus(httpResponseWrapper.getResponseStatus());
            return searchShipmentV3Response;
        }
        catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
    Description - Fetch the address level details of the shipments
     */
    public ShipmentDetailsV3WithAddressResponse getShipmentDetailsWithAddress(String shipmentIds) {

        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/v3/shipments/" + shipmentIds;
        try {
            String response = httpSender.executeGet(channelBaseUrl + apiEndpoint, Collections.emptyMap(), headersMap, httpResponseWrapper);
            handleResponseCode(response, httpResponseWrapper);
            ShipmentDetailsV3WithAddressResponse shipmentDetailsSearchResponse = new Gson().fromJson(response, ShipmentDetailsV3WithAddressResponse.class);
            shipmentDetailsSearchResponse.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            shipmentDetailsSearchResponse.setResponseStatus(httpResponseWrapper.getResponseStatus());
            return shipmentDetailsSearchResponse;
        }
        catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }

    }

    /*
        Description - fetch subPackage level details of shipments
     */
    public ShipmentDetailsV3WithSubPackages getShipmentDetailsWithSubPackages(String shipmentIds) {

        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        Map<String, String> params = new HashMap<>();
        params.put("shipmentIds", shipmentIds);
        String apiEndpoint = "/sellers/v3/shipments";
        try {
            String response = httpSender.executeGet(channelBaseUrl + apiEndpoint, params, headersMap, httpResponseWrapper);
            handleResponseCode(response, httpResponseWrapper);
            ShipmentDetailsV3WithSubPackages shipmentDetailsV3WithSubPackages = new Gson().fromJson(response, ShipmentDetailsV3WithSubPackages.class);
            shipmentDetailsV3WithSubPackages.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            shipmentDetailsV3WithSubPackages.setResponseStatus(httpResponseWrapper.getResponseStatus());
            return shipmentDetailsV3WithSubPackages;
        }
        catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
        Description - Marking self shipments dispatch on flipkart panel.
     */
    public DispatchShipmentV3Response markSelfShipDispatch(DispatchSelfShipmentV3Request dispatchSelfShipmentRequest) {

        String dispatchSelfShipmentRequestJson = new Gson().toJson(dispatchSelfShipmentRequest);
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint =  "/sellers/v3/shipments/selfShip/dispatch";
        try {
            String response = httpSender.executePost(channelBaseUrl + apiEndpoint, dispatchSelfShipmentRequestJson, headersMap, httpResponseWrapper);
            handleResponseCode(response, httpResponseWrapper);
            DispatchShipmentV3Response dispatchShipmentV3Response = new Gson().fromJson(response, DispatchShipmentV3Response.class);
            return dispatchShipmentV3Response;
        }
        catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
        Description - Marking standard shipments dispatch on flipkart panel
     */
    public DispatchShipmentV3Response markStandardFulfilmentShipmentsRTD(DispatchStandardShipmentV3Request dispatchStandardShipmentRequest) {

        String dispatchStandardShipmentRequestJson = new Gson().toJson(dispatchStandardShipmentRequest);
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/v3/shipments/dispatch";
        try {
            String response = httpSender.executePost(channelBaseUrl + apiEndpoint , dispatchStandardShipmentRequestJson, headersMap, httpResponseWrapper);
            handleResponseCode(response, httpResponseWrapper);
            DispatchShipmentV3Response dispatchShipmentV3Response = new Gson().fromJson(response, DispatchShipmentV3Response.class);
            dispatchShipmentV3Response.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            dispatchShipmentV3Response.setResponseStatus(httpResponseWrapper.getResponseStatus());
            return dispatchShipmentV3Response;
        }
        catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
        Description - Marking self shipments deliver of flipkart panel
     */
    public ShipmentsDeliverV3Response markSelfShipmentDeliver(ShipmentDeliveryRequestV3 shipmentDeliveryRequestV3) {

        String shipmentDeliveryRequestV3Json = new Gson().toJson(shipmentDeliveryRequestV3);
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "sellers/v3/shipments/selfShip/delivery";
        try {
            String response = httpSender.executePost(channelBaseUrl + apiEndpoint, shipmentDeliveryRequestV3Json, headersMap, httpResponseWrapper);
            handleResponseCode(response, httpResponseWrapper);
            ShipmentsDeliverV3Response shipmentsDeliverV3Response = new Gson().fromJson(response, ShipmentsDeliverV3Response.class);
            return shipmentsDeliverV3Response;
        }
        catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
        Description - Update shipments status to Packed on flipkart panel
     */
    public ShipmentPackV3Response packShipment(ShipmentPackV3Request shipmentPackRequest) {

        String shipmentPackRequestJson = new Gson().toJson(shipmentPackRequest);
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "sellers/v3/shipments/labels";
        try {
            String response = httpSender.executePost(channelBaseUrl + apiEndpoint, shipmentPackRequestJson, headersMap, httpResponseWrapper);
            handleResponseCode(httpResponseWrapper);
            ShipmentPackV3Response shipmentStatusResponse = new Gson().fromJson(response, ShipmentPackV3Response.class);
            return shipmentStatusResponse;
        } catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
        Description - fetch invoice details for the shipments
     */
    public InvoiceDetailsV3Response getInvoicesInfo(String shipmentIds) {

        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/v3/shipments/" + shipmentIds + "/invoices";
        try {
            String response = httpSender.executeGet(channelBaseUrl + apiEndpoint, Collections.emptyMap(), headersMap, httpResponseWrapper);
            handleResponseCode(response, httpResponseWrapper);
            InvoiceDetailsV3Response invoiceDetailsV3Response = new Gson().fromJson(response, InvoiceDetailsV3Response.class);
            invoiceDetailsV3Response.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            invoiceDetailsV3Response.setResponseStatus(httpResponseWrapper.getResponseStatus());
            return invoiceDetailsV3Response;
        }
        catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
        Description - Downlaod label only for a shipment
     */
    public boolean downloadLabel(String shipmentId, String filePath) {

        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/pdf");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/v3/shipments/"+ shipmentId +"/labelOnly/pdf";
        try {
            httpSender.downloadToFile( channelBaseUrl + apiEndpoint, Collections.emptyMap(), headersMap, HttpSender.MethodType.POST, filePath, httpResponseWrapper);
            handleResponseCode(httpResponseWrapper);
            String parsedLabel = PdfUtils.parsePdf(filePath);
            if (StringUtils.isNotBlank(parsedLabel)) {
                LOGGER.info("Label file downloaded at {}", filePath);
                return true;
            } else {
                LOGGER.info("Label file not available or corrupted file downloaded");
                return false;
            }
        } catch (HttpTransportException | IOException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
        Description - Download invoice only for a shipment
     */
    public String downloadInvoice(String shipmentId, String filePath) {
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/pdf");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/v3/shipments/" + shipmentId + "/invoices";
        try {
            httpSender.downloadToFile( channelBaseUrl + apiEndpoint, Collections.emptyMap(), headersMap, HttpSender.MethodType.GET, filePath, httpResponseWrapper);
            handleResponseCode(httpResponseWrapper);
            String parsedLabel = PdfUtils.parsePdf(filePath);
            if (StringUtils.isNotBlank(parsedLabel)) {
                LOGGER.info("Invoice file downloaded at {}", filePath);
                return filePath;
            } else {
                LOGGER.info("Invoice file not available or corrupted file downloaded");
            }
        } catch (HttpTransportException | IOException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
        return null;
    }

    /*
        Description - Download invoice and label in one a file
     */
    public boolean downloadInvoiceAndLabel(String shipmentId, String filePath) {

        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/pdf");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/v3/shipments/" + shipmentId + "/labels";
        try {
            httpSender.downloadToFile( channelBaseUrl + apiEndpoint, Collections.emptyMap(), headersMap, HttpSender.MethodType.GET, filePath, httpResponseWrapper);
            handleResponseCode(httpResponseWrapper);
            String parsedLabel = PdfUtils.parsePdf(filePath);
            if (StringUtils.isNotBlank(parsedLabel)) {
                LOGGER.info("InvoiceLabel file downloaded at {}", filePath);
                return true;
            } else {
                LOGGER.info("InvoiceLabel file not available");
                return false;
            }
        } catch (HttpTransportException | IOException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
        Description - Update inventory for seller sku's for its location
     */
    public UpdateInventoryV3Response updateInventory(UpdateInventoryV3Request updateInventoryRequest) {

        String UpdateInventoryRequestJson = new Gson().toJson(updateInventoryRequest.getSkus());
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/listings/v3/update/inventory";
        try {
            String response = httpSender.executePost(channelBaseUrl + apiEndpoint, UpdateInventoryRequestJson, headersMap, httpResponseWrapper);
            handleResponseCode(response, httpResponseWrapper);
            Map<String, UpdateInventoryV3Response.InventoryUpdateStatus> updateInventoryStatusMap =  new Gson().fromJson(response, Map.class);
            UpdateInventoryV3Response updateInventoryV3Response = new UpdateInventoryV3Response();
            updateInventoryV3Response.addSku(updateInventoryStatusMap);
            updateInventoryV3Response.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            updateInventoryV3Response.setResponseStatus(httpResponseWrapper.getResponseStatus());
            return updateInventoryV3Response;
        } catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
    }

    /*
        Description - Get the channel manifest for a shipping provider from panel
     */
    public boolean getCurrentChannelManifest(GetManifestRequest getManifestRequest, String filePath) {

        String getManifestRequestJson = new Gson().toJson(getManifestRequest);
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/v3/shipments/manifest";
        try {
            httpSender.downloadToFile( channelBaseUrl + apiEndpoint, getManifestRequestJson, headersMap, HttpSender.MethodType.POST, filePath, httpResponseWrapper);
            handleResponseCode(httpResponseWrapper);
            String parsedManifest = PdfUtils.parsePdf(filePath);
            if (StringUtils.isNotBlank(parsedManifest)) {
                LOGGER.info("Manifest file downloaded at {}", filePath);
                return true;
            } else {
                LOGGER.info("Manifest file not available or corrupted file downloaded");
            }
        } catch (HttpTransportException | IOException ex) {
            LOGGER.error("Something went wrong apiEndpoint {}, Error {}",apiEndpoint, ex);
            throw new FailureResponse("Something went wrong - " + apiEndpoint + " , error : " + ex.getMessage());
        }
        return false;
    }

    private void handleResponseCode(HttpResponseWrapper httpResponseWrapper) {
        String response = null;
        handleResponseCode(response, httpResponseWrapper);
    }
    private void handleResponseCode(String response, HttpResponseWrapper httpResponseWrapper) {
        HttpStatus status = httpResponseWrapper.getResponseStatus();
        String errorMessage = "invalid response please contact support team";
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
            LOGGER.error("Flipkart Server error : {}, responseCode : {} ",response, httpResponseWrapper.getResponseStatus().value());
            if ( StringUtils.isNotBlank(response) && response.contains("DEPENDENT_SYSTEM_CALL_FAILED")){
                errorMessage = "DEPENDENT_SYSTEM_CALL_FAILED. Please try after sometime";
            }
            else if (StringUtils.isNotBlank(response) && response.contains("Gateway Time")){
                errorMessage = "Gateway Time-out";
            }
            else{
                errorMessage = "Internal Server Error";
            }

            throw new FailureResponse("Flipkart Server error : " + errorMessage + ", responseCode : " + httpResponseWrapper.getResponseStatus().value());
        }
        else if ( status.is4xxClientError()) {
            LOGGER.error("Client Error : {}, responseCode : {} ",response, httpResponseWrapper.getResponseStatus().value());
            if ( HttpStatus.BAD_REQUEST.name().equalsIgnoreCase(status.name()) ){
                errorMessage = "Bad Request";
            }
            else if ( HttpStatus.UNAUTHORIZED.name().equalsIgnoreCase(status.name()) || HttpStatus.FORBIDDEN.name().equalsIgnoreCase(status.name())) {
                errorMessage = isResponseTypeIsJson ? responseJson.get("error_description").getAsString() : errorMessage;
            }
            else if ( HttpStatus.TOO_MANY_REQUESTS.name().equalsIgnoreCase(status.name()) ) {
                errorMessage = "Request throttled Too many requests";
            }
            throw new FailureResponse("Client Error : response " + errorMessage + " responseCode : " + status.value());
        }
    }

}

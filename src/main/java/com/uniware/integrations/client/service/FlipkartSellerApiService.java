package com.uniware.integrations.client.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.unifier.core.transport.http.HttpResponseWrapper;
import com.unifier.core.transport.http.HttpSender;
import com.unifier.core.transport.http.HttpTransportException;
import com.unifier.core.utils.DateUtils;
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
import com.uniware.integrations.client.dto.api.requestDto.DispatchSelfShipmentRequestV3;
import com.uniware.integrations.client.dto.api.responseDto.DispatchShipmentV3Response;
import com.uniware.integrations.client.dto.api.responseDto.InvoiceDetailsResponseV3;
import com.uniware.integrations.client.dto.api.responseDto.LocationDetailsResponse;
import com.uniware.integrations.client.dto.api.responseDto.SearchShipmentResponse;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentDetailsWithAddressResponseV3;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentDetailsWithSubPackages;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentPackV3Response;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentsDeliverResponseV3;
import com.uniware.integrations.client.dto.api.responseDto.UpdateInventoryV3Response;
import com.uniware.integrations.utils.http.HttpSenderFactory;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class FlipkartSellerApiService {

    public static enum Family {
        INFORMATIONAL,
        SUCCESSFUL,
        REDIRECTION,
        CLIENT_ERROR,
        SERVER_ERROR,
        OTHER;

        private Family() {
        }

        public static Family familyOf(int statusCode) {
            switch (statusCode / 100) {
            case 1:
                return INFORMATIONAL;
            case 2:
                return SUCCESSFUL;
            case 3:
                return REDIRECTION;
            case 4:
                return CLIENT_ERROR;
            case 5:
                return SERVER_ERROR;
            default:
                return OTHER;
            }
        }
    }

    // Todo
    //  1) add method description

    private static final String FK_CODE        =  "fk_code";
    private static final Logger LOGGER = LoggerFactory.getLogger(FlipkartSellerApiService.class);

    @Autowired private Environment environment;

    public String getChannelBaseUrl(ChannelSource channelSource) {
        String channelBaseUrl = channelSource.getChannelSourceCode().toLowerCase() + ".baseUrl";
        String baseUrl = environment.getProperty(channelBaseUrl);
        return baseUrl;
    }

    public LocationDetailsResponse getAllLocations() {

        LocationDetailsResponse locationDetailsResponse = null;
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("cache-control", "no-cache");
        headersMap.put("Authorization", "authToken");

        HttpSender httpSender = HttpSenderFactory.getHttpSender();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        try {
            String response = httpSender.executeGet(channelBaseUrl + "/sellers/locations/allLocations", Collections.emptyMap(), headersMap, httpResponseWrapper);
            locationDetailsResponse = new Gson().fromJson(response, LocationDetailsResponse.class);
            return locationDetailsResponse;
        }
        catch (HttpTransportException | JsonSyntaxException e) {
            LOGGER.error("Exception while fetchShipmentTrackingDetails", e);
        }

        return locationDetailsResponse;
    }

    public AuthTokenResponse getAuthToken(Map<String, String> headers, String payload) {

        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("redirect_uri", environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_REDIRECT_URL));
        requestParams.put("code", headers.get(FK_CODE));
        requestParams.put("grant_type", "authorization_code");
        requestParams.put("state", "123");

        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        try {
            httpSender.setBasicAuthentication(environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_APPLICATION_ID),
                    environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_APPLICATION_SECRET));
            String response = httpSender.executeGet(channelBaseUrl + "/oauth-service/oauth/token", requestParams,
                    headersMap, httpResponseWrapper);
            AuthTokenResponse authTokenResponseJson = new Gson().fromJson(response, AuthTokenResponse.class);
            return authTokenResponseJson;
        }
        catch (HttpTransportException | JsonSyntaxException e) {
            LOGGER.error("Exception while generating auth token", e);
        }
        return null;
    }

    public AuthTokenResponse refreshAuthToken(String refreshToken) {

        AuthTokenResponse authTokenResponse = null;
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("redirect_uri", environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_REDIRECT_URL));
        requestParams.put("refresh_token", refreshToken);
        requestParams.put("grant_type", "refresh_token");

        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        try {
            httpSender.setBasicAuthentication(environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_APPLICATION_ID),
                    environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_APPLICATION_SECRET));
            String response = httpSender.executeGet(channelBaseUrl + "/oauth-service/oauth/token", requestParams,
                    headersMap, httpResponseWrapper);
            authTokenResponse = new Gson().fromJson(response, AuthTokenResponse.class);
        }
        catch (HttpTransportException | JsonSyntaxException e) {
            LOGGER.error("Exception while fetchShipmentTrackingDetails", e);
        }
        return authTokenResponse;
    }

    public SearchShipmentResponse searchPreDispatchShipmentPost(SearchShipmentRequest searchShipmentRequest)
    {

        SearchShipmentResponse searchShipmentResponse = null;
        String searchShipmentRequestJson = new Gson().toJson(searchShipmentRequest);

        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", "authToken");
        try {
            String response = httpSender.executePost(channelBaseUrl + "/v3/shipments/filter", searchShipmentRequestJson,
                    headersMap, httpResponseWrapper);

            if (httpResponseWrapper.getResponseStatus().is2xxSuccessful()) {
                searchShipmentResponse = new Gson().fromJson(response, SearchShipmentResponse.class);
                LOGGER.info("searchShipmentResponse : " + searchShipmentResponse);
                return searchShipmentResponse;
            }
        }
        catch (Exception e) {
            LOGGER.error("Error occur while fetching pendency", e.getMessage());
        }

        return null;
    }

    public SearchShipmentResponse searchPreDispatchShipmentGet(String nextPageUrl) {
        SearchShipmentResponse searchShipmentResponse = null;
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", "authToken");
        try {
            String response = httpSender.executeGet(channelBaseUrl + "/sellers" + nextPageUrl, Collections.emptyMap(),
                    headersMap, httpResponseWrapper);
            searchShipmentResponse = new Gson().fromJson(response, SearchShipmentResponse.class);
            LOGGER.info("searchShipmentResponse : " + searchShipmentResponse);
            return searchShipmentResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    Description - Api give shipment details with its address details
     */
    public ShipmentDetailsWithAddressResponseV3 getShipmentDetailsWithAddress(String shipmentIds) {

        ShipmentDetailsWithAddressResponseV3 shipmentDetailsSearchResponse = null;
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", "authToken");
        try {
            String response = httpSender.executeGet(channelBaseUrl + "/v3/shipments/" + shipmentIds, Collections.emptyMap(), headersMap, httpResponseWrapper);
            shipmentDetailsSearchResponse = new Gson().fromJson(response, ShipmentDetailsWithAddressResponseV3.class);
            LOGGER.info("shipmentDetailsSearchResponse : " + shipmentDetailsSearchResponse);
            return shipmentDetailsSearchResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ShipmentDetailsWithSubPackages getShipmentDetailsWithSubPackages(String shipmentIds) {

        ShipmentDetailsWithSubPackages shipmentDetailsWithSubPackages = null;
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", "authToken");
        try {
            String response = httpSender.executeGet(channelBaseUrl + "/v3/shipments?" + shipmentIds, Collections.emptyMap(), headersMap, httpResponseWrapper);
            shipmentDetailsWithSubPackages = new Gson().fromJson(response, ShipmentDetailsWithSubPackages.class);
            LOGGER.info("shipmentDetailsSearchResponse : " + shipmentDetailsWithSubPackages);
            return shipmentDetailsWithSubPackages;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DispatchShipmentV3Response markSelfShipDispatch(DispatchSelfShipmentRequestV3 dispatchSelfShipmentRequest) {

        String dispatchSelfShipmentRequestJson = new Gson().toJson(dispatchSelfShipmentRequest);

        DispatchShipmentV3Response dispatchShipmentV3Response = null;
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", "authToken");
        try {
            String response = httpSender.executePost(channelBaseUrl + "/sellers/v3/shipments/selfShip/dispatch",
                    dispatchSelfShipmentRequestJson, headersMap, httpResponseWrapper);
            dispatchShipmentV3Response = new Gson().fromJson(response, DispatchShipmentV3Response.class);
            LOGGER.info("DispatchShipmentV3Response : " + dispatchShipmentV3Response);
            return dispatchShipmentV3Response;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DispatchShipmentV3Response markStandardFulfilmentShipmentsRTD(DispatchStandardShipmentV3Request dispatchStandardShipmentRequest) {

        String dispatchStandardShipmentRequestJson = new Gson().toJson(dispatchStandardShipmentRequest);

        DispatchShipmentV3Response dispatchShipmentV3Response = null;
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", "authToken");
        try {
            String response = httpSender.executePost(channelBaseUrl + "/sellers/v3/shipments/dispatch",
                    dispatchStandardShipmentRequestJson, headersMap, httpResponseWrapper);
            dispatchShipmentV3Response = new Gson().fromJson(response, DispatchShipmentV3Response.class);
            LOGGER.info("dispatchShipmentV3Response : " + dispatchShipmentV3Response);
            return dispatchShipmentV3Response;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ShipmentsDeliverResponseV3 markSelfShipmentDeliver(ShipmentDeliveryRequestV3 shipmentDeliveryRequestV3) {
        String shipmentDeliveryRequestV3Json = new Gson().toJson(shipmentDeliveryRequestV3);

        ShipmentsDeliverResponseV3 shipmentsDeliverResponseV3;
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", "authToken");
        try {
            String response = httpSender.executePost(channelBaseUrl + "sellers/v3/shipments/selfShip/delivery",
                    shipmentDeliveryRequestV3Json, headersMap, httpResponseWrapper);
            shipmentsDeliverResponseV3 = new Gson().fromJson(response, ShipmentsDeliverResponseV3.class);
            LOGGER.info("shipmentsDeliverResponseV3 : " + shipmentsDeliverResponseV3);
            return shipmentsDeliverResponseV3;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    public ShipmentPackV3Response packShipment(ShipmentPackV3Request shipmentPackRequest) {

        String shipmentPackRequestJson = new Gson().toJson(shipmentPackRequest);

        ShipmentPackV3Response shipmentStatusResponse = null;
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", "authToken");
        try {
            String response = httpSender.executePost(channelBaseUrl + "sellers/v3/shipments/labels",
                    shipmentPackRequestJson, headersMap, httpResponseWrapper);
            shipmentStatusResponse = new Gson().fromJson(response, ShipmentPackV3Response.class);
            LOGGER.info("shipmentStatusResponse : " + shipmentStatusResponse);
            return shipmentStatusResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public InvoiceDetailsResponseV3 getInvoicesInfo(String shipmentIds) {
        InvoiceDetailsResponseV3 invoiceDetailsResponseV3 = null;
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", "authToken");
        try {
            String response = httpSender.executeGet(channelBaseUrl + "/v3/shipments/" + shipmentIds + "invoices",
                    Collections.emptyMap(), headersMap, httpResponseWrapper);
            invoiceDetailsResponseV3 = new Gson().fromJson(response, InvoiceDetailsResponseV3.class);
            LOGGER.info("invoiceDetailsResponseV3 : " + invoiceDetailsResponseV3);
            return invoiceDetailsResponseV3;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean downloadLabel(String shipmentId, String filePath) {
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/pdf");
        headersMap.put("Authorization", "authToken");
        try {
            httpSender.downloadToFile( channelBaseUrl + "/" + shipmentId + "/labelOnly/pdf", Collections.emptyMap(), headersMap, HttpSender.MethodType.POST, filePath, httpResponseWrapper);
            String parsedLabel = PdfUtils.parsePdf(filePath);
            if (StringUtils.isNotBlank(parsedLabel)) {
                LOGGER.info("Label file downloaded at {}", filePath);
                return true;
            } else {
                LOGGER.info("Label file not available");
                return false;
            }
        } catch (HttpTransportException | JsonSyntaxException | IOException e) {
            LOGGER.error("Exception while print label", e);
        }
        return false;
    }

    public String downloadInvoice(String shipmentId, String filePath) {
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/pdf");
        headersMap.put("Authorization", "authToken");
        try {
            httpSender.downloadToFile( channelBaseUrl + "/" + shipmentId + "/invoices", Collections.emptyMap(), headersMap, HttpSender.MethodType.GET, filePath, httpResponseWrapper);
            String parsedLabel = PdfUtils.parsePdf(filePath);
            if (StringUtils.isNotBlank(parsedLabel)) {
                LOGGER.info("Invoice file downloaded at {}", filePath);
                return filePath;
            } else {
                LOGGER.info("Invoice file not available");
            }
        } catch (HttpTransportException | JsonSyntaxException | IOException e) {
            LOGGER.error("Exception while print invoice", e);
        }
        return null;
    }

    public boolean downloadInvoiceAndLabel(String shipmentId, String filePath) {
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/pdf");
        headersMap.put("Authorization", "authToken");
        try {
            httpSender.downloadToFile( channelBaseUrl + "/" + shipmentId + "/labels", Collections.emptyMap(), headersMap, HttpSender.MethodType.GET, filePath, httpResponseWrapper);
            String parsedLabel = PdfUtils.parsePdf(filePath);
            if (StringUtils.isNotBlank(parsedLabel)) {
                LOGGER.info("InvoiceLabel file downloaded at {}", filePath);
                return true;
            } else {
                LOGGER.info("InvoiceLabel file not available");
                return false;
            }
        } catch (HttpTransportException | JsonSyntaxException | IOException e) {
            LOGGER.error("Exception while print InvoiceLabel", e);
        }
        return false;
    }

    public UpdateInventoryV3Response updateInventory(UpdateInventoryV3Request updateInventoryRequest) {

        String UpdateInventoryRequestJson = new Gson().toJson(updateInventoryRequest);

        UpdateInventoryV3Response updateInventoryV3Response;
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", "authToken");
        try {
            String response = httpSender.executePost(channelBaseUrl + "/sellers/listings/v3/update/inventory",
                    UpdateInventoryRequestJson, headersMap, httpResponseWrapper);
            updateInventoryV3Response = new Gson().fromJson(response, UpdateInventoryV3Response.class);
            updateInventoryV3Response.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            updateInventoryV3Response.setResponseStatus(httpResponseWrapper.getResponseStatus());
            LOGGER.info("updateInventoryV3Response : " + updateInventoryV3Response);
            return updateInventoryV3Response;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean getCurrentChannelManifest(GetManifestRequest getManifestRequest, String filePath) {

        String getManifestRequestJson = new Gson().toJson(getManifestRequest);

        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        String apiEndpoint = "/sellers/v3/shipments/manifest";
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/pdf");
        headersMap.put("Authorization", "authToken");
        try {
            httpSender.downloadToFile( channelBaseUrl + apiEndpoint, getManifestRequestJson, headersMap, HttpSender.MethodType.POST, filePath, httpResponseWrapper);
            String parsedManifest = PdfUtils.parsePdf(filePath);
            if (StringUtils.isNotBlank(parsedManifest)) {
                LOGGER.info("Manifest file downloaded at {}", filePath);
                return true;
            } else {
                LOGGER.info("Manifest file not available");
            }
        } catch (HttpTransportException | JsonSyntaxException | IOException e) {
            LOGGER.error("Exception while print manifest", e);
        }

        return false;
    }

}

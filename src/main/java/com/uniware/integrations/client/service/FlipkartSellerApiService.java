package com.uniware.integrations.client.service;

import com.google.gson.Gson;
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
import com.uniware.integrations.client.dto.api.requestDto.DispatchSelfShipmentRequestV3;
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
import org.springframework.stereotype.Service;

@Service
public class FlipkartSellerApiService {

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
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/locations/allLocations";

        HttpSender httpSender = HttpSenderFactory.getHttpSender();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        try {
            String response = httpSender.executeGet(channelBaseUrl + apiEndpoint, Collections.emptyMap(), headersMap, httpResponseWrapper);

            if ( httpResponseWrapper.getResponseStatus().is5xxServerError() )
                throw new FailureResponse("Flipkart Server error :" + response + "responseCode " + httpResponseWrapper.getResponseStatus().name());
            if ( httpResponseWrapper.getResponseStatus().is4xxClientError())
            throw new FailureResponse("Client Error : response " + response + " responseCode " + httpResponseWrapper.getResponseStatus().name());

            locationDetailsResponse = new Gson().fromJson(response, LocationDetailsResponse.class);
            return locationDetailsResponse;
        }
        catch (HttpTransportException ex ) {
            LOGGER.error("Getting error while fetching LocationDetails, apiEndpoint : {}, exception : {}",apiEndpoint, ex);
            throw  new FailureResponse("Getting error  while fetching LocationDetails" + ex.getMessage());
        } catch ( JsonSyntaxException ex ) {
            LOGGER.error("Getting error  while parsing LocationDetails, apiEndpoint : {}, exception : {}",apiEndpoint, ex);
            throw  new FailureResponse("Getting error  while parsing LocationDetails" + ex.getMessage());
        }

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
        String apiEndpoint = "/oauth-service/oauth/token";

        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        try {
            httpSender.setBasicAuthentication(environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_APPLICATION_ID), environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_APPLICATION_SECRET));
            String response = httpSender.executeGet(channelBaseUrl + apiEndpoint, requestParams, headersMap, httpResponseWrapper);
            if ( httpResponseWrapper.getResponseStatus().is5xxServerError() )
                throw new FailureResponse("Flipkart Server error :" + response + "responseCode " + httpResponseWrapper.getResponseStatus().name());
            if ( httpResponseWrapper.getResponseStatus().is4xxClientError())
                throw new FailureResponse("Client Error : response " + response + " responseCode " + httpResponseWrapper.getResponseStatus().name());

            AuthTokenResponse authTokenResponseJson = new Gson().fromJson(response, AuthTokenResponse.class);
            return authTokenResponseJson;
        }
        catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Error while generating auth token, apiEndpoint : {}, exception : {}",apiEndpoint, ex);
            throw new FailureResponse("Error while generating auth token"+ ex.getMessage());
        }

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
        String apiEndpoint =  "/oauth-service/oauth/token";

        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        try {
            httpSender.setBasicAuthentication(environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_APPLICATION_ID),
                    environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_APPLICATION_SECRET));
            String response = httpSender.executeGet(channelBaseUrl + apiEndpoint, requestParams, headersMap, httpResponseWrapper);

            if ( httpResponseWrapper.getResponseStatus().is5xxServerError() )
                throw new FailureResponse("Flipkart Server error :" + response + "responseCode " + httpResponseWrapper.getResponseStatus().name());
            if ( httpResponseWrapper.getResponseStatus().is4xxClientError())
                throw new FailureResponse("Client Error : response " + response + " responseCode " + httpResponseWrapper.getResponseStatus().name());

            authTokenResponse = new Gson().fromJson(response, AuthTokenResponse.class);
        }
        catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Getting Error while generating AuthToken, apiEndpoint : {}, exception : {}",apiEndpoint, ex);
            throw new FailureResponse("Getting Error while generating AuthToken" + ex.getMessage());
        }
        return authTokenResponse;
    }

    public SearchShipmentV3Response searchPreDispatchShipmentPost(SearchShipmentRequest searchShipmentRequest) {

        SearchShipmentV3Response searchShipmentV3Response;
        String searchShipmentRequestJson = new Gson().toJson(searchShipmentRequest);

        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/v3/shipments/filter";

        try {
            String response = httpSender.executePost(getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource()) + apiEndpoint, searchShipmentRequestJson,
                    headersMap, httpResponseWrapper);

            if ( httpResponseWrapper.getResponseStatus().is5xxServerError() )
                throw new FailureResponse("Flipkart Server error :" + response + "responseCode " + httpResponseWrapper.getResponseStatus().name());
            if ( httpResponseWrapper.getResponseStatus().is4xxClientError())
                throw new FailureResponse("Client Error : response " + response + " responseCode " + httpResponseWrapper.getResponseStatus().name());

            searchShipmentV3Response = new Gson().fromJson(response, SearchShipmentV3Response.class);
            searchShipmentV3Response.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            searchShipmentV3Response.setResponseStatus(httpResponseWrapper.getResponseStatus());
            return searchShipmentV3Response;
        }
        catch (Exception e) {
            LOGGER.error("Error occur while fetching shipments apiEndpoint : {}, exception {}, ",apiEndpoint, e);
            throw new FailureResponse("Error occur while fetching shipments exception : " + e.getMessage());
        }
    }

    public SearchShipmentV3Response searchPreDispatchShipmentGet(String nextPageUrl) {

        SearchShipmentV3Response searchShipmentV3Response = new SearchShipmentV3Response();
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers" + nextPageUrl;
        try {
            String response = httpSender.executeGet(getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource()) + apiEndpoint , Collections.emptyMap(),
                    headersMap, httpResponseWrapper);

            if ( httpResponseWrapper.getResponseStatus().is5xxServerError() )
                throw new FailureResponse("Flipkart Server error :" + response + "responseCode " + httpResponseWrapper.getResponseStatus().name());
            if ( httpResponseWrapper.getResponseStatus().is4xxClientError())
                throw new FailureResponse("Client Error : response " + response + " responseCode " + httpResponseWrapper.getResponseStatus().name());

            searchShipmentV3Response = new Gson().fromJson(response, SearchShipmentV3Response.class);
            searchShipmentV3Response.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            searchShipmentV3Response.setResponseStatus(httpResponseWrapper.getResponseStatus());
            return searchShipmentV3Response;
        }
        catch (Exception e) {
            LOGGER.error("Error occur while fetching shipments apiEndpoint : {}, exception {}, ",apiEndpoint, e);
            throw new FailureResponse("Error occur while fetching shipments exception {}" + e.getMessage());
        }
    }

    /*
    Description - Api give shipment details with its address details
     */
    public ShipmentDetailsV3WithAddressResponse getShipmentDetailsWithAddress(String shipmentIds) {

        ShipmentDetailsV3WithAddressResponse shipmentDetailsSearchResponse = new ShipmentDetailsV3WithAddressResponse();
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/v3/shipments/";
        try {
            String response = httpSender.executeGet(getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource()) + apiEndpoint + shipmentIds, Collections.emptyMap(), headersMap, httpResponseWrapper);

            if ( httpResponseWrapper.getResponseStatus().is5xxServerError() )
                throw new FailureResponse("Flipkart Server error :" + response + "responseCode " + httpResponseWrapper.getResponseStatus().name());
            if ( httpResponseWrapper.getResponseStatus().is4xxClientError())
                throw new FailureResponse("Client Error : response " + response + " responseCode " + httpResponseWrapper.getResponseStatus().name());

            shipmentDetailsSearchResponse = new Gson().fromJson(response, ShipmentDetailsV3WithAddressResponse.class);
            shipmentDetailsSearchResponse.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            shipmentDetailsSearchResponse.setResponseStatus(httpResponseWrapper.getResponseStatus());
            return shipmentDetailsSearchResponse;
        }
        catch (Exception e) {
            LOGGER.error("Error occur while fetching shipments details apiEndpoint : {}, exception {}, ",apiEndpoint, e);
            throw new FailureResponse("Error occur while fetching shipments details exception {}" + e.getMessage());
        }

    }

    public ShipmentDetailsV3WithSubPackages getShipmentDetailsWithSubPackages(String shipmentIds) {

        ShipmentDetailsV3WithSubPackages shipmentDetailsV3WithSubPackages;
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
            if ( httpResponseWrapper.getResponseStatus().is5xxServerError() )
                throw new FailureResponse("Flipkart Server error :" + response + "responseCode " + httpResponseWrapper.getResponseStatus().name());
            if ( httpResponseWrapper.getResponseStatus().is4xxClientError())
                throw new FailureResponse("Client Error : response " + response + " responseCode " + httpResponseWrapper.getResponseStatus().name());

            shipmentDetailsV3WithSubPackages = new Gson().fromJson(response, ShipmentDetailsV3WithSubPackages.class);
            shipmentDetailsV3WithSubPackages.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            shipmentDetailsV3WithSubPackages.setResponseStatus(httpResponseWrapper.getResponseStatus());
            return shipmentDetailsV3WithSubPackages;
        }
        catch (Exception e) {
            LOGGER.error("Error occur while fetching shipments details apiEndpoint : {}, exception {}, ",apiEndpoint, e);
            throw new FailureResponse("Error occur while fetching shipments details exception {}" + e.getMessage());
        }

    }

    public DispatchShipmentV3Response markSelfShipDispatch(DispatchSelfShipmentRequestV3 dispatchSelfShipmentRequest) {

        String dispatchSelfShipmentRequestJson = new Gson().toJson(dispatchSelfShipmentRequest);

        DispatchShipmentV3Response dispatchShipmentV3Response = null;
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint =  "/sellers/v3/shipments/selfShip/dispatch";
        try {
            String response = httpSender.executePost(channelBaseUrl + apiEndpoint, dispatchSelfShipmentRequestJson, headersMap, httpResponseWrapper);
            if ( httpResponseWrapper.getResponseStatus().is5xxServerError() )
                throw new FailureResponse("Flipkart Server error :" + response + "responseCode " + httpResponseWrapper.getResponseStatus().name());
            if ( httpResponseWrapper.getResponseStatus().is4xxClientError())
                throw new FailureResponse("Client Error : response " + response + " responseCode " + httpResponseWrapper.getResponseStatus().name());

            dispatchShipmentV3Response = new Gson().fromJson(response, DispatchShipmentV3Response.class);
            return dispatchShipmentV3Response;
        }
        catch (Exception e) {
            LOGGER.error("Error occur while marking shipment dispatch : {}, exception {}, ",apiEndpoint, e);
            throw new FailureResponse("Error occur while marking shipment dispatch exception {}" + e.getMessage());
        }
    }

    public DispatchShipmentV3Response markStandardFulfilmentShipmentsRTD(DispatchStandardShipmentV3Request dispatchStandardShipmentRequest) {

        String dispatchStandardShipmentRequestJson = new Gson().toJson(dispatchStandardShipmentRequest);

        DispatchShipmentV3Response dispatchShipmentV3Response = null;
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/v3/shipments/dispatch";
        try {
            String response = httpSender.executePost(channelBaseUrl + apiEndpoint , dispatchStandardShipmentRequestJson, headersMap, httpResponseWrapper);
            if ( httpResponseWrapper.getResponseStatus().is5xxServerError() )
                throw new FailureResponse("Flipkart Server error :" + response + "responseCode " + httpResponseWrapper.getResponseStatus().name());
            if ( httpResponseWrapper.getResponseStatus().is4xxClientError())
                throw new FailureResponse("Client Error : response " + response + " responseCode " + httpResponseWrapper.getResponseStatus().name());

            dispatchShipmentV3Response = new Gson().fromJson(response, DispatchShipmentV3Response.class);
            return dispatchShipmentV3Response;
        }
        catch (Exception e) {
            LOGGER.error("Error occur while marking shipment dispatch : {}, exception {}, ",apiEndpoint, e);
            throw new FailureResponse("Error occur while marking shipment dispatch exception {}" + e.getMessage());
        }
    }

    public ShipmentsDeliverV3Response markSelfShipmentDeliver(ShipmentDeliveryRequestV3 shipmentDeliveryRequestV3) {
        String shipmentDeliveryRequestV3Json = new Gson().toJson(shipmentDeliveryRequestV3);

        ShipmentsDeliverV3Response shipmentsDeliverV3Response;
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        try {
            String response = httpSender.executePost(channelBaseUrl + "sellers/v3/shipments/selfShip/delivery",
                    shipmentDeliveryRequestV3Json, headersMap, httpResponseWrapper);
            if ( httpResponseWrapper.getResponseStatus().is5xxServerError() )
                throw new FailureResponse("Flipkart Server error :" + response + "responseCode " + httpResponseWrapper.getResponseStatus().name());
            if ( httpResponseWrapper.getResponseStatus().is4xxClientError())
                throw new FailureResponse("Client Error : response " + response + " responseCode " + httpResponseWrapper.getResponseStatus().name());
            shipmentsDeliverV3Response = new Gson().fromJson(response, ShipmentsDeliverV3Response.class);
            LOGGER.info("shipmentsDeliverV3Response : " + shipmentsDeliverV3Response);
            return shipmentsDeliverV3Response;
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
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        try {
            String response = httpSender.executePost(channelBaseUrl + "sellers/v3/shipments/labels",
                    shipmentPackRequestJson, headersMap, httpResponseWrapper);
            if ( httpResponseWrapper.getResponseStatus().is5xxServerError() )
                throw new FailureResponse("Flipkart Server error :" + response + "responseCode " + httpResponseWrapper.getResponseStatus().name());
            if ( httpResponseWrapper.getResponseStatus().is4xxClientError())
                throw new FailureResponse("Client Error : response " + response + " responseCode " + httpResponseWrapper.getResponseStatus().name());
            shipmentStatusResponse = new Gson().fromJson(response, ShipmentPackV3Response.class);
            LOGGER.info("shipmentStatusResponse : " + shipmentStatusResponse);
            return shipmentStatusResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public InvoiceDetailsV3Response getInvoicesInfo(String shipmentIds) {
        InvoiceDetailsV3Response invoiceDetailsV3Response = null;
        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource());
        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/v3/shipments/" + shipmentIds + "/invoices";
        try {
            String response = httpSender.executeGet(channelBaseUrl + apiEndpoint, Collections.emptyMap(), headersMap, httpResponseWrapper);
            if ( httpResponseWrapper.getResponseStatus().is5xxServerError() )
                throw new FailureResponse("Flipkart Server error :" + response + "responseCode " + httpResponseWrapper.getResponseStatus().name());
            if ( httpResponseWrapper.getResponseStatus().is4xxClientError())
                throw new FailureResponse("Client Error : response " + response + " responseCode " + httpResponseWrapper.getResponseStatus().name());

            invoiceDetailsV3Response = new Gson().fromJson(response, InvoiceDetailsV3Response.class);
            invoiceDetailsV3Response.setResponseHeaders(httpResponseWrapper.getAllHeaders());
            invoiceDetailsV3Response.setResponseStatus(httpResponseWrapper.getResponseStatus());
            return invoiceDetailsV3Response;
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
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
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
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
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
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        String apiEndpoint = "/sellers/v3/shipments/" + shipmentId + "/labels";
        try {
            httpSender.downloadToFile( channelBaseUrl + apiEndpoint, Collections.emptyMap(), headersMap, HttpSender.MethodType.GET, filePath, httpResponseWrapper);
            if ( httpResponseWrapper.getResponseStatus().is5xxServerError() )
                throw new FailureResponse("Flipkart Server error :" + "responseCode " + httpResponseWrapper.getResponseStatus().name());
            if ( httpResponseWrapper.getResponseStatus().is4xxClientError())
                throw new FailureResponse("Client Error : response " + " responseCode " + httpResponseWrapper.getResponseStatus().name());
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
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        try {
            String response = httpSender.executePost(channelBaseUrl + "/sellers/listings/v3/update/inventory",
                    UpdateInventoryRequestJson, headersMap, httpResponseWrapper);

            if ( httpResponseWrapper.getResponseStatus().is5xxServerError() )
                throw new FailureResponse("Flipkart Server error :" + response + "responseCode " + httpResponseWrapper.getResponseStatus().name());
            if ( httpResponseWrapper.getResponseStatus().is4xxClientError())
                throw new FailureResponse("Client Error : response " + response + " responseCode " + httpResponseWrapper.getResponseStatus().name());

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
        headersMap.put("Authorization", FlipkartRequestContext.current().getAuthToken());
        try {
            httpSender.downloadToFile( channelBaseUrl + apiEndpoint, getManifestRequestJson, headersMap, HttpSender.MethodType.POST, filePath, httpResponseWrapper);
            if ( httpResponseWrapper.getResponseStatus().is5xxServerError() )
                throw new FailureResponse("Flipkart Server error :" + "responseCode " + httpResponseWrapper.getResponseStatus().name());
            if ( httpResponseWrapper.getResponseStatus().is4xxClientError())
                throw new FailureResponse("Client Error : response " + " responseCode " + httpResponseWrapper.getResponseStatus().name());
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

package com.uniware.integrations.client.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.unifier.core.transport.http.HttpSender;
import com.unifier.core.transport.http.HttpTransportException;
import com.uniware.integrations.client.annotation.FlipkartClient;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.constants.EnvironmentPropertiesConstant;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.dto.AuthTokenResponse;
import com.uniware.integrations.core.dto.api.Response;
import com.uniware.integrations.client.dto.PreConfigurationResponse;
import com.uniware.integrations.utils.ResponseUtil;
import com.uniware.integrations.utils.http.HttpSenderFactory;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


/**
 * Created by vipin on 20/05/22.
 */

@Service(value = "flipkartDropshipServiceImpl")
@FlipkartClient(version = "v1",channelSource = { ChannelSource.FLIPKART_DROPSHIP, ChannelSource.FLIPKART_WHOLESALE})
public class FlipkartDropshipServiceImpl extends AbstractSalesFlipkartService {

    private enum OrderStatus {
        APPROVED(1),
        HOLD(2),
        UNHOLD(3),
        PACKING_IN_PROGRESS(4),
        PACKED(5),
        READY_TO_DISPATCH(6),
        CANCELLED(7),
        SHIPPED(8),
        DELIVERED(9);

        private int precedence;

        OrderStatus(int precedence) {
            this.precedence = precedence;
        }

        public static OrderStatus findByOrderStatusValue(String value) {
            OrderStatus result = null;
            for (OrderStatus orderStatus : values()) {
                if (value.equalsIgnoreCase(orderStatus.name())) {
                    result = orderStatus;
                    break;
                }
            }
            return result;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(FlipkartDropshipServiceImpl.class);

    @Autowired
    private Environment environment;

    public String getChannelBaseUrl(String sourceCode) {
        ChannelSource channelSource = ChannelSource.findByChannelSourceCode(sourceCode);
        String baseUrl = environment.getProperty(channelSource.getChannelSourceCode() + "_BASE_URL");
        return baseUrl;
    }


    @Override
    public Response preConfiguration(Map<String,String> headers, String payload, String connectorName) {
        PreConfigurationResponse channelPreConfigurationResponse = new PreConfigurationResponse();
        HashMap<String,String> params = new HashMap<>();
        params.put("client_id",environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_APPLICATION_ID));
        params.put("response_type","code");
        params.put("scope",environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_SCOPE));
        params.put("state",headers.get("state"));
        params.put("redirect_uri",environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_REDIRECT_URL));
        channelPreConfigurationResponse.setUrl(getChannelBaseUrl(headers.get("ChannelSource")));
        channelPreConfigurationResponse.setMethod("GET");
        channelPreConfigurationResponse.setParams(params);

        return ResponseUtil.success("SUCCESS",channelPreConfigurationResponse);
    }

    @Override
    public Response postConfiguration(Map<String,String> headers, String payload, String connectorName) {

        String channelBaseUrl = getChannelBaseUrl(FlipkartRequestContext.current().getChannelSource().name());
        HashMap<String,String> requestParams = new HashMap<>();
        requestParams.put("redirect_uri",environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_REDIRECT_URL));
        requestParams.put("code","");
        requestParams.put("grant_type","authorization_code");
        requestParams.put("state","123");

        HashMap<String,String> headersMap = new HashMap<>();
        headersMap.put("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        HttpSender httpSender = HttpSenderFactory.getHttpSender();
        try {
            httpSender.setBasicAuthentication(environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_APPLICATION_ID),environment.getProperty(EnvironmentPropertiesConstant.FLIPKART_DROPSHIP_APPLICATION_SECRET));
            String response = httpSender.executeGet(channelBaseUrl + "/oauth-service/oauth/token", requestParams, headersMap);
            AuthTokenResponse authTokenResponse = new Gson().fromJson(response, AuthTokenResponse.class);
            String authToken = authTokenResponse.getTokenType() + " " + authTokenResponse.getAccessToken();
            String authTokenExpireIn = authTokenResponse.getExpiresIn();
            String refreshToken = authTokenResponse.getRefreshToken();

            HashMap<String,String> responseParam = new HashMap<>();
            responseParam.put("authToken",authToken);
            responseParam.put("refreshToken",refreshToken);
            responseParam.put("authTokenExpiresIn",authTokenExpireIn);
            return ResponseUtil.success("SUCCESS",responseParam);

        } catch (HttpTransportException | JsonSyntaxException e) {
            LOGGER.error("Exception while fetchShipmentTrackingDetails", e);
        }
        return ResponseUtil.failure("Unable to generate AuthToken");
    }


}

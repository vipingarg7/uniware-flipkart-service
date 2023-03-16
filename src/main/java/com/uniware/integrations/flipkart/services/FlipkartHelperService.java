package com.uniware.integrations.flipkart.services;

import com.google.gson.JsonSyntaxException;
import com.unicommerce.platform.aws.S3Service;
import com.unicommerce.platform.integration.NameValuePair;
import com.unicommerce.platform.integration.auth.models.request.PostConfigurationRequest;
import com.unicommerce.platform.integration.auth.models.request.PreConfigurationRequest;
import com.unicommerce.platform.integration.auth.models.request.VerifyConnectorRequest;
import com.unicommerce.platform.integration.auth.models.response.PostConfigurationResponse;
import com.unicommerce.platform.integration.auth.models.response.PreConfigurationResponse;
import com.unicommerce.platform.integration.auth.models.response.VerifyConnectorResponse;
import com.unicommerce.platform.utils.http.HttpSenderFactory;
import com.unicommerce.platform.web.context.TenantRequestContext;
import com.unifier.core.fileparser.Row;
import com.unifier.core.transport.http.HttpSender;
import com.unifier.core.transport.http.HttpTransportException;
import com.unifier.core.utils.DateUtils;
import com.unifier.core.utils.StringUtils;
import com.uniware.integrations.client.constants.EnvironmentPropertiesConstant;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.dto.api.responseDto.AuthTokenResponse;
import com.uniware.integrations.client.dto.api.responseDto.LocationDetailsResponse;
import com.uniware.integrations.web.exception.BadRequest;
import com.uniware.integrations.web.exception.FailureResponse;
import com.unicommerce.platform.integration.Error;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import static com.unicommerce.platform.integration.ScriptErrorCode.CHANNEL_ERROR;
import static com.unicommerce.platform.integration.ScriptErrorCode.INVALID_CREDENTIALS;
import static com.uniware.integrations.client.constants.flipkartConstants.AUTH_TOKEN;
import static com.uniware.integrations.client.constants.flipkartConstants.AUTH_TOKEN_EXPIRES_IN;
import static com.uniware.integrations.client.constants.flipkartConstants.CATALOG;
import static com.uniware.integrations.client.constants.flipkartConstants.CLIENT_ID;
import static com.uniware.integrations.client.constants.flipkartConstants.CODE;
import static com.uniware.integrations.client.constants.flipkartConstants.CURRENT_CHANNEL_MANIFEST;
import static com.uniware.integrations.client.constants.flipkartConstants.FK_CODE;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_FA;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_INVENTORY_PANEL;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_LITE;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_SELLER_PANEL;
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
import static com.uniware.integrations.client.constants.flipkartConstants.SELLER_ID;
import static com.uniware.integrations.client.constants.flipkartConstants.STATE;
import static com.uniware.integrations.client.constants.flipkartConstants.TMP_DIR;
import static com.uniware.integrations.client.constants.flipkartConstants.USERNAME;

@Service
public class FlipkartHelperService {

    @Autowired private Environment environment;
    @Autowired private FlipkartSellerApiService flipkartSellerApiService;
    @Autowired FlipkartSellerPanelService flipkartSellerPanelService;
    private static final String BUCKET_NAME = "uni-flipkart-integration";
    private static final Logger LOGGER = LoggerFactory.getLogger(FlipkartHelperService.class);
    @Autowired private S3Service s3Service;

    public PreConfigurationResponse preConfiguration(PreConfigurationRequest preConfigurationRequest ) {

        String state = preConfigurationRequest.getParams() != null ? preConfigurationRequest.getParams().get(STATE) : null;
        if ( StringUtils.isBlank( state )) {
            throw new BadRequest("state is missing in request params");
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
        preConfigurationResponse.setMethod(PreConfigurationResponse.Method.GET);
        preConfigurationResponse.setParams(params);
        preConfigurationResponse.setSuccessful(true);

        return preConfigurationResponse;
    }

    public PostConfigurationResponse postConfiguration(PostConfigurationRequest postConfigurationRequest) {

        PostConfigurationResponse postConfigurationResponse = new PostConfigurationResponse();

        Map<String,String> requestParams = postConfigurationRequest.getParams();

        // Generating auth token with the help of authCode (fk_code) passed in request params
        AuthTokenResponse authTokenResponse = flipkartSellerApiService.getAuthToken(requestParams.get(FK_CODE));

        if ( authTokenResponse == null || authTokenResponse.getAccessToken() == null ) {
            postConfigurationResponse.setSuccessful(false);
            postConfigurationResponse.addError(new Error(CHANNEL_ERROR.name(), "either authToken or authToken response is null"));
            return postConfigurationResponse;
        }

        String authToken = authTokenResponse.getTokenType() + " " + authTokenResponse.getAccessToken();
        Long authTokenExpireIn = authTokenResponse.getExpiresIn();
        authTokenExpireIn = (authTokenExpireIn * 1000) + DateUtils.getCurrentTime().getTime();
        String refreshToken = authTokenResponse.getRefreshToken();

        HashMap<String, String> responseParams = new HashMap<>();
        responseParams.put(AUTH_TOKEN, authToken);
        responseParams.put(REFRESH_TOKEN, refreshToken);
        responseParams.put(AUTH_TOKEN_EXPIRES_IN, String.valueOf(authTokenExpireIn));

        String locationId = requestParams.get(LOCATION_ID);
        FlipkartRequestContext.current().setAuthToken(authToken);
        LocationDetailsResponse locationDetailsResponse = flipkartSellerApiService.getAllLocations();
        if ( locationDetailsResponse == null ){
            postConfigurationResponse.setSuccessful(false);
            postConfigurationResponse.addError(new Error(CHANNEL_ERROR.name(), "getting error while fetching location details"));
            return postConfigurationResponse;
        }

        boolean isValidLocation = locationDetailsResponse.getLocations().stream().anyMatch(location -> locationId.equalsIgnoreCase(location.getLocationId()));
        if ( isValidLocation ) {
            responseParams.put(LOCATION_ID, locationId);
        } else {
            postConfigurationResponse.setSuccessful(false);
            postConfigurationResponse.addError(new Error(INVALID_CREDENTIALS.name(), "invalid location id"));
            return postConfigurationResponse;
        }

        postConfigurationResponse.setSuccessful(true);
        postConfigurationResponse.setParams(responseParams);
        return postConfigurationResponse;

    }

    public VerifyConnectorResponse connectorVerification(VerifyConnectorRequest verifyConnectorRequest, String connectorName) {

        VerifyConnectorResponse verifyConnectorResponse = new VerifyConnectorResponse();

        switch ( connectorName ) {

            case FLIPKART_SELLER_PANEL:
                String username = FlipkartRequestContext.current().getUserName();
                String password = FlipkartRequestContext.current().getPassword();
                boolean loginSuccess = flipkartSellerPanelService.sellerPanelLogin(username, password);
                if (!loginSuccess) {
                    verifyConnectorResponse.setSuccessful(false);
                    verifyConnectorResponse.addError(new Error(CHANNEL_ERROR.name(), "unable to login on Flipkart panel"));
                    return verifyConnectorResponse;
                }
                if (verifyConnectorRequest.isConfigure()) {
                    String sellerId = flipkartSellerPanelService.getFeaturesForSeller().getFirst();
                    verifyConnectorResponse.addPersistentParams(new NameValuePair(USERNAME,username));
                    verifyConnectorResponse.addPersistentParams(new NameValuePair(PASSWORD,password));
                    verifyConnectorResponse.addPersistentParams(new NameValuePair(SELLER_ID,sellerId));
                }
                break;

            case FLIPKART_INVENTORY_PANEL:
                String refreshToken = FlipkartRequestContext.current().getRefreshToken();
                Long authTokenExpiresIn = Long.valueOf(FlipkartRequestContext.current().getAuthTokenExpiresIn());
                String locationId = FlipkartRequestContext.current().getLocationId();

                // Generate new authToken 1 day before its expiry.
                boolean isAuthTokenExpiryNear = isAuthTokenExpiryNear(authTokenExpiresIn);
                if (isAuthTokenExpiryNear) {
                    AuthTokenResponse authTokenResponse = flipkartSellerApiService.refreshAuthToken(refreshToken);
                    if (authTokenResponse == null) {
                        verifyConnectorResponse.setSuccessful(false);
                        verifyConnectorResponse.addError(new Error(CHANNEL_ERROR.name(), "unable to fetch authToken"));
                        return verifyConnectorResponse;
                    }
                    String authToken = authTokenResponse.getAccessToken();
                    refreshToken = authTokenResponse.getRefreshToken();
                    authTokenExpiresIn = authTokenResponse.getExpiresIn();
                    authTokenExpiresIn = (authTokenExpiresIn * 1000) + DateUtils.getCurrentTime().getTime();
                    FlipkartRequestContext.current().setAuthToken(authToken);
                    verifyConnectorResponse.addPersistentParams(new NameValuePair(AUTH_TOKEN,authToken));
                    verifyConnectorResponse.addPersistentParams(new NameValuePair(REFRESH_TOKEN,refreshToken));
                    verifyConnectorResponse.addPersistentParams(new NameValuePair(AUTH_TOKEN_EXPIRES_IN,String.valueOf(authTokenExpiresIn)));
                }

                LocationDetailsResponse locationDetailsResponse = flipkartSellerApiService.getAllLocations();
                if (locationDetailsResponse == null) {
                    verifyConnectorResponse.setSuccessful(false);
                    verifyConnectorResponse.addError(new Error(CHANNEL_ERROR.name(), "Getting error while fetching location details"));
                    return verifyConnectorResponse;
                }
                boolean isValidLocation = locationDetailsResponse.getLocations().stream().anyMatch(location -> locationId.equalsIgnoreCase(location.getLocationId()));
                if (!isValidLocation) {
                    verifyConnectorResponse.setSuccessful(false);
                    verifyConnectorResponse.addError(new Error(CHANNEL_ERROR.name(), "Invalid locationId. Kindly check"));
                    return verifyConnectorResponse;
                }
                break;

            default:
                LOGGER.info("Invalid connector name");
                throw new FailureResponse("Invalid Connector");
        }

        return verifyConnectorResponse;
    }
    public boolean isAuthTokenExpiryNear(Long authTokenExpiresIn)  {

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

    public String getChannelProductIdBySourceCode(Row row, String sourceCode) {

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
            return TMP_DIR + com.unicommerce.platform.utils.StringUtils.join('_', TenantRequestContext.current().getHttpSenderIdentifier(), UUID.randomUUID().toString(), PENDENCY) + "." + fileFormat;

        // For catalog we kept file for 3 hour on local machine
        case CATALOG:
            return TMP_DIR + com.unicommerce.platform.utils.StringUtils.join('_', TenantRequestContext.current().getTenantCode(), FlipkartRequestContext.current().getSellerId(), currentDate, CATALOG) + "." + fileFormat;

        // For invoice we kept file for 1/2 hour on local machine
        case INVOICE:
            return TMP_DIR + com.unicommerce.platform.utils.StringUtils.join('_', TenantRequestContext.current().getHttpSenderIdentifier(), UUID.randomUUID().toString(), INVOICE) + ".pdf";

        // For label we kept file for 1/2 hour on local machine
        case LABEL:
            return TMP_DIR + com.unicommerce.platform.utils.StringUtils.join('_', TenantRequestContext.current().getHttpSenderIdentifier(), UUID.randomUUID().toString(), LABEL) + ".pdf";

        // For invoice_label we kept file for 1/2 hour on local machine
        case INVOICE_LABEL:
            return TMP_DIR + com.unicommerce.platform.utils.StringUtils.join('_', TenantRequestContext.current().getHttpSenderIdentifier(), UUID.randomUUID().toString(), INVOICE_LABEL) + ".pdf";

        // For current_channel_manifest we kept file for 1/2 hour on local machine
        case CURRENT_CHANNEL_MANIFEST:
            return TMP_DIR + com.unicommerce.platform.utils.StringUtils.join('_', TenantRequestContext.current().getHttpSenderIdentifier(), UUID.randomUUID().toString(), CURRENT_CHANNEL_MANIFEST) + "." + fileFormat;

        default:
                LOGGER.error("Invalid File Identifier");
                return null;
        }
    }

    @Cacheable(value = "csrfTokenCache", key = "#result.first", unless = "#result != null")
    public Pair<String, String> fetchCsrfToken(){
        Pair<String, String> sellerIdToCsrfTokenPair = flipkartSellerPanelService.getFeaturesForSeller();
        if ( sellerIdToCsrfTokenPair == null ) {
            LOGGER.error("Unable to fetch csrf token.");
            return null;
        }
        return sellerIdToCsrfTokenPair;
    }


    public String checkIfFileExistOnS3(String fileName) {
        String url = "https:// " + BUCKET_NAME + ".s3.amazonaws.com/" + fileName;

        HttpSender httpSender = HttpSenderFactory.getHttpSenderNoProxy();
        try {
            String response = httpSender.executeGet(url, Collections.emptyMap());
            if (response.contains("AccessDenied")){
                LOGGER.info("file not exist on s3");
                return null;
            }
            return url;
        }
        catch (HttpTransportException | JsonSyntaxException ex) {
            LOGGER.error("Something went wrong while downloading file from s3 Error {}", ex);
            throw new FailureResponse("Something went wrong while downloading file from s3, error message {} " + ex.getMessage());
        }
    }
}

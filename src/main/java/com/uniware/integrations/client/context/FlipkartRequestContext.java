package com.uniware.integrations.client.context;

import com.unifier.core.utils.StringUtils;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.utils.Constants;
import com.uniware.integrations.web.filter.FlipkartFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Created by vipin on 20/05/22.
 */
public class FlipkartRequestContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlipkartFilter.class);
    private static final ThreadLocal<FlipkartRequestContext> ctx = new ThreadLocal<FlipkartRequestContext>();

    private String              apiVersion;
    private String              requestURI;
    private String              requestIdentifier;
    private ChannelSource       channelSource;
    private Map<String,String>  apiHeaders = new HashMap<>();
    private Map<String, String> sellerPanelHeaders = new HashMap<>();
    private String              userName;
    private String              password;
    private String              sellerId;
    private String              locationId;
    private String              authToken;
    private String              authTokenExpiresIn;
    private String              refreshToken;
    private Map<String, Object> contextVariables = new HashMap();

    public FlipkartRequestContext() {
        sellerPanelHeaders.put("Accept", "application/json, text/javascript, */*; q=0.01");
        sellerPanelHeaders.put("Accept-Encoding", "gzip");
        sellerPanelHeaders.put("X-Requested-With", "XMLHttpRequest");
        sellerPanelHeaders.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        sellerPanelHeaders.put("Content-Type", "application/json");
    }

    public FlipkartRequestContext(FlipkartRequestContext flipkartRequestContext) {
        setApiVersion(flipkartRequestContext.getApiVersion());
        setRequestURI(flipkartRequestContext.getRequestURI());
        setRequestIdentifier(flipkartRequestContext.getRequestIdentifier());
        setSellerPanelHeaders(flipkartRequestContext.getSellerPanelHeaders());
        setSellerId(this.sellerId = flipkartRequestContext.getSellerId());
        setLocationId(flipkartRequestContext.getLocationId());
        setUserName(flipkartRequestContext.getUserName());
        setPassword(flipkartRequestContext.getPassword());
        setAuthToken(flipkartRequestContext.getAuthToken());
        setAuthTokenExpiresIn(flipkartRequestContext.getAuthTokenExpiresIn());
        setRefreshToken(flipkartRequestContext.getRefreshToken());
    }

    public static void setContext(FlipkartRequestContext flipkartRequestContext) {
        FlipkartRequestContext flipkartRequestContext1 = new FlipkartRequestContext(flipkartRequestContext);
        ctx.set(flipkartRequestContext1);
    }

    public static FlipkartRequestContext current() {
        FlipkartRequestContext flipkartRequestContext = ctx.get();
        if (flipkartRequestContext == null) {
            flipkartRequestContext = new FlipkartRequestContext();
            ctx.set(flipkartRequestContext);
        }
        return flipkartRequestContext;
    }

    public static void destroy() {
        ctx.remove();
        MDC.remove(Constants.REQUEST_IDENTIFIER);
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getRequestIdentifier() {
        return requestIdentifier;
    }

    public ChannelSource getChannelSource() {
        return channelSource;
    }

    public void setChannelSource(ChannelSource channelSource) {
        this.channelSource = channelSource;
    }

    public void setRequestIdentifier(String requestIdentifier) {
        MDC.put(Constants.REQUEST_IDENTIFIER, requestIdentifier);
        this.requestIdentifier = requestIdentifier;
    }

    public void addVariable(String key, Object value) {
        contextVariables.put(key, value);
    }

    public void removeVariable(String key) {
        contextVariables.remove(key);
    }

    public Object getVariable(String key) {
        return contextVariables.get(key);
    }

    public Map<String, Object> getContextVariables() {
        return contextVariables;
    }

    public static String generateRequestIdentifier() {
        return UUID.randomUUID().toString();
    }

    public Map<String, String> getSellerPanelHeaders() {
        return sellerPanelHeaders;
    }

    public void setSellerPanelHeaders(Map<String, String> sellerPanelHeaders) {
        this.sellerPanelHeaders = sellerPanelHeaders;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthTokenExpiresIn() {
        return authTokenExpiresIn;
    }

    public void setAuthTokenExpiresIn(String authTokenExpiresIn) {
        this.authTokenExpiresIn = authTokenExpiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean validate() {
        if ( StringUtils.isBlank(authToken) ) {
            LOGGER.error("AuthToken is missing in headers");
            return false;
        }
        if ( StringUtils.isBlank(locationId) ) {
            LOGGER.error("locationId is missing in headers");
            return false;
        }
        if ( StringUtils.isBlank(userName) ) {
            LOGGER.error("userName is missing in headers");
            return false;
        }
        if ( StringUtils.isBlank(password) ) {
            LOGGER.error("password is missing in headers");
            return false;
        }
        return true;
    }

    @Override public String toString() {
        return "FlipkartRequestContext{" + "apiVersion='" + apiVersion + '\'' + ", requestURI='" + requestURI + '\''
                + ", requestIdentifier='" + requestIdentifier + '\'' + ", channelSource=" + channelSource
                + ", apiHeaders=" + apiHeaders + ", sellerPanelHeaders=" + sellerPanelHeaders + ", userName='"
                + userName + '\'' + ", password='" + password + '\'' + ", sellerId='" + sellerId + '\''
                + ", locationId='" + locationId + '\'' + ", authToken='" + authToken + '\'' + ", authTokenExpiresIn='"
                + authTokenExpiresIn + '\'' + ", refreshToken='" + refreshToken + '\'' + ", contextVariables="
                + contextVariables + '}';
    }
}

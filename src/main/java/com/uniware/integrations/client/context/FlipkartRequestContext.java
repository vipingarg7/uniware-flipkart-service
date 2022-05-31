package com.uniware.integrations.client.context;

import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.utils.Constants;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.MDC;

/**
 * Created by vipin on 20/05/22.
 */
public class FlipkartRequestContext {

    private static final ThreadLocal<FlipkartRequestContext> ctx = new ThreadLocal<FlipkartRequestContext>();

    private String              apiVersion;
    private String              requestURI;
    private String              requestIdentifier;
    private ChannelSource channelSource;
    private Map<String, Object> contextVariables = new HashMap();

    public FlipkartRequestContext() {}

    public FlipkartRequestContext(FlipkartRequestContext flipkartRequestContext) {
        setApiVersion(flipkartRequestContext.getApiVersion());
        setRequestURI(flipkartRequestContext.getRequestURI());
        setRequestIdentifier(flipkartRequestContext.getRequestIdentifier());
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
}

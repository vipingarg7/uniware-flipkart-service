package com.uniware.integrations.client.dto.uniware;

import java.util.Map;

/**
 * Created by vipin on 25/05/22.
 */
public class PreConfigurationResponse {

    private String url;
    private String method;
    private Map<String,String> params;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

}

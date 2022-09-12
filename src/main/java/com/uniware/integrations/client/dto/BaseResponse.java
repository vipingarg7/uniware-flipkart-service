package com.uniware.integrations.client.dto;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.springframework.http.HttpStatus;

public class BaseResponse {

    private Map<String,String> responseHeaders;

    private HttpStatus responseStatus;

    public BaseResponse() {}

    public HttpStatus getResponseStatus() {
        return responseStatus;
    }

    public Map<String,String> getAllResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public void setResponseStatus(HttpStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    // Todo add method to get a specific header

}

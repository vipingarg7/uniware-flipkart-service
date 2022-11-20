package com.uniware.integrations.client.dto.uniware;

import com.uniware.integrations.uniware.dto.rest.api.Error;

public class DispatchShipmentResponse {

    public enum Status {
        SUCCESS,
        FAILED
    }

    private String saleOrderCode;
    private Status status;
    private Error error;

    public String getSaleOrderCode() {
        return saleOrderCode;
    }

    public void setSaleOrderCode(String saleOrderCode) {
        this.saleOrderCode = saleOrderCode;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}

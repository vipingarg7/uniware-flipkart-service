package com.uniware.integrations.client.dto;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class DispatchShipmentStatus {

    @SerializedName("shipmentId")
    private String shipmentId = null;

    @SerializedName(value="status", alternate="processingStatus")
    private String status = null;

    @SerializedName("errorCode")
    private String errorCode = null;

    @SerializedName("errorMessage")
    private String errorMessage = null;

    public DispatchShipmentStatus errorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    /**
     * Get errorCode
     * @return errorCode
     **/

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public DispatchShipmentStatus errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    /**
     * Get errorMessage
     * @return errorMessage
     **/

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public DispatchShipmentStatus processingStatus(String processingStatus) {
        this.status = processingStatus;
        return this;
    }

    /**
     * Get status
     * @return status
     **/

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DispatchShipmentStatus shipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
        return this;
    }

    /**
     * Get shipmentId
     * @return shipmentId
     **/

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    @Override public String toString() {
        return "DispatchShipmentStatus{" + "shipmentId='" + shipmentId + '\'' + ", status='" + status + '\''
                + ", errorCode='" + errorCode + '\'' + ", errorMessage='" + errorMessage + '\'' + '}';
    }

}

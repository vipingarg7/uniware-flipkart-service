package com.uniware.integrations.client.dto;

import com.google.gson.annotations.SerializedName;

public class PackShipmentStatus {

    @SerializedName("errorCode")
    private String errorCode = null;

    @SerializedName("errorMessage")
    private String errorMessage = null;

    @SerializedName("status")
    private String status = null;

    @SerializedName("shipmentId")
    private String shipmentId = null;

    public PackShipmentStatus errorCode(String errorCode) {
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

    public PackShipmentStatus errorMessage(String errorMessage) {
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

    public PackShipmentStatus processingStatus(String processingStatus) {
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

    public PackShipmentStatus shipmentId(String shipmentId) {
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
        return "PackShipmentStatus{" + "errorCode='" + errorCode + '\'' + ", errorMessage='" + errorMessage + '\''
                + ", status='" + status + '\'' + ", shipmentId='" + shipmentId + '\'' + '}';
    }

}

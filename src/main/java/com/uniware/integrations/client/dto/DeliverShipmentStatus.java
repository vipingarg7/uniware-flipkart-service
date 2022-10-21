package com.uniware.integrations.client.dto;

import com.google.gson.annotations.SerializedName;

public class DeliverShipmentStatus {

    @SerializedName("errorCode")
    private String errorCode = null;

    @SerializedName("errorMessage")
    private String errorMessage = null;

    @SerializedName("processingStatus")
    private String processingStatus = null;

    @SerializedName("shipmentId")
    private String shipmentId = null;

    public DeliverShipmentStatus errorCode(String errorCode) {
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

    public DeliverShipmentStatus errorMessage(String errorMessage) {
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

    public DeliverShipmentStatus processingStatus(String processingStatus) {
        this.processingStatus = processingStatus;
        return this;
    }

    /**
     * Get processingStatus
     * @return processingStatus
     **/
    
    public String getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(String processingStatus) {
        this.processingStatus = processingStatus;
    }

    public DeliverShipmentStatus shipmentId(String shipmentId) {
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
        return "DeliverShipmentStatus{" + "errorCode='" + errorCode + '\'' + ", errorMessage='" + errorMessage + '\''
                + ", processingStatus='" + processingStatus + '\'' + ", shipmentId='" + shipmentId + '\'' + '}';
    }
}

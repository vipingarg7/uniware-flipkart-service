package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class DeliverShipmentStatus {

    @JsonProperty("errorCode")
    private String errorCode = null;

    @JsonProperty("errorMessage")
    private String errorMessage = null;

    @JsonProperty("processingStatus")
    private String processingStatus = null;

    @JsonProperty("shipmentId")
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeliverShipmentStatus DeliverShipmentStatus = (DeliverShipmentStatus) o;
        return Objects.equals(this.errorCode, DeliverShipmentStatus.errorCode) &&
                Objects.equals(this.errorMessage, DeliverShipmentStatus.errorMessage) &&
                Objects.equals(this.processingStatus, DeliverShipmentStatus.processingStatus) &&
                Objects.equals(this.shipmentId, DeliverShipmentStatus.shipmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorCode, errorMessage, processingStatus, shipmentId);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DeliverShipmentStatus {\n");

        sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
        sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
        sb.append("    processingStatus: ").append(toIndentedString(processingStatus)).append("\n");
        sb.append("    shipmentId: ").append(toIndentedString(shipmentId)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
    
}

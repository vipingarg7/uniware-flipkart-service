package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class DispatchShipmentStatus {

    @JsonProperty("shipmentId")
    private String shipmentId = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("errorCode")
    private String errorCode = null;

    @JsonProperty("errorMessage")
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DispatchShipmentStatus DispatchShipmentStatus = (DispatchShipmentStatus) o;
        return Objects.equals(this.errorCode, DispatchShipmentStatus.errorCode) &&
                Objects.equals(this.errorMessage, DispatchShipmentStatus.errorMessage) &&
                Objects.equals(this.status, DispatchShipmentStatus.status) &&
                Objects.equals(this.shipmentId, DispatchShipmentStatus.shipmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorCode, errorMessage, status, shipmentId);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DispatchShipmentStatus {\n");

        sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
        sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

package com.uniware.integrations.client.dto.api.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseRequest;
import java.util.List;
import java.util.Objects;

public class DispatchStandardShipmentV3Request extends BaseRequest {

    @SerializedName("locationId")
    private String locationId = null;

    @SerializedName("shipmentIds")
    private List<String> shipmentIds = null;

    public DispatchStandardShipmentV3Request locationId(String locationId) {
        this.locationId = locationId;
        return this;
    }

    /**
     * Get locationId
     * @return locationId
     **/
    
    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public DispatchStandardShipmentV3Request shipmentIds(java.util.List<String> shipmentIds) {
        this.shipmentIds = shipmentIds;
        return this;
    }

    public DispatchStandardShipmentV3Request addShipmentId(String shipmentId) {
        if (this.shipmentIds == null) {
            this.shipmentIds = new java.util.ArrayList<>();
        }
        this.shipmentIds.add(shipmentId);
        return this;
    }

    /**
     * Get shipmentIds
     * @return shipmentIds
     **/
    
    public java.util.List<String> getShipmentIds() {
        return shipmentIds;
    }

    public void setShipmentIds(java.util.List<String> shipmentIds) {
        this.shipmentIds = shipmentIds;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DispatchStandardShipmentV3Request DispatchStandardShipmentRequest = (DispatchStandardShipmentV3Request) o;
        return Objects.equals(this.locationId, DispatchStandardShipmentRequest.locationId) &&
                Objects.equals(this.shipmentIds, DispatchStandardShipmentRequest.shipmentIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, shipmentIds);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DispatchStandardShipmentRequest {\n");

        sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
        sb.append("    shipmentIds: ").append(toIndentedString(shipmentIds)).append("\n");
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

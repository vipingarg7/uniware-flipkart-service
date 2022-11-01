package com.uniware.integrations.client.dto.api.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseRequest;
import com.uniware.integrations.client.dto.PackRequest;
import java.util.List;
import java.util.Objects;

public class ShipmentPackV3Request extends BaseRequest {

    @SerializedName("shipments")
    private List<PackRequest> shipments = null;

    public ShipmentPackV3Request shipments(java.util.List<PackRequest> shipments) {
        this.shipments = shipments;
        return this;
    }

    public ShipmentPackV3Request addShipmentsItem(PackRequest shipment) {
        if (this.shipments == null) {
            this.shipments = new java.util.ArrayList<>();
        }
        this.shipments.add(shipment);
        return this;
    }

    /**
     * Get shipments
     * @return shipments
     **/
    public java.util.List<PackRequest> getShipments() {
        return shipments;
    }

    public void setShipments(java.util.List<PackRequest> shipments) {
        this.shipments = shipments;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShipmentPackV3Request shipmentPackRequest = (ShipmentPackV3Request) o;
        return Objects.equals(this.shipments, shipmentPackRequest.shipments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shipments);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ShipmentPackRequest {\n");

        sb.append("    shipments: ").append(toIndentedString(shipments)).append("\n");
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

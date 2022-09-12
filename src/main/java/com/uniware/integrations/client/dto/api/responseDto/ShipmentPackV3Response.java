package com.uniware.integrations.client.dto.api.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uniware.integrations.client.dto.PackShipmentStatus;
import java.util.List;
import java.util.Objects;

public class ShipmentPackV3Response {

    @JsonProperty("shipments")
    private List<PackShipmentStatus> shipments = null;

    public ShipmentPackV3Response shipments(List<PackShipmentStatus> shipments) {
        this.shipments = shipments;
        return this;
    }

    public ShipmentPackV3Response addShipmentsItem(PackShipmentStatus shipmentsItem) {
        if (this.shipments == null) {
            this.shipments = new java.util.ArrayList<>();
        }
        this.shipments.add(shipmentsItem);
        return this;
    }

    /**
     * Get shipments
     * @return shipments
     **/

    public List<PackShipmentStatus> getShipments() {
        return shipments;
    }

    public void setShipments(List<PackShipmentStatus> shipments) {
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
        ShipmentPackV3Response ShipmentPackV3Response = (ShipmentPackV3Response) o;
        return Objects.equals(this.shipments, ShipmentPackV3Response.shipments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shipments);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ShipmentPackV3Response {\n");

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

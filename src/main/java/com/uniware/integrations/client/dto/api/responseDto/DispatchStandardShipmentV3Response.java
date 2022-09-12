package com.uniware.integrations.client.dto.api.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uniware.integrations.client.dto.DispatchShipmentStatus;
import java.util.List;
import java.util.Objects;

public class DispatchStandardShipmentV3Response {

    @JsonProperty("shipments")
    private List<DispatchShipmentStatus> shipments;

    public DispatchStandardShipmentV3Response addShipmentsItem(DispatchShipmentStatus shipmentsItem) {
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
    public List<DispatchShipmentStatus> getShipments() {
        return shipments;
    }

    public void setShipments(List<DispatchShipmentStatus> shipments) {
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
        DispatchStandardShipmentV3Response DispatchStandardShipmentV3Response = (DispatchStandardShipmentV3Response) o;
        return Objects.equals(this.shipments, DispatchStandardShipmentV3Response.shipments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shipments);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DispatchStandardShipmentV3Response {\n");

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

package com.uniware.integrations.client.dto.api.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uniware.integrations.client.dto.ShipmentDetails;
import java.util.Objects;

public class ShipmentDetailsSearchResponseV3 {

    @JsonProperty("shipments")
    private java.util.List<ShipmentDetails> shipments = null;

    public ShipmentDetailsSearchResponseV3 shipments(java.util.List<ShipmentDetails> shipments) {
        this.shipments = shipments;
        return this;
    }

    public ShipmentDetailsSearchResponseV3 addShipmentsItem(ShipmentDetails shipmentsItem) {
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
    
    public java.util.List<ShipmentDetails> getShipments() {
        return shipments;
    }

    public void setShipments(java.util.List<ShipmentDetails> shipments) {
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
        ShipmentDetailsSearchResponseV3 ShipmentDetailsSearchResponse = (ShipmentDetailsSearchResponseV3) o;
        return Objects.equals(this.shipments, ShipmentDetailsSearchResponse.shipments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shipments);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ShipmentDetailsSearchResponse {\n");

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

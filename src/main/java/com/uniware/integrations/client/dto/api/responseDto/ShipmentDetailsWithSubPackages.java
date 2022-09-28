package com.uniware.integrations.client.dto.api.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uniware.integrations.client.dto.Shipment;
import java.util.Objects;

public class ShipmentDetailsWithSubPackages {
    
    @JsonProperty("shipments")
    private java.util.List<Shipment> shipments = null;

    public ShipmentDetailsWithSubPackages shipments(java.util.List<Shipment> shipments) {
        this.shipments = shipments;
        return this;
    }

    public ShipmentDetailsWithSubPackages addShipmentsItem(Shipment shipmentsItem) {
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

    public java.util.List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(java.util.List<Shipment> shipments) {
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
        ShipmentDetailsWithSubPackages ShipmentDetailsSearchResponse = (ShipmentDetailsWithSubPackages) o;
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

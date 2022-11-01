package com.uniware.integrations.client.dto.api.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseRequest;
import com.uniware.integrations.client.dto.DeliverRequest;
import java.util.Objects;

public class ShipmentDeliveryRequestV3 extends BaseRequest {

    @SerializedName("shipments")
    private java.util.List<DeliverRequest> shipments = new java.util.ArrayList<>();

    public ShipmentDeliveryRequestV3 shipments(java.util.List<DeliverRequest> shipments) {
        this.shipments = shipments;
        return this;
    }

    public ShipmentDeliveryRequestV3 addShipmentsItem(DeliverRequest shipmentsItem) {
        this.shipments.add(shipmentsItem);
        return this;
    }

    /**
     * Get shipments
     * @return shipments
     **/

    public java.util.List<DeliverRequest> getShipments() {
        return shipments;
    }

    public void setShipments(java.util.List<DeliverRequest> shipments) {
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
        ShipmentDeliveryRequestV3 ShipmentDeliveryRequestV3 = (ShipmentDeliveryRequestV3) o;
        return Objects.equals(this.shipments, ShipmentDeliveryRequestV3.shipments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shipments);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ShipmentDeliveryRequestV3 {\n");

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

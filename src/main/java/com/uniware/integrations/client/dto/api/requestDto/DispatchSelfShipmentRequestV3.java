package com.uniware.integrations.client.dto.api.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uniware.integrations.client.dto.BaseRequest;
import com.uniware.integrations.client.dto.DispatchRequest;
import java.util.Objects;

public class DispatchSelfShipmentRequestV3 extends BaseRequest {

    @JsonProperty("shipments")
    private java.util.List<DispatchRequest> shipments = null;

    public DispatchSelfShipmentRequestV3 shipments(java.util.List<DispatchRequest> shipments) {
        this.shipments = shipments;
        return this;
    }

    public DispatchSelfShipmentRequestV3 addShipmentsItem(DispatchRequest shipmentsItem) {
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
    
    public java.util.List<DispatchRequest> getShipments() {
        return shipments;
    }

    public void setShipments(java.util.List<DispatchRequest> shipments) {
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
        DispatchSelfShipmentRequestV3 DispatchSelfShipmentRequest = (DispatchSelfShipmentRequestV3) o;
        return Objects.equals(this.shipments, DispatchSelfShipmentRequest.shipments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shipments);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DispatchSelfShipmentRequest {\n");

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

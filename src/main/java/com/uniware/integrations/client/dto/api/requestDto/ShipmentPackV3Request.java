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

    @Override public String toString() {
        return "ShipmentPackV3Request{" + "shipments=" + shipments + '}';
    }
}

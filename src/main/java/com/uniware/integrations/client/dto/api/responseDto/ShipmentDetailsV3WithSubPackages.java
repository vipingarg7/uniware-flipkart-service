package com.uniware.integrations.client.dto.api.responseDto;

import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseResponse;
import com.uniware.integrations.client.dto.Shipment;
import java.util.List;

public class ShipmentDetailsV3WithSubPackages extends BaseResponse {
    
    @SerializedName("shipments")
    private List<Shipment> shipments = null;

    public ShipmentDetailsV3WithSubPackages shipments(List<Shipment> shipments) {
        this.shipments = shipments;
        return this;
    }

    public ShipmentDetailsV3WithSubPackages addShipmentsItem(Shipment shipmentsItem) {
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

    public List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }

    @Override public String toString() {
        return "ShipmentDetailsV3WithSubPackages{" + "shipments=" + shipments + '}';
    }
}

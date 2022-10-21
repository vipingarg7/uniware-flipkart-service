package com.uniware.integrations.client.dto.api.responseDto;

import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseResponse;
import com.uniware.integrations.client.dto.PackShipmentStatus;
import java.util.List;

public class ShipmentPackV3Response extends BaseResponse {

    @SerializedName("shipments")
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

    @Override public String toString() {
        return "ShipmentPackV3Response{" + "shipments=" + shipments + '}';
    }

}

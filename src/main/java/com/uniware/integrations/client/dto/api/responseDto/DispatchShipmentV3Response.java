package com.uniware.integrations.client.dto.api.responseDto;

import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseResponse;
import com.uniware.integrations.client.dto.DispatchShipmentStatus;
import java.util.List;

public class DispatchShipmentV3Response extends BaseResponse {

    @SerializedName("shipments")
    private List<DispatchShipmentStatus> shipments;

    public DispatchShipmentV3Response addShipmentsItem(DispatchShipmentStatus shipmentsItem) {
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

    @Override public String toString() {
        return "DispatchShipmentV3Response{" + "shipments=" + shipments + '}';
    }
}

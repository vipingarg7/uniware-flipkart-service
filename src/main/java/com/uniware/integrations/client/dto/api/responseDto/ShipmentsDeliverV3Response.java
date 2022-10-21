package com.uniware.integrations.client.dto.api.responseDto;

import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseResponse;
import com.uniware.integrations.client.dto.DeliverShipmentStatus;
import java.util.List;

public class ShipmentsDeliverV3Response extends BaseResponse {

    @SerializedName("shipments")
    private List<DeliverShipmentStatus> shipments = null;

    public ShipmentsDeliverV3Response shipments(List<DeliverShipmentStatus> shipments) {
        this.shipments = shipments;
        return this;
    }

    public ShipmentsDeliverV3Response addShipmentsItem(DeliverShipmentStatus shipmentsItem) {
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
    
    public List<DeliverShipmentStatus> getShipments() {
        return shipments;
    }

    public void setShipments(List<DeliverShipmentStatus> shipments) {
        this.shipments = shipments;
    }

    @Override public String toString() {
        return "ShipmentsDeliverV3Response{" + "shipments=" + shipments + '}';
    }
}

package com.uniware.integrations.client.dto.api.responseDto;

import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseResponse;
import com.uniware.integrations.client.dto.ShipmentDetails;
import java.util.List;

public class ShipmentDetailsV3WithAddressResponse extends BaseResponse {

    @SerializedName("shipments")
    private List<ShipmentDetails> shipments = null;

    public ShipmentDetailsV3WithAddressResponse shipments(List<ShipmentDetails> shipments) {
        this.shipments = shipments;
        return this;
    }

    public ShipmentDetailsV3WithAddressResponse addShipmentsItem(ShipmentDetails shipmentsItem) {
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
    
    public List<ShipmentDetails> getShipments() {
        return shipments;
    }

    public void setShipments(List<ShipmentDetails> shipments) {
        this.shipments = shipments;
    }

    @Override public String toString() {
        return "ShipmentDetailsV3WithAddressResponse{" + "shipments=" + shipments + '}';
    }
}

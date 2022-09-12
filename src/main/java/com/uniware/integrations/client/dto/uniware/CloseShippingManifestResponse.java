package com.uniware.integrations.client.dto.uniware;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CloseShippingManifestResponse {

    private String shippingManifestLink;
    private List<ShipmentStatus> shipments;

    public CloseShippingManifestResponse addShipment(ShipmentStatus shipment) {
        if (this.shipments == null) {
            this.shipments = new ArrayList<>();
        }
        this.shipments.add(shipment);
        return this;
    }

    @Data
    public static class ShipmentStatus {

        @JsonProperty("shipmentCode")
        private String shipmentCode = null;

        @JsonProperty("saleOrderCode")
        private String saleOrderCode = null;

        @JsonProperty("status")
        private String status = null;

        @JsonProperty("isCancelled")
        private Boolean isCancelled = false;

        @JsonProperty("errorCode")
        private String errorCode = null;

        @JsonProperty("errorMessage")
        private String errorMessage = null;

    }
}

package com.uniware.integrations.client.dto.uniware;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CloseShippingManifestResponse {

    private String shippingManifestLink;
    private List<FailedShipment> failedShipments;

    public CloseShippingManifestResponse addFailedShipment(FailedShipment failedShipment) {
        if ( this.failedShipments == null ) {
            this.failedShipments = new ArrayList<>();
        }
        this.failedShipments.add(failedShipment);
        return this;
    }

    public static class FailedShipment {
        private String shipmentCode;
        private String saleOrderCode;
        private String failureReason;
        private boolean cancelled = false;
        public FailedShipment() {
        }

        public FailedShipment(String shipmentCode, String saleOrderCode, String failureReason, boolean cancelled) {
            this.shipmentCode = shipmentCode;
            this.saleOrderCode = saleOrderCode;
            this.failureReason = failureReason;
            this.cancelled = cancelled;
        }

        public String getShipmentCode() {
            return shipmentCode;
        }

        public void setShipmentCode(String shipmentCode) {
            this.shipmentCode = shipmentCode;
        }

        public String getSaleOrderCode() {
            return saleOrderCode;
        }

        public void setSaleOrderCode(String saleOrderCode) {
            this.saleOrderCode = saleOrderCode;
        }

        public String getFailureReason() {
            return failureReason;
        }

        public void setFailureReason(String failureReason) {
            this.failureReason = failureReason;
        }

        public boolean isCancelled() {
            return cancelled;
        }

        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }

    }

}

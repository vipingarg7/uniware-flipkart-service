package com.uniware.integrations.client.dto;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DispatchRequest {

    @SerializedName("facilityId")
    private String facilityId = null;

    @SerializedName("validTrackingLength")
    private Boolean validTrackingLength = null;

    @SerializedName("dispatchDateValid")
    private Boolean dispatchDateValid = null;

    @SerializedName("shipmentId")
    private String shipmentId = null;

    @SerializedName("tentativeDeliveryDateValid")
    private Boolean tentativeDeliveryDateValid = null;

    @SerializedName("locationId")
    private String locationId = null;

    @SerializedName("invoice")
    private Invoice invoice;

    @SerializedName("trackingId")
    private String trackingId = null;

    @SerializedName("tentativeDeliveryDate")
    private String tentativeDeliveryDate = null;

    @SerializedName("deliveryPartner")
    private String deliveryPartner = null;

    @SerializedName("dispatchDate")
    private String dispatchDate = null;

    public DispatchRequest facilityId(String facilityId) {
        this.facilityId = facilityId;
        return this;
    }

    /**
     * Get facilityId
     * @return facilityId
     **/
    
    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public DispatchRequest validTrackingLength(Boolean validTrackingLength) {
        this.validTrackingLength = validTrackingLength;
        return this;
    }

    /**
     * Get validTrackingLength
     * @return validTrackingLength
     **/
    
    public Boolean isValidTrackingLength() {
        return validTrackingLength;
    }

    public void setValidTrackingLength(Boolean validTrackingLength) {
        this.validTrackingLength = validTrackingLength;
    }

    public DispatchRequest dispatchDateValid(Boolean dispatchDateValid) {
        this.dispatchDateValid = dispatchDateValid;
        return this;
    }

    /**
     * Get dispatchDateValid
     * @return dispatchDateValid
     **/
    
    public Boolean isDispatchDateValid() {
        return dispatchDateValid;
    }

    public void setDispatchDateValid(Boolean dispatchDateValid) {
        this.dispatchDateValid = dispatchDateValid;
    }

    public DispatchRequest shipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
        return this;
    }

    /**
     * Get shipmentId
     * @return shipmentId
     **/
    
    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public DispatchRequest tentativeDeliveryDateValid(Boolean tentativeDeliveryDateValid) {
        this.tentativeDeliveryDateValid = tentativeDeliveryDateValid;
        return this;
    }

    /**
     * Get tentativeDeliveryDateValid
     * @return tentativeDeliveryDateValid
     **/
    
    public Boolean isTentativeDeliveryDateValid() {
        return tentativeDeliveryDateValid;
    }

    public void setTentativeDeliveryDateValid(Boolean tentativeDeliveryDateValid) {
        this.tentativeDeliveryDateValid = tentativeDeliveryDateValid;
    }

    public DispatchRequest locationId(String locationId) {
        this.locationId = locationId;
        return this;
    }

    /**
     * Get locationId
     * @return locationId
     **/
    
    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Boolean getValidTrackingLength() {
        return validTrackingLength;
    }

    public Boolean getDispatchDateValid() {
        return dispatchDateValid;
    }

    public Boolean getTentativeDeliveryDateValid() {
        return tentativeDeliveryDateValid;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public DispatchRequest trackingId(String trackingId) {
        this.trackingId = trackingId;
        return this;
    }

    /**
     * Get trackingId
     * @return trackingId
     **/
    
    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public DispatchRequest tentativeDeliveryDate(String tentativeDeliveryDate) {
        this.tentativeDeliveryDate = tentativeDeliveryDate;
        return this;
    }

    /**
     * Get tentativeDeliveryDate
     * @return tentativeDeliveryDate
     **/
    
    public String getTentativeDeliveryDate() {
        return tentativeDeliveryDate;
    }

    public void setTentativeDeliveryDate(String tentativeDeliveryDate) {
        this.tentativeDeliveryDate = tentativeDeliveryDate;
    }

    public DispatchRequest deliveryPartner(String deliveryPartner) {
        this.deliveryPartner = deliveryPartner;
        return this;
    }

    /**
     * Get deliveryPartner
     * @return deliveryPartner
     **/
    
    public String getDeliveryPartner() {
        return deliveryPartner;
    }

    public void setDeliveryPartner(String deliveryPartner) {
        this.deliveryPartner = deliveryPartner;
    }

    public DispatchRequest dispatchDate(String dispatchDate) {
        this.dispatchDate = dispatchDate;
        return this;
    }

    /**
     * Get dispatchDate
     * @return dispatchDate
     **/
    
    public String getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(String dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    @Override public String toString() {
        return "DispatchRequest{" + "facilityId='" + facilityId + '\'' + ", validTrackingLength=" + validTrackingLength
                + ", dispatchDateValid=" + dispatchDateValid + ", shipmentId='" + shipmentId + '\''
                + ", tentativeDeliveryDateValid=" + tentativeDeliveryDateValid + ", locationId='" + locationId + '\''
                + ", invoice=" + invoice + ", trackingId='" + trackingId + '\'' + ", tentativeDeliveryDate="
                + tentativeDeliveryDate + ", deliveryPartner='" + deliveryPartner + '\'' + ", dispatchDate="
                + dispatchDate + '}';
    }

    public static class Invoice {

        @SerializedName("invoiceNumber")
        private String invoiceNumber = null;

        @SerializedName("invoiceDate")
        private String invoiceDate = null;

        @SerializedName("items")
        private List<ConfirmItemRow> orderItems = new ArrayList<>();

        public String getInvoiceNumber() {
            return invoiceNumber;
        }

        public void setInvoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
        }

        public String getInvoiceDate() {
            return invoiceDate;
        }

        public void setInvoiceDate(String invoiceDate) {
            this.invoiceDate = invoiceDate;
        }

        public List<ConfirmItemRow> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(List<ConfirmItemRow> orderItems) {
            this.orderItems = orderItems;
        }

        public Invoice orderItems(List<ConfirmItemRow> orderItems) {
            this.orderItems = orderItems;
            return this;
        }

        public Invoice addOrderItemsItem(ConfirmItemRow orderItemsItem) {
            this.orderItems.add(orderItemsItem);
            return this;
        }

        public Invoice incrementOrderItemQuantityByOne(String orderItemId) {
            for (ConfirmItemRow item : this.getOrderItems()) {
                if (item.getOrderItemId().equalsIgnoreCase(orderItemId))
                    item.quantity(item.getQuantity() + 1);
            }
            return this;
        }

        @Override public String toString() {
            return "Invoice{" + "invoiceNumber='" + invoiceNumber + '\'' + ", invoiceDate='" + invoiceDate + '\''
                    + ", orderItems=" + orderItems + '}';
        }
    }

}

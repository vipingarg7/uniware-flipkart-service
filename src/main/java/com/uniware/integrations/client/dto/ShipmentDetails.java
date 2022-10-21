package com.uniware.integrations.client.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ShipmentDetails {

    @SerializedName("orderId")
    private String orderId = null;

    @SerializedName("returnAddress")
    private Address returnAddress = null;

    @SerializedName("buyerDetails")
    private BuyerDetails buyerDetails = null;

    @SerializedName("shipmentId")
    private String shipmentId = null;

    @SerializedName("subShipments")
    private List<SubShipment> subShipments;
    @SerializedName("billingAddress")
    private Address billingAddress = null;

    @SerializedName("deliveryAddress")
    private Address deliveryAddress = null;

    @SerializedName("sellerAddress")
    private Address sellerAddress = null;

    @SerializedName("orderItems")
    private List<OrderItem> orderItems = null;

    @SerializedName("locationId")
    private String locationId;

    public ShipmentDetails orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    /**
     * Get orderId
     * @return orderId
     **/
    
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ShipmentDetails returnAddress(Address returnAddress) {
        this.returnAddress = returnAddress;
        return this;
    }

    /**
     * Get returnAddress
     * @return returnAddress
     **/
    
    public Address getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(Address returnAddress) {
        this.returnAddress = returnAddress;
    }

    public ShipmentDetails buyerDetails(BuyerDetails buyerDetails) {
        this.buyerDetails = buyerDetails;
        return this;
    }

    /**
     * Get buyerDetails
     * @return buyerDetails
     **/
    
    public BuyerDetails getBuyerDetails() {
        return buyerDetails;
    }

    public void setBuyerDetails(BuyerDetails buyerDetails) {
        this.buyerDetails = buyerDetails;
    }

    public ShipmentDetails shipmentId(String shipmentId) {
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

    public List<SubShipment> getSubShipments() {
        return subShipments;
    }

    public void setSubShipments(List<SubShipment> subShipments) {
        this.subShipments = subShipments;
    }

    public ShipmentDetails billingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    /**
     * Get billingAddress
     * @return billingAddress
     **/
    
    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public ShipmentDetails deliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    /**
     * Get deliveryAddress
     * @return deliveryAddress
     **/
    
    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public ShipmentDetails sellerAddress(Address sellerAddress) {
        this.sellerAddress = sellerAddress;
        return this;
    }

    /**
     * Get sellerAddress
     * @return sellerAddress
     **/
    
    public Address getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(Address sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public ShipmentDetails orderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public ShipmentDetails addOrderItemsItem(OrderItem orderItemsItem) {
        if (this.orderItems == null) {
            this.orderItems = new java.util.ArrayList<>();
        }
        this.orderItems.add(orderItemsItem);
        return this;
    }

    /**
     * Get orderItems
     * @return orderItems
     **/
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public ShipmentDetails locationId(String locationId) {
        this.locationId = locationId;
        return this;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    @Override public String toString() {
        return "ShipmentDetails{" + "orderId='" + orderId + '\'' + ", returnAddress=" + returnAddress
                + ", buyerDetails=" + buyerDetails + ", shipmentId='" + shipmentId + '\'' + ", subShipments="
                + subShipments + ", billingAddress=" + billingAddress + ", deliveryAddress=" + deliveryAddress
                + ", sellerAddress=" + sellerAddress + ", orderItems=" + orderItems + ", locationId='" + locationId
                + '\'' + '}';
    }

    public static class OrderItem {

        @SerializedName("id")
        private String orderItemId;
        @SerializedName("fragile")
        private boolean fragile;
        @SerializedName("large")
        private boolean large;
        @SerializedName("dangerous")
        private boolean dangerous;

        public String getOrderItemId() {
            return orderItemId;
        }

        public void setOrderItemId(String orderItemId) {
            this.orderItemId = orderItemId;
        }

        public boolean isFragile() {
            return fragile;
        }

        public void setFragile(boolean fragile) {
            this.fragile = fragile;
        }

        public boolean isLarge() {
            return large;
        }

        public void setLarge(boolean large) {
            this.large = large;
        }

        public boolean isDangerous() {
            return dangerous;
        }

        public void setDangerous(boolean dangerous) {
            this.dangerous = dangerous;
        }

        @Override public String toString() {
            return "OrderItem{" + "orderItemId='" + orderItemId + '\'' + ", fragile=" + fragile + ", large=" + large
                    + ", dangerous=" + dangerous + '}';
        }
    }

    public static class SubShipment {

        @SerializedName("shipmentDimensions")
        private Dimensions dimensions = null;

        @SerializedName("courierDetails")
        private Courier courierDetails = null;
        @SerializedName("subShipmentId")
        private String subShipmentId = null;
        
        /**
         * Get dimensions
         * @return dimensions
         **/

        public Dimensions getDimensions() {
            return dimensions;
        }

        public void setDimensions(Dimensions dimensions) {
            this.dimensions = dimensions;
        }

        public Courier getCourierDetails() {
            return courierDetails;
        }

        public void setCourierDetails(Courier courierDetails) {
            this.courierDetails = courierDetails;
        }
        
        /**
         * Get subShipmentId
         * @return subShipmentId
         **/

        public String getSubShipmentId() {
            return subShipmentId;
        }

        public void setSubShipmentId(String subShipmentId) {
            this.subShipmentId = subShipmentId;
        }

        @Override public String toString() {
            return "SubShipment{" + "dimensions=" + dimensions + ", courierDetails=" + courierDetails
                    + ", subShipmentId='" + subShipmentId + '\'' + '}';
        }

    }
}

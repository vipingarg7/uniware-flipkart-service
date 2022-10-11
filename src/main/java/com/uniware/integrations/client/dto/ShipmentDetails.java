package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class ShipmentDetails {

    @JsonProperty("orderId")
    private String orderId = null;

    @JsonProperty("returnAddress")
    private Address returnAddress = null;

    @JsonProperty("buyerDetails")
    private BuyerDetails buyerDetails = null;

    @JsonProperty("shipmentId")
    private String shipmentId = null;
    private List<SubShipments> subShipments;
    @JsonProperty("billingAddress")
    private Address billingAddress = null;

    @JsonProperty("deliveryAddress")
    private Address deliveryAddress = null;

    @JsonProperty("sellerAddress")
    private Address sellerAddress = null;

    @JsonProperty("orderItems")
    private List<OrderItem> orderItems = null;

    @JsonProperty("weighingRequired")
    private Boolean weighingRequired = null;

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

    public List<SubShipments> getSubShipments() {
        return subShipments;
    }

    public void setSubShipments(List<SubShipments> subShipments) {
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

    public ShipmentDetails weighingRequired(Boolean weighingRequired) {
        this.weighingRequired = weighingRequired;
        return this;
    }

    /**
     * Get weighingRequired
     * @return weighingRequired
     **/
    
    public Boolean isWeighingRequired() {
        return weighingRequired;
    }

    public void setWeighingRequired(Boolean weighingRequired) {
        this.weighingRequired = weighingRequired;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShipmentDetails shipmentDetails = (ShipmentDetails) o;
        return Objects.equals(this.orderId, shipmentDetails.orderId) &&
                Objects.equals(this.returnAddress, shipmentDetails.returnAddress) &&
                Objects.equals(this.buyerDetails, shipmentDetails.buyerDetails) &&
                Objects.equals(this.shipmentId, shipmentDetails.shipmentId) &&
                Objects.equals(this.subShipments, shipmentDetails.subShipments) &&
                Objects.equals(this.billingAddress, shipmentDetails.billingAddress) &&
                Objects.equals(this.deliveryAddress, shipmentDetails.deliveryAddress) &&
                Objects.equals(this.sellerAddress, shipmentDetails.sellerAddress) &&
                Objects.equals(this.orderItems, shipmentDetails.orderItems) &&
                Objects.equals(this.weighingRequired, shipmentDetails.weighingRequired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, returnAddress, buyerDetails, shipmentId, subShipments, billingAddress, deliveryAddress, sellerAddress, orderItems, weighingRequired);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ShipmentDetails {\n");

        sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
        sb.append("    returnAddress: ").append(toIndentedString(returnAddress)).append("\n");
        sb.append("    buyerDetails: ").append(toIndentedString(buyerDetails)).append("\n");
        sb.append("    shipmentId: ").append(toIndentedString(shipmentId)).append("\n");
        sb.append("    courierDetails: ").append(toIndentedString(subShipments)).append("\n");
        sb.append("    billingAddress: ").append(toIndentedString(billingAddress)).append("\n");
        sb.append("    deliveryAddress: ").append(toIndentedString(deliveryAddress)).append("\n");
        sb.append("    sellerAddress: ").append(toIndentedString(sellerAddress)).append("\n");
        sb.append("    orderItems: ").append(toIndentedString(orderItems)).append("\n");
        sb.append("    weighingRequired: ").append(toIndentedString(weighingRequired)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}

package com.uniware.integrations.client.dto;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

public class Shipment {

    @SerializedName("forms")
    private List<Form> forms = null;

    @SerializedName("subShipments")
    private List<SubShipment> subShipments = null;

    @SerializedName("dispatchByDate")
    private Date dispatchByDate = null;

    @SerializedName("shipmentId")
    private String shipmentId = null;

    @SerializedName("packagingPolicy")
    private String packagingPolicy = null;

    @SerializedName("shipmentType")
    private String shipmentType = null;

    @SerializedName("locationId")
    private String locationId = null;

    @SerializedName("dispatchAfterDate")
    private Date dispatchAfterDate = null;

    @SerializedName("updatedAt")
    private Date updatedAt = null;

    @SerializedName("hold")
    private Boolean hold = null;

    @SerializedName("orderItems")
    private List<OrderItem> orderItems = null;

    @SerializedName("mps")
    private Boolean mps = null;

    public Shipment forms(List<Form> forms) {
        this.forms = forms;
        return this;
    }

    public Shipment addFormsItem(Form formsItem) {
        if (this.forms == null) {
            this.forms = new java.util.ArrayList<>();
        }
        this.forms.add(formsItem);
        return this;
    }

    /**
     * Get forms
     * @return forms
     **/
    
    public List<Form> getForms() {
        return forms;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

    public Shipment subShipments(List<SubShipment> subShipments) {
        this.subShipments = subShipments;
        return this;
    }

    public Shipment addSubShipmentsItem(SubShipment subShipmentsItem) {
        if (this.subShipments == null) {
            this.subShipments = new java.util.ArrayList<>();
        }
        this.subShipments.add(subShipmentsItem);
        return this;
    }

    /**
     * Get subShipments
     * @return subShipments
     **/
    
    public List<SubShipment> getSubShipments() {
        return subShipments;
    }

    public void setSubShipments(List<SubShipment> subShipments) {
        this.subShipments = subShipments;
    }

    public Shipment dispatchByDate(Date dispatchByDate) {
        this.dispatchByDate = dispatchByDate;
        return this;
    }

    /**
     * Get dispatchByDate
     * @return dispatchByDate
     **/
    
    public Date getDispatchByDate() {
        return dispatchByDate;
    }

    public void setDispatchByDate(Date dispatchByDate) {
        this.dispatchByDate = dispatchByDate;
    }

    public Shipment shipmentId(String shipmentId) {
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

    public Shipment packagingPolicy(String packagingPolicy) {
        this.packagingPolicy = packagingPolicy;
        return this;
    }

    /**
     * Get packagingPolicy
     * @return packagingPolicy
     **/
    
    public String getPackagingPolicy() {
        return packagingPolicy;
    }

    public void setPackagingPolicy(String packagingPolicy) {
        this.packagingPolicy = packagingPolicy;
    }

    public Shipment shipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
        return this;
    }

    /**
     * Get shipmentType
     * @return shipmentType
     **/
    
    public String getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }

    public Shipment locationId(String locationId) {
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

    public Shipment dispatchAfterDate(Date dispatchAfterDate) {
        this.dispatchAfterDate = dispatchAfterDate;
        return this;
    }

    /**
     * Get dispatchAfterDate
     * @return dispatchAfterDate
     **/
    
    public Date getDispatchAfterDate() {
        return dispatchAfterDate;
    }

    public void setDispatchAfterDate(Date dispatchAfterDate) {
        this.dispatchAfterDate = dispatchAfterDate;
    }

    public Shipment updatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Get updatedAt
     * @return updatedAt
     **/
    
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Shipment hold(Boolean hold) {
        this.hold = hold;
        return this;
    }

    /**
     * Get hold
     * @return hold
     **/
    
    public Boolean isHold() {
        return hold;
    }

    public void setHold(Boolean hold) {
        this.hold = hold;
    }

    public Shipment orderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public Shipment addOrderItemsItem(OrderItem orderItemsItem) {
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

    public Shipment mps(Boolean mps) {
        this.mps = mps;
        return this;
    }

    /**
     * Get mps
     * @return mps
     **/
    
    public Boolean isMps() {
        return mps;
    }

    public void setMps(Boolean mps) {
        this.mps = mps;
    }

    @Override public String toString() {
        return "Shipment{" + "forms=" + forms + ", subShipments=" + subShipments + ", dispatchByDate=" + dispatchByDate
                + ", shipmentId='" + shipmentId + '\'' + ", packagingPolicy='" + packagingPolicy + '\''
                + ", shipmentType='" + shipmentType + '\'' + ", locationId='" + locationId + '\''
                + ", dispatchAfterDate=" + dispatchAfterDate + ", updatedAt=" + updatedAt + ", hold=" + hold
                + ", orderItems=" + orderItems + ", mps=" + mps + '}';
    }
}

package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;
import org.joda.time.DateTime;

public class Shipment {

    @JsonProperty("forms")
    private List<Form> forms = null;

    @JsonProperty("subShipments")
    private List<SubShipment> subShipments = null;

    @JsonProperty("dispatchByDate")
    private DateTime dispatchByDate = null;

    @JsonProperty("shipmentId")
    private String shipmentId = null;

    @JsonProperty("packagingPolicy")
    private String packagingPolicy = null;

    @JsonProperty("shipmentType")
    private String shipmentType = null;

    @JsonProperty("locationId")
    private String locationId = null;

    @JsonProperty("dispatchAfterDate")
    private DateTime dispatchAfterDate = null;

    @JsonProperty("updatedAt")
    private DateTime updatedAt = null;

    @JsonProperty("hold")
    private Boolean hold = null;

    @JsonProperty("orderItems")
    private List<OrderItem> orderItems = null;

    @JsonProperty("mps")
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

    public Shipment dispatchByDate(DateTime dispatchByDate) {
        this.dispatchByDate = dispatchByDate;
        return this;
    }

    /**
     * Get dispatchByDate
     * @return dispatchByDate
     **/
    
    public DateTime getDispatchByDate() {
        return dispatchByDate;
    }

    public void setDispatchByDate(DateTime dispatchByDate) {
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

    public Shipment dispatchAfterDate(DateTime dispatchAfterDate) {
        this.dispatchAfterDate = dispatchAfterDate;
        return this;
    }

    /**
     * Get dispatchAfterDate
     * @return dispatchAfterDate
     **/
    
    public DateTime getDispatchAfterDate() {
        return dispatchAfterDate;
    }

    public void setDispatchAfterDate(DateTime dispatchAfterDate) {
        this.dispatchAfterDate = dispatchAfterDate;
    }

    public Shipment updatedAt(DateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Get updatedAt
     * @return updatedAt
     **/
    
    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shipment shipment = (Shipment) o;
        return Objects.equals(this.forms, shipment.forms) &&
                Objects.equals(this.subShipments, shipment.subShipments) &&
                Objects.equals(this.dispatchByDate, shipment.dispatchByDate) &&
                Objects.equals(this.shipmentId, shipment.shipmentId) &&
                Objects.equals(this.packagingPolicy, shipment.packagingPolicy) &&
                Objects.equals(this.shipmentType, shipment.shipmentType) &&
                Objects.equals(this.locationId, shipment.locationId) &&
                Objects.equals(this.dispatchAfterDate, shipment.dispatchAfterDate) &&
                Objects.equals(this.updatedAt, shipment.updatedAt) &&
                Objects.equals(this.hold, shipment.hold) &&
                Objects.equals(this.orderItems, shipment.orderItems) &&
                Objects.equals(this.mps, shipment.mps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(forms, subShipments, dispatchByDate, shipmentId, packagingPolicy, shipmentType, locationId, dispatchAfterDate, updatedAt, hold, orderItems, mps);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Shipment {\n");

        sb.append("    forms: ").append(toIndentedString(forms)).append("\n");
        sb.append("    subShipments: ").append(toIndentedString(subShipments)).append("\n");
        sb.append("    dispatchByDate: ").append(toIndentedString(dispatchByDate)).append("\n");
        sb.append("    shipmentId: ").append(toIndentedString(shipmentId)).append("\n");
        sb.append("    packagingPolicy: ").append(toIndentedString(packagingPolicy)).append("\n");
        sb.append("    shipmentType: ").append(toIndentedString(shipmentType)).append("\n");
        sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
        sb.append("    dispatchAfterDate: ").append(toIndentedString(dispatchAfterDate)).append("\n");
        sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
        sb.append("    hold: ").append(toIndentedString(hold)).append("\n");
        sb.append("    orderItems: ").append(toIndentedString(orderItems)).append("\n");
        sb.append("    mps: ").append(toIndentedString(mps)).append("\n");
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

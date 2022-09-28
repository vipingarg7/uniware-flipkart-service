package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.Objects;
import org.joda.time.DateTime;

public class DispatchRequest {

    @JsonProperty("facilityId")
    private String facilityId = null;

    @JsonProperty("validTrackingLength")
    private Boolean validTrackingLength = null;

    @JsonProperty("dispatchDateValid")
    private Boolean dispatchDateValid = null;

    @JsonProperty("shipmentId")
    private String shipmentId = null;

    @JsonProperty("tentativeDeliveryDateValid")
    private Boolean tentativeDeliveryDateValid = null;

    @JsonProperty("locationId")
    private String locationId = null;

    @JsonProperty("orderItems")
    private java.util.List<ConfirmItemRow> orderItems = new java.util.ArrayList<>();

    @JsonProperty("trackingId")
    private String trackingId = null;

    @JsonProperty("tentativeDeliveryDate")
    private Date tentativeDeliveryDate = null;

    @JsonProperty("deliveryPartner")
    private String deliveryPartner = null;

    @JsonProperty("dispatchDate")
    private Date dispatchDate = null;

    public DispatchRequest incrementOrderItemQuantityByOne(String orderItemId) {
        for (ConfirmItemRow item : this.getOrderItems()) {
            if (item.getOrderItemId().equalsIgnoreCase(orderItemId))
                item.quantity(item.getQuantity() + 1);
        }
        return this;
    }

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

    public DispatchRequest orderItems(java.util.List<ConfirmItemRow> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public DispatchRequest addOrderItemsItem(ConfirmItemRow orderItemsItem) {
        this.orderItems.add(orderItemsItem);
        return this;
    }

    /**
     * Get orderItems
     * @return orderItems
     **/
    
    public java.util.List<ConfirmItemRow> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(java.util.List<ConfirmItemRow> orderItems) {
        this.orderItems = orderItems;
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

    public DispatchRequest tentativeDeliveryDate(Date tentativeDeliveryDate) {
        this.tentativeDeliveryDate = tentativeDeliveryDate;
        return this;
    }

    /**
     * Get tentativeDeliveryDate
     * @return tentativeDeliveryDate
     **/
    
    public Date getTentativeDeliveryDate() {
        return tentativeDeliveryDate;
    }

    public void setTentativeDeliveryDate(Date tentativeDeliveryDate) {
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

    public DispatchRequest dispatchDate(Date dispatchDate) {
        this.dispatchDate = dispatchDate;
        return this;
    }

    /**
     * Get dispatchDate
     * @return dispatchDate
     **/
    
    public Date getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DispatchRequest dispatchRequest = (DispatchRequest) o;
        return Objects.equals(this.facilityId, dispatchRequest.facilityId) &&
                Objects.equals(this.validTrackingLength, dispatchRequest.validTrackingLength) &&
                Objects.equals(this.dispatchDateValid, dispatchRequest.dispatchDateValid) &&
                Objects.equals(this.shipmentId, dispatchRequest.shipmentId) &&
                Objects.equals(this.tentativeDeliveryDateValid, dispatchRequest.tentativeDeliveryDateValid) &&
                Objects.equals(this.locationId, dispatchRequest.locationId) &&
                Objects.equals(this.orderItems, dispatchRequest.orderItems) &&
                Objects.equals(this.trackingId, dispatchRequest.trackingId) &&
                Objects.equals(this.tentativeDeliveryDate, dispatchRequest.tentativeDeliveryDate) &&
                Objects.equals(this.deliveryPartner, dispatchRequest.deliveryPartner) &&
                Objects.equals(this.dispatchDate, dispatchRequest.dispatchDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facilityId, validTrackingLength, dispatchDateValid, shipmentId, tentativeDeliveryDateValid, locationId, orderItems, trackingId, tentativeDeliveryDate, deliveryPartner, dispatchDate);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DispatchRequest {\n");

        sb.append("    facilityId: ").append(toIndentedString(facilityId)).append("\n");
        sb.append("    validTrackingLength: ").append(toIndentedString(validTrackingLength)).append("\n");
        sb.append("    dispatchDateValid: ").append(toIndentedString(dispatchDateValid)).append("\n");
        sb.append("    shipmentId: ").append(toIndentedString(shipmentId)).append("\n");
        sb.append("    tentativeDeliveryDateValid: ").append(toIndentedString(tentativeDeliveryDateValid)).append("\n");
        sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
        sb.append("    orderItems: ").append(toIndentedString(orderItems)).append("\n");
        sb.append("    trackingId: ").append(toIndentedString(trackingId)).append("\n");
        sb.append("    tentativeDeliveryDate: ").append(toIndentedString(tentativeDeliveryDate)).append("\n");
        sb.append("    deliveryPartner: ").append(toIndentedString(deliveryPartner)).append("\n");
        sb.append("    dispatchDate: ").append(toIndentedString(dispatchDate)).append("\n");
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

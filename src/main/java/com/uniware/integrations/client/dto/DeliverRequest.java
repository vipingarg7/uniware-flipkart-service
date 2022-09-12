package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.joda.time.DateTime;

public class DeliverRequest {

    @JsonProperty("deliveryDate")
    private DateTime deliveryDate = null;

    @JsonProperty("deliveryDateValid")
    private Boolean deliveryDateValid = null;

    @JsonProperty("locationId")
    private String locationId = null;

    @JsonProperty("shipmentId")
    private String shipmentId = null;

    public DeliverRequest deliveryDate(DateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    /**
     * Get deliveryDate
     * @return deliveryDate
     **/

    public DateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(DateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public DeliverRequest deliveryDateValid(Boolean deliveryDateValid) {
        this.deliveryDateValid = deliveryDateValid;
        return this;
    }

    /**
     * Get deliveryDateValid
     * @return deliveryDateValid
     **/

    public Boolean isDeliveryDateValid() {
        return deliveryDateValid;
    }

    public void setDeliveryDateValid(Boolean deliveryDateValid) {
        this.deliveryDateValid = deliveryDateValid;
    }

    public DeliverRequest locationId(String locationId) {
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

    public DeliverRequest shipmentId(String shipmentId) {
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeliverRequest deliverRequest = (DeliverRequest) o;
        return Objects.equals(this.deliveryDate, deliverRequest.deliveryDate) &&
                Objects.equals(this.deliveryDateValid, deliverRequest.deliveryDateValid) &&
                Objects.equals(this.locationId, deliverRequest.locationId) &&
                Objects.equals(this.shipmentId, deliverRequest.shipmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryDate, deliveryDateValid, locationId, shipmentId);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DeliverRequest {\n");

        sb.append("    deliveryDate: ").append(toIndentedString(deliveryDate)).append("\n");
        sb.append("    deliveryDateValid: ").append(toIndentedString(deliveryDateValid)).append("\n");
        sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
        sb.append("    shipmentId: ").append(toIndentedString(shipmentId)).append("\n");
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

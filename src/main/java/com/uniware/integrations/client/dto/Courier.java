package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class Courier {

    @SerializedName("pickupDetails")
    private CourierDetails pickupDetails = null;

    @SerializedName("deliveryDetails")
    private CourierDetails deliveryDetails = null;

    public Courier pickupDetails(CourierDetails pickupDetails) {
        this.pickupDetails = pickupDetails;
        return this;
    }

    /**
     * Get pickupDetails
     * @return pickupDetails
     **/
    
    public CourierDetails getPickupDetails() {
        return pickupDetails;
    }

    public void setPickupDetails(CourierDetails pickupDetails) {
        this.pickupDetails = pickupDetails;
    }

    public Courier deliveryDetails(CourierDetails deliveryDetails) {
        this.deliveryDetails = deliveryDetails;
        return this;
    }

    /**
     * Get deliveryDetails
     * @return deliveryDetails
     **/
    
    public CourierDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    public void setDeliveryDetails(CourierDetails deliveryDetails) {
        this.deliveryDetails = deliveryDetails;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Courier courier = (Courier) o;
        return Objects.equals(this.pickupDetails, courier.pickupDetails) &&
                Objects.equals(this.deliveryDetails, courier.deliveryDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pickupDetails, deliveryDetails);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Courier {\n");
        sb.append("    pickupDetails: ").append(toIndentedString(pickupDetails)).append("\n");
        sb.append("    deliveryDetails: ").append(toIndentedString(deliveryDetails)).append("\n");
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

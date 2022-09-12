package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class CourierDetails {

    @JsonProperty("vendorName")
    private String vendorName = null;

    @JsonProperty("trackingId")
    private String trackingId = null;

    public CourierDetails vendorName(String vendorName) {
        this.vendorName = vendorName;
        return this;
    }

    /**
     * Get vendorName
     * @return vendorName
     **/
    
    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public CourierDetails trackingId(String trackingId) {
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CourierDetails courierDetails = (CourierDetails) o;
        return Objects.equals(this.vendorName, courierDetails.vendorName) &&
                Objects.equals(this.trackingId, courierDetails.trackingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendorName, trackingId);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CourierDetails {\n");

        sb.append("    vendorName: ").append(toIndentedString(vendorName)).append("\n");
        sb.append("    trackingId: ").append(toIndentedString(trackingId)).append("\n");
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

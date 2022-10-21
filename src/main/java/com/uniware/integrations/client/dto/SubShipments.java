package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SubShipments {

    @JsonProperty("dimensions")
    private Dimensions dimensions = null;
    @JsonProperty("subShipmentId")
    private String subShipmentId = null;

    public SubShipments dimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
        return this;
    }

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

    public SubShipments subShipmentId(String subShipmentId) {
        this.subShipmentId = subShipmentId;
        return this;
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubShipments subShipments = (SubShipments) o;
        return Objects.equals(this.dimensions, subShipments.dimensions) &&
                Objects.equals(this.subShipmentId, subShipments.subShipmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimensions, subShipmentId);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SubShipments {\n");

        sb.append("    dimensions: ").append(toIndentedString(dimensions)).append("\n");
        sb.append("    subShipmentId: ").append(toIndentedString(subShipmentId)).append("\n");
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

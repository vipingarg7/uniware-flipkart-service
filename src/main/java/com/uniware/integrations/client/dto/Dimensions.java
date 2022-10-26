package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.Objects;

public class Dimensions {

    @SerializedName("breadth")
    private BigDecimal breadth = null;

    @SerializedName("length")
    private BigDecimal length = null;

    @SerializedName("weight")
    private BigDecimal weight = null;

    @SerializedName("height")
    private BigDecimal height = null;

    public Dimensions breadth(BigDecimal breadth) {
        this.breadth = breadth;
        return this;
    }

    /**
     * Get breadth
     * @return breadth
     **/
    
    public BigDecimal getBreadth() {
        return breadth;
    }

    public void setBreadth(BigDecimal breadth) {
        this.breadth = breadth;
    }

    public Dimensions length(BigDecimal length) {
        this.length = length;
        return this;
    }

    /**
     * Get length
     * @return length
     **/
    
    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public Dimensions weight(BigDecimal weight) {
        this.weight = weight;
        return this;
    }

    /**
     * Get weight
     * @return weight
     **/
    
    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Dimensions height(BigDecimal height) {
        this.height = height;
        return this;
    }

    /**
     * Get height
     * @return height
     **/
    
    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public Dimensions defaultDimensions(){
        this.length = BigDecimal.valueOf(10);
        this.breadth = BigDecimal.valueOf(10);
        this.height = BigDecimal.valueOf(10);
        this.weight = BigDecimal.valueOf(0.1);
        return this;
    }
    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dimensions dimensions = (Dimensions) o;
        return Objects.equals(this.breadth, dimensions.breadth) &&
                Objects.equals(this.length, dimensions.length) &&
                Objects.equals(this.weight, dimensions.weight) &&
                Objects.equals(this.height, dimensions.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(breadth, length, weight, height);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Dimensions {\n");

        sb.append("    breadth: ").append(toIndentedString(breadth)).append("\n");
        sb.append("    length: ").append(toIndentedString(length)).append("\n");
        sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
        sb.append("    height: ").append(toIndentedString(height)).append("\n");
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

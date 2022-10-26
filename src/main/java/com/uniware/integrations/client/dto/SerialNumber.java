package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SerialNumber {

    @SerializedName("serialNumbers")
    private List<List<String>> serialNumbers = null;

    @SerializedName("orderItemId")
    private String orderItemId = null;

    public SerialNumber serialNumbers(List<List<String>> serialNumbers) {
        this.serialNumbers = serialNumbers;
        return this;
    }

    public SerialNumber addSerialNumbersItem(List<String> serialNumbersItem) {
        if (this.serialNumbers == null) {
            this.serialNumbers = new ArrayList<>();
        }
        this.serialNumbers.add(serialNumbersItem);
        return this;
    }

    /**
     * Get serialNumbers
     * @return serialNumbers
     **/
    
    public List<List<String>> getSerialNumbers() {
        return serialNumbers;
    }

    public void setSerialNumbers(List<List<String>> serialNumbers) {
        this.serialNumbers = serialNumbers;
    }

    public SerialNumber orderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
        return this;
    }

    /**
     * Get orderItemId
     * @return orderItemId
     **/
    
    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SerialNumber serialNumber = (SerialNumber) o;
        return Objects.equals(this.serialNumbers, serialNumber.serialNumbers) &&
                Objects.equals(this.orderItemId, serialNumber.orderItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumbers, orderItemId);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SerialNumber {\n");

        sb.append("    serialNumbers: ").append(toIndentedString(serialNumbers)).append("\n");
        sb.append("    orderItemId: ").append(toIndentedString(orderItemId)).append("\n");
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

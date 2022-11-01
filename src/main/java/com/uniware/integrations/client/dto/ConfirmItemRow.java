package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.Objects;
import org.joda.time.LocalDate;

public class ConfirmItemRow {

    @SerializedName("orderItemId")
    private String orderItemId = null;

    @SerializedName("serialNumbers")
    private java.util.List<java.util.List<String>> serialNumbers = null;

    @SerializedName("quantity")
    private Integer quantity = null;

    public ConfirmItemRow orderItemId(String orderItemId) {
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

    public ConfirmItemRow serialNumbers(java.util.List<java.util.List<String>> serialNumbers) {
        this.serialNumbers = serialNumbers;
        return this;
    }

    public ConfirmItemRow addSerialNumbersItem(java.util.List<String> serialNumbersItem) {
        if (this.serialNumbers == null) {
            this.serialNumbers = new java.util.ArrayList<>();
        }
        this.serialNumbers.add(serialNumbersItem);
        return this;
    }

    /**
     * Get serialNumbers
     * @return serialNumbers
     **/
    
    public java.util.List<java.util.List<String>> getSerialNumbers() {
        return serialNumbers;
    }

    public void setSerialNumbers(java.util.List<java.util.List<String>> serialNumbers) {
        this.serialNumbers = serialNumbers;
    }

    public ConfirmItemRow quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * Get quantity
     * minimum: 1
     * @return quantity
     **/
    
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ConfirmItemRow {\n");

        sb.append("    orderItemId: ").append(toIndentedString(orderItemId)).append("\n");
        sb.append("    serialNumbers: ").append(toIndentedString(serialNumbers)).append("\n");
        sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
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

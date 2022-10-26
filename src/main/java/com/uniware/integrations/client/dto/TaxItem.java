package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.Objects;

public class TaxItem {

    @SerializedName("taxRate")
    private BigDecimal taxRate = null;

    @SerializedName("orderItemId")
    private String orderItemId = null;

    @SerializedName("quantity")
    private Integer quantity = null;

    public TaxItem() {};

    public TaxItem(BigDecimal taxRate, String orderItemId, Integer quantity) {
        this.taxRate = taxRate;
        this.orderItemId = orderItemId;
        this.quantity = quantity;
    }

    public TaxItem taxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
        return this;
    }

    /**
     * Get taxRate
     * @return taxRate
     **/
    
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public TaxItem orderItemId(String orderItemId) {
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

    public TaxItem quantity(Integer quantity) {
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
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaxItem taxItem = (TaxItem) o;
        return Objects.equals(this.taxRate, taxItem.taxRate) &&
                Objects.equals(this.orderItemId, taxItem.orderItemId) &&
                Objects.equals(this.quantity, taxItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taxRate, orderItemId, quantity);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TaxItem {\n");

        sb.append("    taxRate: ").append(toIndentedString(taxRate)).append("\n");
        sb.append("    orderItemId: ").append(toIndentedString(orderItemId)).append("\n");
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

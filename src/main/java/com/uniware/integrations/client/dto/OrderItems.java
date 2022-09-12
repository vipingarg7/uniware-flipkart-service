package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;

public class OrderItems {

    @JsonProperty("invoiceAmount")
    private BigDecimal invoiceAmount = null;

    @JsonProperty("taxRate")
    private BigDecimal taxRate = null;

    @JsonProperty("serialNumbers")
    private java.util.List<java.util.List<String>> serialNumbers = null;

    @JsonProperty("orderItemId")
    private String orderItemId = null;

    @JsonProperty("taxDetails")
    private TaxDetails taxDetails = null;

    public OrderItems invoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
        return this;
    }

    /**
     * Get invoiceAmount
     * @return invoiceAmount
     **/
    
    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public OrderItems taxRate(BigDecimal taxRate) {
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

    public OrderItems serialNumbers(java.util.List<java.util.List<String>> serialNumbers) {
        this.serialNumbers = serialNumbers;
        return this;
    }

    public OrderItems addSerialNumbersItem(java.util.List<String> serialNumbersItem) {
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

    public OrderItems orderItemId(String orderItemId) {
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

    public OrderItems taxDetails(TaxDetails taxDetails) {
        this.taxDetails = taxDetails;
        return this;
    }

    /**
     * Get taxDetails
     * @return taxDetails
     **/
    
    public TaxDetails getTaxDetails() {
        return taxDetails;
    }

    public void setTaxDetails(TaxDetails taxDetails) {
        this.taxDetails = taxDetails;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderItems orderItems = (OrderItems) o;
        return Objects.equals(this.invoiceAmount, orderItems.invoiceAmount) &&
                Objects.equals(this.taxRate, orderItems.taxRate) &&
                Objects.equals(this.serialNumbers, orderItems.serialNumbers) &&
                Objects.equals(this.orderItemId, orderItems.orderItemId) &&
                Objects.equals(this.taxDetails, orderItems.taxDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceAmount, taxRate, serialNumbers, orderItemId, taxDetails);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OrderItems {\n");

        sb.append("    invoiceAmount: ").append(toIndentedString(invoiceAmount)).append("\n");
        sb.append("    taxRate: ").append(toIndentedString(taxRate)).append("\n");
        sb.append("    serialNumbers: ").append(toIndentedString(serialNumbers)).append("\n");
        sb.append("    orderItemId: ").append(toIndentedString(orderItemId)).append("\n");
        sb.append("    taxDetails: ").append(toIndentedString(taxDetails)).append("\n");
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

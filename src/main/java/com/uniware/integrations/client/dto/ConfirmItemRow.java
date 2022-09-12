package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;
import org.joda.time.LocalDate;

public class ConfirmItemRow {

    @JsonProperty("taxRate")
    private BigDecimal taxRate = null;

    @JsonProperty("orderItemId")
    private String orderItemId = null;

    @JsonProperty("invoiceDate")
    private LocalDate invoiceDate = null;

    @JsonProperty("serialNumbers")
    private java.util.List<java.util.List<String>> serialNumbers = null;

    @JsonProperty("invoiceNumber")
    private String invoiceNumber = null;

    @JsonProperty("quantity")
    private Integer quantity = null;

    public ConfirmItemRow taxRate(BigDecimal taxRate) {
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

    public ConfirmItemRow invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    /**
     * Get invoiceDate
     * @return invoiceDate
     **/
    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
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

    public ConfirmItemRow invoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    /**
     * Get invoiceNumber
     * @return invoiceNumber
     **/
    
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfirmItemRow confirmItemRow = (ConfirmItemRow) o;
        return Objects.equals(this.taxRate, confirmItemRow.taxRate) &&
                Objects.equals(this.orderItemId, confirmItemRow.orderItemId) &&
                Objects.equals(this.invoiceDate, confirmItemRow.invoiceDate) &&
                Objects.equals(this.serialNumbers, confirmItemRow.serialNumbers) &&
                Objects.equals(this.invoiceNumber, confirmItemRow.invoiceNumber) &&
                Objects.equals(this.quantity, confirmItemRow.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taxRate, orderItemId, invoiceDate, serialNumbers, invoiceNumber, quantity);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ConfirmItemRow {\n");

        sb.append("    taxRate: ").append(toIndentedString(taxRate)).append("\n");
        sb.append("    orderItemId: ").append(toIndentedString(orderItemId)).append("\n");
        sb.append("    invoiceDate: ").append(toIndentedString(invoiceDate)).append("\n");
        sb.append("    serialNumbers: ").append(toIndentedString(serialNumbers)).append("\n");
        sb.append("    invoiceNumber: ").append(toIndentedString(invoiceNumber)).append("\n");
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

package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;
import org.joda.time.LocalDate;

public class InvoiceDetails {

    @JsonProperty("taxRate")
    private BigDecimal taxRate = null;

    @JsonProperty("orderItemId")
    private String orderItemId = null;

    @JsonProperty("invoiceDate")
    private LocalDate invoiceDate = null;

    @JsonProperty("serialNumbers")
    private java.util.List<java.util.List<String>> serialNumbers = null;

    @JsonProperty("invoiceAmount")
    private BigDecimal invoiceAmount = null;

    @JsonProperty("invoiceNumber")
    private String invoiceNumber = null;

    @JsonProperty("taxDetails")
    private TaxDetails taxDetails = null;

    public InvoiceDetails taxRate(BigDecimal taxRate) {
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

    public InvoiceDetails orderItemId(String orderItemId) {
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

    public InvoiceDetails invoiceDate(LocalDate invoiceDate) {
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

    public InvoiceDetails serialNumbers(java.util.List<java.util.List<String>> serialNumbers) {
        this.serialNumbers = serialNumbers;
        return this;
    }

    public InvoiceDetails addSerialNumbersItem(java.util.List<String> serialNumbersItem) {
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

    public InvoiceDetails invoiceAmount(BigDecimal invoiceAmount) {
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

    public InvoiceDetails invoiceNumber(String invoiceNumber) {
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

    public InvoiceDetails taxDetails(TaxDetails taxDetails) {
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
        InvoiceDetails invoiceDetails = (InvoiceDetails) o;
        return Objects.equals(this.taxRate, invoiceDetails.taxRate) &&
                Objects.equals(this.orderItemId, invoiceDetails.orderItemId) &&
                Objects.equals(this.invoiceDate, invoiceDetails.invoiceDate) &&
                Objects.equals(this.serialNumbers, invoiceDetails.serialNumbers) &&
                Objects.equals(this.invoiceAmount, invoiceDetails.invoiceAmount) &&
                Objects.equals(this.invoiceNumber, invoiceDetails.invoiceNumber) &&
                Objects.equals(this.taxDetails, invoiceDetails.taxDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taxRate, orderItemId, invoiceDate, serialNumbers, invoiceAmount, invoiceNumber, taxDetails);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class InvoiceDetails {\n");

        sb.append("    taxRate: ").append(toIndentedString(taxRate)).append("\n");
        sb.append("    orderItemId: ").append(toIndentedString(orderItemId)).append("\n");
        sb.append("    invoiceDate: ").append(toIndentedString(invoiceDate)).append("\n");
        sb.append("    serialNumbers: ").append(toIndentedString(serialNumbers)).append("\n");
        sb.append("    invoiceAmount: ").append(toIndentedString(invoiceAmount)).append("\n");
        sb.append("    invoiceNumber: ").append(toIndentedString(invoiceNumber)).append("\n");
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

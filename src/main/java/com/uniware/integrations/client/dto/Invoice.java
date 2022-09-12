package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.joda.time.LocalDate;

public class Invoice {

    @JsonProperty("invoiceNumber")
    private String invoiceNumber = null;

    @JsonProperty("orderItems")
    private java.util.List<OrderItems> orderItems = null;

    @JsonProperty("invoiceDate")
    private LocalDate invoiceDate = null;

    @JsonProperty("orderId")
    private String orderId = null;

    @JsonProperty("shipmentId")
    private String shipmentId = null;

    public Invoice invoiceNumber(String invoiceNumber) {
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

    public Invoice orderItems(java.util.List<OrderItems> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public Invoice addOrderItemsItem(OrderItems orderItemsItem) {
        if (this.orderItems == null) {
            this.orderItems = new java.util.ArrayList<>();
        }
        this.orderItems.add(orderItemsItem);
        return this;
    }

    /**
     * Get orderItems
     * @return orderItems
     **/
    
    public java.util.List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(java.util.List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }

    public Invoice invoiceDate(LocalDate invoiceDate) {
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

    public Invoice orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    /**
     * Get orderId
     * @return orderId
     **/
    
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Invoice invoice = (Invoice) o;
        return Objects.equals(this.invoiceNumber, invoice.invoiceNumber) &&
                Objects.equals(this.orderItems, invoice.orderItems) &&
                Objects.equals(this.invoiceDate, invoice.invoiceDate) &&
                Objects.equals(this.orderId, invoice.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceNumber, orderItems, invoiceDate, orderId);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Invoice {\n");

        sb.append("    invoiceNumber: ").append(toIndentedString(invoiceNumber)).append("\n");
        sb.append("    orderItems: ").append(toIndentedString(orderItems)).append("\n");
        sb.append("    invoiceDate: ").append(toIndentedString(invoiceDate)).append("\n");
        sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
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

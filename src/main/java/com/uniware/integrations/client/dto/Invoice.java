package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;
import org.joda.time.LocalDate;

public class Invoice {

    @JsonProperty("invoiceNumber")
    private String invoiceNumber = null;

    @JsonProperty("orderItems")
    private java.util.List<OrderItem> orderItems = null;

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

    public Invoice orderItems(java.util.List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public Invoice addOrderItemsItem(OrderItem orderItemsItem) {
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
    
    public java.util.List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(java.util.List<OrderItem> orderItems) {
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

    public static class OrderItem {

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

        public OrderItem invoiceAmount(BigDecimal invoiceAmount) {
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

        public OrderItem taxRate(BigDecimal taxRate) {
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

        public OrderItem serialNumbers(java.util.List<java.util.List<String>> serialNumbers) {
            this.serialNumbers = serialNumbers;
            return this;
        }

        public OrderItem addSerialNumbersItem(java.util.List<String> serialNumbersItem) {
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

        public OrderItem orderItemId(String orderItemId) {
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

        public OrderItem taxDetails(TaxDetails taxDetails) {
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
            OrderItem orderItems = (OrderItem) o;
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

}

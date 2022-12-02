package com.uniware.integrations.client.dto;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Invoice {

    @SerializedName("invoiceNumber")
    private String invoiceNumber = null;

    @SerializedName("orderItems")
    private List<OrderItem> orderItems = null;

    @SerializedName("invoiceDate")
    private String invoiceDate = null;

    @SerializedName("orderId")
    private String orderId = null;

    @SerializedName("shipmentId")
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

    public Invoice orderItems(List<OrderItem> orderItems) {
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
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Invoice invoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    /**
     * Get invoiceDate
     * @return invoiceDate
     **/
    
    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
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

    @Override public String toString() {
        return "Invoice{" + "invoiceNumber='" + invoiceNumber + '\'' + ", orderItems=" + orderItems + ", invoiceDate="
                + invoiceDate + ", orderId='" + orderId + '\'' + ", shipmentId='" + shipmentId + '\'' + '}';
    }

    public static class OrderItem {

        @SerializedName("invoiceAmount")
        private BigDecimal invoiceAmount = null;

        @SerializedName("taxRate")
        private BigDecimal taxRate = null;

        @SerializedName("serialNumbers")
        private List<List<String>> serialNumbers = null;

        @SerializedName("orderItemId")
        private String orderItemId = null;

        @SerializedName("taxDetails")
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

        public OrderItem serialNumbers(List<List<String>> serialNumbers) {
            this.serialNumbers = serialNumbers;
            return this;
        }

        public OrderItem addSerialNumbersItem(List<String> serialNumbersItem) {
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

        public List<List<String>> getSerialNumbers() {
            return serialNumbers;
        }

        public void setSerialNumbers(List<List<String>> serialNumbers) {
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

        @Override public String toString() {
            return "OrderItem{" + "invoiceAmount=" + invoiceAmount + ", taxRate=" + taxRate + ", serialNumbers="
                    + serialNumbers + ", orderItemId='" + orderItemId + '\'' + ", taxDetails=" + taxDetails + '}';
        }

    }

}

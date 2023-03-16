package com.uniware.integrations.client.dto;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @Data
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

        public OrderItem addSerialNumbersItem(List<String> serialNumbersItem) {
            if (this.serialNumbers == null) {
                this.serialNumbers = new java.util.ArrayList<>();
            }
            this.serialNumbers.add(serialNumbersItem);
            return this;
        }

    }

}

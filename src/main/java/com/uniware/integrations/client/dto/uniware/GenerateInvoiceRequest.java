package com.uniware.integrations.client.dto.uniware;

import java.math.BigDecimal;
import java.util.List;

public class GenerateInvoiceRequest {
    
    private ShippingPackage shippingPackage;

    public ShippingPackage getShippingPackage() {
        return shippingPackage;
    }

    public void setShippingPackage(ShippingPackage shippingPackage) {
        this.shippingPackage = shippingPackage;
    }

    public static class ShippingPackage {

        private List<SaleOrderItem> saleOrderItems;

        private String invoiceCode;

        private SaleOrder saleOrder;

        private String code;

        private String facilityCode;

        private BigDecimal length;

        private BigDecimal breadth;

        private BigDecimal height;

        private BigDecimal weight;

        public String getFacilityCode() {
            return facilityCode;
        }

        public void setFacilityCode(String facilityCode) {
            this.facilityCode = facilityCode;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<SaleOrderItem> getSaleOrderItems() {
            return saleOrderItems;
        }

        public void setSaleOrderItems(List<SaleOrderItem> saleOrderItems) {
            this.saleOrderItems = saleOrderItems;
        }

        public String getInvoiceCode() {
            return invoiceCode;
        }

        public void setInvoiceCode(String invoiceCode) {
            this.invoiceCode = invoiceCode;
        }

        public SaleOrder getSaleOrder() {
            return saleOrder;
        }

        public void setSaleOrder(SaleOrder saleOrder) {
            this.saleOrder = saleOrder;
        }

        public BigDecimal getLength() {
            return length;
        }

        public void setLength(BigDecimal length) {
            this.length = length;
        }

        public BigDecimal getBreadth() {
            return breadth;
        }

        public void setBreadth(BigDecimal breadth) {
            this.breadth = breadth;
        }

        public BigDecimal getHeight() {
            return height;
        }

        public void setHeight(BigDecimal height) {
            this.height = height;
        }

        public BigDecimal getWeight() {
            return weight;
        }

        public void setWeight(BigDecimal weight) {
            this.weight = weight;
        }
    }

    public static class SaleOrderItem {

        private String code;

        private String combinationIdentifier;

        private String statusCode;

        private String channelSaleOrderItemCode;

        private String bundleSkuCode;

        private String channelProductId;

        private String itemDetails;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCombinationIdentifier() {
            return combinationIdentifier;
        }

        public void setCombinationIdentifier(String combinationIdentifier) {
            this.combinationIdentifier = combinationIdentifier;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public String getChannelSaleOrderItemCode() {
            return channelSaleOrderItemCode;
        }

        public void setChannelSaleOrderItemCode(String channelSaleOrderItemCode) {
            this.channelSaleOrderItemCode = channelSaleOrderItemCode;
        }

        public String getBundleSkuCode() {
            return bundleSkuCode;
        }

        public void setBundleSkuCode(String bundleSkuCode) {
            this.bundleSkuCode = bundleSkuCode;
        }

        public String getChannelProductId() {
            return channelProductId;
        }

        public void setChannelProductId(String channelProductId) {
            this.channelProductId = channelProductId;
        }

        public String getItemDetails() {
            return itemDetails;
        }

        public void setItemDetails(String itemDetails) {
            this.itemDetails = itemDetails;
        }
    }

    public static class SaleOrder {

        private String code;
        private String displayOrderCode;
        private String additionalInfo;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getAdditionalInfo() {
            return additionalInfo;
        }

        public void setAdditionalInfo(String additionalInfo) {
            this.additionalInfo = additionalInfo;
        }

        public String getDisplayOrderCode() {   return displayOrderCode;    }

        public void setDisplayOrderCode(String displayOrderCode) {  this.displayOrderCode = displayOrderCode;   }
    }

}

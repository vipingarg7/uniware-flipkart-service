package com.uniware.integrations.client.dto.uniware;

import java.util.List;

public class GenerateLabelRequest {

    private String saleOrderCode;
    private String shippingPackageCode;
    private String invoiceCode;
    private List<SaleOrderItem> saleOrderItems;

    public String getSaleOrderCode() {
        return saleOrderCode;
    }

    public void setSaleOrderCode(String saleOrderCode) {
        this.saleOrderCode = saleOrderCode;
    }

    public String getShippingPackageCode() {
        return shippingPackageCode;
    }

    public void setShippingPackageCode(String shippingPackageCode) {
        this.shippingPackageCode = shippingPackageCode;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public List<SaleOrderItem> getSaleOrderItems() {
        return saleOrderItems;
    }

    public void setSaleOrderItems(List<SaleOrderItem> saleOrderItems) {
        this.saleOrderItems = saleOrderItems;
    }
}

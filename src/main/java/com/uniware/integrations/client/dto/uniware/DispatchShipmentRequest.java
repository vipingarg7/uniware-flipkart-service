package com.uniware.integrations.client.dto.uniware;

import com.uniware.integrations.uniware.dto.rest.api.SaleOrderItem;
import java.util.Date;
import java.util.List;

public class DispatchShipmentRequest {

    private String  saleOrderCode;
    private List<SaleOrderItem> saleOrderItems;
    private String invoiceCode;
    private Date invoiceDate;
    private String shippingProvider;
    private String trackingNumber;
    private String  shippingManager;
    private boolean isShippingProviderIsAggregator;
    private String  aggregatorAllocatedCourier;
    private Date tentativeDeliveryDate;
    private Date dispatchDate;

    public String getSaleOrderCode() {
        return saleOrderCode;
    }

    public void setSaleOrderCode(String saleOrderCode) {
        this.saleOrderCode = saleOrderCode;
    }

    public List<SaleOrderItem> getSaleOrderItems() {
        return saleOrderItems;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getShippingProvider() {
        return shippingProvider;
    }

    public void setShippingProvider(String shippingProvider) {
        this.shippingProvider = shippingProvider;
    }

    public void setSaleOrderItems(List<SaleOrderItem> saleOrderItems) {
        this.saleOrderItems = saleOrderItems;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getShippingManager() {
        return shippingManager;
    }

    public boolean isShippingProviderIsAggregator() {
        return isShippingProviderIsAggregator;
    }

    public void setShippingProviderIsAggregator(boolean shippingProviderIsAggregator) {
        isShippingProviderIsAggregator = shippingProviderIsAggregator;
    }

    public String getAggregatorAllocatedCourier() {
        return aggregatorAllocatedCourier;
    }

    public void setAggregatorAllocatedCourier(String aggregatorAllocatedCourier) {
        this.aggregatorAllocatedCourier = aggregatorAllocatedCourier;
    }

    public void setShippingManager(String shippingManager) {
        this.shippingManager = shippingManager;
    }

    public Date getTentativeDeliveryDate() {
        return tentativeDeliveryDate;
    }

    public void setTentativeDeliveryDate(Date tentativeDeliveryDate) {
        this.tentativeDeliveryDate = tentativeDeliveryDate;
    }

    public Date getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }
}

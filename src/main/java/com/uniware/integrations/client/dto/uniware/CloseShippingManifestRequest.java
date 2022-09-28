package com.uniware.integrations.client.dto.uniware;

import com.uniware.integrations.utils.DateUtils;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;

public class CloseShippingManifestRequest {

    private String  shippingManifestCode;
    private String  shippingManager;
    private String  shippingProvider;
    private boolean isShippingProviderIsAggregator;
    private String  aggregatorAllocatedCourier;
    private List<ShippingManifestItems> shippingManifestItems;

    public String getShippingManifestCode() {
        return shippingManifestCode;
    }

    public void setShippingManifestCode(String shippingManifestCode) {
        this.shippingManifestCode = shippingManifestCode;
    }

    public String getShippingManager() {
        return shippingManager;
    }

    public void setShippingManager(String shippingManager) {
        this.shippingManager = shippingManager;
    }

    public String getShippingProvider() {
        return shippingProvider;
    }

    public void setShippingProvider(String shippingProvider) {
        this.shippingProvider = shippingProvider;
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

    public List<ShippingManifestItems> getShippingManifestItems() {
        return shippingManifestItems;
    }

    public void setShippingManifestItems(List<ShippingManifestItems> shippingManifestItems)
    {
        this.shippingManifestItems = shippingManifestItems;
    }

    public static class ShippingManifestItems {

        private String saleOrderCode;
        private String shippingPackageCode;
        private String trackingNumber;
        private String invoiceCode;
        private Date invoiceDate;
        private Date dispatchDate;
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

        public String getTrackingNumber() {
            return trackingNumber;
        }

        public void setTrackingNumber(String trackingNumber) {
            this.trackingNumber = trackingNumber;
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

        public Date getDispatchDate() {
            return dispatchDate;
        }

        public void setDispatchDate(Date dispatchDate) {
            this.dispatchDate = dispatchDate;
        }

        public void setInvoiceDate(Date invoiceDate) {
            this.invoiceDate = invoiceDate;
        }

        public List<SaleOrderItem> getSaleOrderItems() {
            return saleOrderItems;
        }

        public void setSaleOrderItems(List<SaleOrderItem> saleOrderItems) {
            this.saleOrderItems = saleOrderItems;
        }
    }


}

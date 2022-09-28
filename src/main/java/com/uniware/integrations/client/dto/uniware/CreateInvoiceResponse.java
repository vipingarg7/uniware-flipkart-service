package com.uniware.integrations.client.dto.uniware;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class CreateInvoiceResponse {

    private String invoiceCode;

    private String displayCode;

    private Date channelCreatedTime;

    private Set<String> cancelledSaleOrderItemCodes;

    private String invoiceUrl;

    private TaxInformation taxInformation;

    private ShippingProviderInfo shippingProviderInfo;

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getDisplayCode() {
        return displayCode;
    }

    public void setDisplayCode(String displayCode) {
        this.displayCode = displayCode;
    }

    public Date getChannelCreatedTime() {
        return channelCreatedTime;
    }

    public void setChannelCreatedTime(Date channelCreatedTime) {
        this.channelCreatedTime = channelCreatedTime;
    }

    public Set<String> getCancelledSaleOrderItemCodes() {
        return cancelledSaleOrderItemCodes;
    }

    public void setCancelledSaleOrderItemCodes(Set<String> cancelledSaleOrderItemCodes) {
        this.cancelledSaleOrderItemCodes = cancelledSaleOrderItemCodes;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public TaxInformation getTaxInformation() {
        return taxInformation;
    }

    public void setTaxInformation(TaxInformation taxInformation)
    {
        this.taxInformation = taxInformation;
    }

    public ShippingProviderInfo getShippingProviderInfo() {
        return shippingProviderInfo;
    }

    public void setShippingProviderInfo(ShippingProviderInfo shippingProviderInfo)
    {
        this.shippingProviderInfo = shippingProviderInfo;
    }

    public static class TaxInformation {

        private List<ProductTax> productTaxes = new ArrayList<>();

        public TaxInformation addProductTax(ProductTax productTax) {
            if ( this.productTaxes == null) {
                productTaxes = new ArrayList<>();
            }
            this.productTaxes.add(productTax);
            return this;
        }
        public List<ProductTax> getProductTaxes() {
            return productTaxes;
        }

        public void setProductTaxes(List<ProductTax> productTaxes)
        {
            this.productTaxes = productTaxes;
        }
    }

    public class ShippingProviderInfo {

        private String trackingNumber;

        private String shippingProvider;

        private String shippingLabelLink;

        public String getTrackingNumber() {
            return trackingNumber;
        }

        public void setTrackingNumber(String trackingNumber) {
            this.trackingNumber = trackingNumber;
        }

        public String getShippingProvider() {
            return shippingProvider;
        }

        public void setShippingProvider(String shippingProvider) {
            this.shippingProvider = shippingProvider;
        }

        public String getShippingLabelLink() {
            return shippingLabelLink;
        }

        public void setShippingLabelLink(String shippingLabelLink) {
            this.shippingLabelLink = shippingLabelLink;
        }
    }

    public static class ProductTax {

        private String channelProductId;

        private BigDecimal taxPercentage;

        private BigDecimal centralGst;

        private BigDecimal stateGst;

        private BigDecimal unionTerritoryGst;

        private BigDecimal integratedGst;

        private BigDecimal compensationCess;

        public String getChannelProductId() {
            return channelProductId;
        }

        public void setChannelProductId(String channelProductId) {
            this.channelProductId = channelProductId;
        }

        public BigDecimal getTaxPercentage() {
            return taxPercentage;
        }

        public void setTaxPercentage(BigDecimal taxPercentage) {
            this.taxPercentage = taxPercentage;
        }

        public BigDecimal getCentralGst() {
            return centralGst;
        }

        public void setCentralGst(BigDecimal centralGst) {
            this.centralGst = centralGst;
        }

        public BigDecimal getStateGst() {
            return stateGst;
        }

        public void setStateGst(BigDecimal stateGst) {
            this.stateGst = stateGst;
        }

        public BigDecimal getUnionTerritoryGst() {
            return unionTerritoryGst;
        }

        public void setUnionTerritoryGst(BigDecimal unionTerritoryGst) {
            this.unionTerritoryGst = unionTerritoryGst;
        }

        public BigDecimal getIntegratedGst() {
            return integratedGst;
        }

        public void setIntegratedGst(BigDecimal integratedGst) {
            this.integratedGst = integratedGst;
        }

        public BigDecimal getCompensationCess() {
            return compensationCess;
        }

        public void setCompensationCess(BigDecimal compensationCess) {
            this.compensationCess = compensationCess;
        }

        @Override
        public String toString() {
            return "ProductTax{" +
                    "channelProductId='" + channelProductId + '\'' +
                    ", taxPercentage=" + taxPercentage +
                    ", centralGst=" + centralGst +
                    ", stateGst=" + stateGst +
                    ", unionTerritoryGst=" + unionTerritoryGst +
                    ", integratedGst=" + integratedGst +
                    ", compensationCess=" + compensationCess +
                    '}';
        }

    }
}

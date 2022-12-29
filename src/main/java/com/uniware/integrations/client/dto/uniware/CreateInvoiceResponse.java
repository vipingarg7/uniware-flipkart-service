package com.uniware.integrations.client.dto.uniware;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class CreateInvoiceResponse {

    private boolean thirdPartyInvoicingNotAvailable;
    private String invoiceCode;
    private String displayCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone="IST")
    private Date channelCreatedTime;
    private Set<String> cancelledSaleOrderItemCodes;
    private String invoiceUrl;
    private TaxInformation taxInformation;
    private ShippingProviderInfo shippingProviderInfo;

    @Data
    public static class TaxInformation {

        private List<ProductTax> productTaxes = new ArrayList<>();

        public TaxInformation addProductTax(ProductTax productTax) {
            if ( this.productTaxes == null) {
                productTaxes = new ArrayList<>();
            }
            this.productTaxes.add(productTax);
            return this;
        }

    }

    @Data
    public static class ProductTax {

        private String channelProductId;
        private BigDecimal taxPercentage;
        private BigDecimal centralGst;
        private BigDecimal stateGst;
        private BigDecimal unionTerritoryGst;
        private BigDecimal integratedGst;
        private BigDecimal compensationCess;

    }
}

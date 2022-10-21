package com.uniware.integrations.client.dto.uniware;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unifier.core.annotation.EncryptableList;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class SaleOrder {

    public enum PaymentInstrument {
        CASH,
        CREDIT_CARD,
        DEBIT_CARD,
        NET_BANKING,
        WALLET,
        LOAN
    }

    public enum StatusCode {
        CANCELLED,
        PROCESSING,
        COMPLETE,
        CREATED,
        PENDING_VERIFICATION
    }

    public enum Priority {
        NORMAL,
        HIGH,
        CRITICAL
    }

    public enum ReconciliationStatus {
        RECONCILED,
        AWAITING_PAYMENT,
        DISPUTED,
        UNRECONCILED,
        IRRECONCILIABLE
    }

    @Length(max = 45)
    private String                           code;

    @Length(max = 45)
    private String                           displayOrderCode;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date                             displayOrderDateTime;

    private String                           customerCode;

    @Length(max = 100)
    private String                           customerName;

    private String                           channel;

    @Length(max = 100)
    private String                           notificationEmail;

    @Length(max = 45)
    private String                           notificationMobile;

    @NotNull
    private Boolean                          cashOnDelivery;

    private SaleOrder.PaymentInstrument      paymentInstrument;

    @Length(max = 500)
    private String                           additionalInfo;

    private Boolean                          thirdPartyShipping;

    @Valid
    private List<ShippingProvider>           shippingProviders;

    @Valid
    private List<SaleOrderItemCombination>   saleOrderItemCombinations;

    @Valid
    @EncryptableList
    private List<AddressDetail>              addresses;

    @Valid
    private AddressRef                       billingAddress;

    @Valid
    private AddressRef                       shippingAddress;

    @Valid
    @Size(max = 10000)
    private List<SaleOrderItem>              saleOrderItems;

    @Valid
    private List<CustomFieldValue>           customFieldValues;

    private String                           currencyCode;

    private String                           shippingPackageTypeCode;

    private Boolean                          taxExempted;

    private Boolean                          cformProvided;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date                             fulfillmentTat;

    private Boolean                          verificationRequired;

    private Integer                          priority;

    @Min(value = 0)
    private BigDecimal totalDiscount;

    @Min(value = 0)
    private BigDecimal                       totalShippingCharges;

    @Min(value = 0)
    private BigDecimal                       totalCashOnDeliveryCharges;

    @Min(value = 0)
    private BigDecimal                       totalGiftWrapCharges;

    @Min(value = 0)
    private BigDecimal                       totalStoreCredit;

    @Min(value = 0)
    private BigDecimal                       totalPrepaidAmount;

    private boolean                          useVerifiedListings;

    private String                           customerGSTIN;

    @Length(max = 45)
    private String                           transactionId;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date                             transactionDate;

    @Min(value = 0)
    private BigDecimal                       amountPaid;

    @Length(max = 150)
    private String                           paymentMode;

    private String                           transactionNote;

    public static class ShippingProvider {
        @NotNull
        @Min(value = 1)
        private Integer packetNumber;

        @NotBlank
        private String  code;

        @Length(max = 45)
        private String  trackingNumber;

        /**
         * @return the packetNumber
         */
        public Integer getPacketNumber() {
            return packetNumber;
        }

        /**
         * @param packetNumber the packetNumber to set
         */
        public void setPacketNumber(Integer packetNumber) {
            this.packetNumber = packetNumber;
        }

        /**
         * @return the code
         */
        public String getCode() {
            return code;
        }

        /**
         * @param code the code to set
         */
        public void setCode(String code) {
            this.code = code;
        }

        /**
         * @return the trackingNumber
         */
        public String getTrackingNumber() {
            return trackingNumber;
        }

        /**
         * @param trackingNumber the trackingNumber to set
         */
        public void setTrackingNumber(String trackingNumber) {
            this.trackingNumber = trackingNumber;
        }



        @Override
        public String toString() {
            return "WsShippingProvider{" + "packetNumber=" + packetNumber + ", code='" + code + '\'' + ", trackingNumber='" + trackingNumber + '\'' + '}';
        }
    }

    public static class SaleOrderItemCombination {

        public SaleOrderItemCombination() {
        }

        public SaleOrderItemCombination(String combinationIdentifier, String combinationDescription) {
            this.combinationIdentifier = combinationIdentifier;
            this.combinationDescription = combinationDescription;
        }

        @NotBlank
        private String combinationIdentifier;

        @NotBlank
        @Length(max = 200)
        private String combinationDescription;

        /**
         * @return the combinationIdentifier
         */
        public String getCombinationIdentifier() {
            return combinationIdentifier;
        }

        /**
         * @param combinationIdentifier the combinationIdentifier to set
         */
        public void setCombinationIdentifier(String combinationIdentifier) {
            this.combinationIdentifier = combinationIdentifier;
        }

        /**
         * @return the combinationDescription
         */
        public String getCombinationDescription() {
            return combinationDescription;
        }

        /**
         * @param combinationDescription the combinationDescription to set
         */
        public void setCombinationDescription(String combinationDescription) {
            this.combinationDescription = combinationDescription;
        }

        @Override
        public String toString() {
            return "SaleOrderItemCombination{" + "combinationIdentifier='" + combinationIdentifier + '\'' + ", combinationDescription='" + combinationDescription + '\'' + '}';
        }
    }

}

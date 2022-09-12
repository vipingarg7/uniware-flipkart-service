package com.uniware.integrations.client.dto.uniware;

import com.unifier.core.utils.NumberUtils;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaleOrderItem {

    private static final Logger LOG                   = LoggerFactory.getLogger(SaleOrderItem.class);

    @NotBlank
    private String                   itemSku;

    private String                   itemName;

    private String                   channelProductId;

    private String                   channelSaleOrderItemCode;

    @NotBlank
    private String                   shippingMethodCode;

    @NotBlank
    @Length(max = 45)
    private String                   code;

    private boolean                  giftWrap;

    @Length(max = 256)
    private String                   giftMessage;

    @NotNull
    @Min(value = 0)
    private BigDecimal totalPrice;

    @Min(value = 0)
    private BigDecimal               sellingPrice;

    @Min(value = 0)
    private BigDecimal               prepaidAmount         = BigDecimal.ZERO;

    @Min(value = 0)
    private BigDecimal               discount;

    @Min(value = 0)
    private BigDecimal               shippingCharges       = BigDecimal.ZERO;

    @Min(value = 0)
    private BigDecimal               shippingMethodCharges = BigDecimal.ZERO;

    @Min(value = 0)
    private BigDecimal               cashOnDeliveryCharges = BigDecimal.ZERO;

    @Min(value = 0)
    private BigDecimal               giftWrapCharges       = BigDecimal.ZERO;

    @Length(max = 45)
    private String                   voucherCode;

    @Min(value = 0)
    private BigDecimal               voucherValue          = BigDecimal.ZERO;

    @Min(value = 0)
    private BigDecimal               storeCredit           = BigDecimal.ZERO;

    @Min(value = 0)
    private BigDecimal               channelTransferPrice  = BigDecimal.ZERO;

    private BigDecimal               transferPrice         = BigDecimal.ZERO;

    private int                      packetNumber;
    private boolean                  onHold;
    private String                   facilityCode;

    @Length(max = 45)
    private String                   combinationIdentifier;

    private Boolean                  requiresCustomization;

    private AddressRef               shippingAddress;

    private List<CustomFieldValue>   customFieldValues;

    private List<String>             itemDetailFields;

    private String                   type;

    private String                   ucBatchCode;

    private BigDecimal               channelMrp;

    private Date                     channelExpiryDate;

    private String                   channelVendorBatchNumber;

    private Date                     channelMfd;

    private String                   countryOfOrigin;

    private Date                     expectedDeliveryDate;

    public SaleOrderItem() {
        super();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemSku() {
        return itemSku;
    }

    public void setItemSku(String itemSku) {
        this.itemSku = itemSku;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getChannelProductId() {
        return channelProductId;
    }

    public void setChannelProductId(String channelProductId) {
        this.channelProductId = channelProductId;
    }

    public String getChannelSaleOrderItemCode() {
        return channelSaleOrderItemCode;
    }

    public void setChannelSaleOrderItemCode(String channelSaleOrderItemCode) {
        this.channelSaleOrderItemCode = channelSaleOrderItemCode;
    }

    public String getShippingMethodCode() {
        return shippingMethodCode;
    }

    public void setShippingMethodCode(String shippingMethodCode) {
        this.shippingMethodCode = shippingMethodCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isGiftWrap() {
        return giftWrap;
    }

    public void setGiftWrap(boolean giftWrap) {
        this.giftWrap = giftWrap;
    }

    public String getGiftMessage() {
        return giftMessage;
    }

    public void setGiftMessage(String giftMessage) {
        this.giftMessage = giftMessage;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getPrepaidAmount() {
        return prepaidAmount;
    }

    public void setPrepaidAmount(BigDecimal prepaidAmount) {
        this.prepaidAmount = prepaidAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(BigDecimal shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public BigDecimal getShippingMethodCharges() {
        return shippingMethodCharges;
    }

    public void setShippingMethodCharges(BigDecimal shippingMethodCharges) {
        this.shippingMethodCharges = shippingMethodCharges;
    }

    public BigDecimal getCashOnDeliveryCharges() {
        return cashOnDeliveryCharges;
    }

    public void setCashOnDeliveryCharges(BigDecimal cashOnDeliveryCharges) {
        this.cashOnDeliveryCharges = cashOnDeliveryCharges;
    }

    public BigDecimal getGiftWrapCharges() {
        return giftWrapCharges;
    }

    public void setGiftWrapCharges(BigDecimal giftWrapCharges) {
        this.giftWrapCharges = giftWrapCharges;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public BigDecimal getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(BigDecimal voucherValue) {
        this.voucherValue = voucherValue;
    }

    public BigDecimal getStoreCredit() {
        return storeCredit;
    }

    public void setStoreCredit(BigDecimal storeCredit) {
        this.storeCredit = storeCredit;
    }

    public BigDecimal getChannelTransferPrice() {
        return channelTransferPrice;
    }

    public void setChannelTransferPrice(BigDecimal channelTransferPrice) {
        this.channelTransferPrice = channelTransferPrice;
    }

    public BigDecimal getTransferPrice() {
        return transferPrice;
    }

    public void setTransferPrice(BigDecimal transferPrice) {
        this.transferPrice = transferPrice;
    }

    public int getPacketNumber() {
        return packetNumber;
    }

    public void setPacketNumber(int packetNumber) {
        this.packetNumber = packetNumber;
    }

    public boolean isOnHold() {
        return onHold;
    }

    public void setOnHold(boolean onHold) {
        this.onHold = onHold;
    }

    public String getFacilityCode() {
        return facilityCode;
    }

    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
    }

    public String getCombinationIdentifier() {
        return combinationIdentifier;
    }

    public void setCombinationIdentifier(String combinationIdentifier) {
        this.combinationIdentifier = combinationIdentifier;
    }

    public Boolean getRequiresCustomization() {
        return requiresCustomization;
    }

    public void setRequiresCustomization(Boolean requiresCustomization) {
        this.requiresCustomization = requiresCustomization;
    }

    public AddressRef getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressRef shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<CustomFieldValue> getCustomFieldValues() {
        return customFieldValues;
    }

    public void setCustomFieldValues(List<CustomFieldValue> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

    public String getUcBatchCode() {
        return ucBatchCode;
    }

    public void setUcBatchCode(String ucBatchCode) {
        this.ucBatchCode = ucBatchCode;
    }

    public BigDecimal getChannelMrp() {
        return channelMrp;
    }

    public void setChannelMrp(BigDecimal channelMrp) {
        this.channelMrp = channelMrp;
    }

    public Date getChannelExpiryDate() {
        return channelExpiryDate;
    }

    public void setChannelExpiryDate(Date channelExpiryDate) {
        this.channelExpiryDate = channelExpiryDate;
    }

    public String getChannelVendorBatchNumber() {
        return channelVendorBatchNumber;
    }

    public void setChannelVendorBatchNumber(String channelVendorBatchNumber) {
        this.channelVendorBatchNumber = channelVendorBatchNumber;
    }

    public Date getChannelMfd() {
        return channelMfd;
    }

    public void setChannelMfd(Date channelMfd) {
        this.channelMfd = channelMfd;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public Date getExpectedDeliveryDate() { return expectedDeliveryDate; }

    public void setExpectedDeliveryDate(Date expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate;}

    public static class SaleOrderItemPricing {
        private BundleItemType        bundleItemType;
        private String                code;
        private BigDecimal            totalPrice;
        private BigDecimal            sellingPrice;
        private BigDecimal            prepaidAmount;
        private BigDecimal            discount;
        private BigDecimal            shippingCharges;
        private BigDecimal            shippingMethodCharges;
        private BigDecimal            cashOnDeliveryCharges;
        private BigDecimal            giftWrapCharges;
        private BigDecimal            voucherValue;
        private BigDecimal            storeCredit;
        private BigDecimal            channelTransferPrice;
        private BigDecimal            transferPrice;
        private BigDecimal            channelMrp;
        private Queue<BundleItemType> bundleItemTypes = new LinkedBlockingQueue<>();
        private BigDecimal            totalRatio      = BigDecimal.ZERO;
        private Integer               counter         = 0;

        public SaleOrderItemPricing(SaleOrderItem SaleOrderItem) {
            this(SaleOrderItem, null);
        }

        public SaleOrderItemPricing(SaleOrderItem SaleOrderItem, List<BundleItemType> bundleItemTypes) {
            code = SaleOrderItem.getCode();
            totalPrice = SaleOrderItem.getTotalPrice();
            sellingPrice = SaleOrderItem.getSellingPrice();
            prepaidAmount = SaleOrderItem.getPrepaidAmount();
            discount = SaleOrderItem.getDiscount();
            shippingCharges = SaleOrderItem.getShippingCharges();
            shippingMethodCharges = SaleOrderItem.getShippingMethodCharges();
            cashOnDeliveryCharges = SaleOrderItem.getCashOnDeliveryCharges();
            giftWrapCharges = SaleOrderItem.getGiftWrapCharges();
            voucherValue = SaleOrderItem.getVoucherValue();
            storeCredit = SaleOrderItem.getStoreCredit();
            channelTransferPrice = SaleOrderItem.getChannelTransferPrice();
            transferPrice = SaleOrderItem.getTransferPrice();
            channelMrp = SaleOrderItem.getChannelMrp();
            if (bundleItemTypes != null) {
                this.bundleItemTypes.addAll(bundleItemTypes);
                for (BundleItemType bundleItemType : bundleItemTypes) {
                    totalRatio = totalRatio.add(bundleItemType.getPriceRatio());
                }
            }
        }

        public SaleOrderItemPricing(SaleOrderItemPricing pricing, BundleItemType bundleItemType, BigDecimal ratio, BigDecimal totalRatio, String code) {
            this.bundleItemType = bundleItemType;
            this.code = code;
            boolean totalRatioZero = BigDecimal.ZERO.compareTo(totalRatio) == 0;
            LOG.info("Total Ratio: {}, Total Ratio Zero: {}", totalRatio, totalRatioZero);
            sellingPrice = totalRatioZero ? BigDecimal.ZERO : NumberUtils.divide(pricing.getSellingPrice().multiply(ratio), totalRatio);
            prepaidAmount = totalRatioZero ? BigDecimal.ZERO : NumberUtils.divide(pricing.getPrepaidAmount().multiply(ratio), totalRatio);
            discount = totalRatioZero ? BigDecimal.ZERO : NumberUtils.divide(pricing.getDiscount().multiply(ratio), totalRatio);
            shippingCharges = totalRatioZero ? BigDecimal.ZERO : NumberUtils.divide(pricing.getShippingCharges().multiply(ratio), totalRatio);
            shippingMethodCharges = totalRatioZero ? BigDecimal.ZERO : NumberUtils.divide(pricing.getShippingMethodCharges().multiply(ratio), totalRatio);
            cashOnDeliveryCharges = totalRatioZero ? BigDecimal.ZERO : NumberUtils.divide(pricing.getCashOnDeliveryCharges().multiply(ratio), totalRatio);
            giftWrapCharges = totalRatioZero ? BigDecimal.ZERO : NumberUtils.divide(pricing.getGiftWrapCharges().multiply(ratio), totalRatio);
            voucherValue = totalRatioZero ? BigDecimal.ZERO : NumberUtils.divide(pricing.getVoucherValue().multiply(ratio), totalRatio);
            storeCredit = totalRatioZero ? BigDecimal.ZERO : NumberUtils.divide(pricing.getStoreCredit().multiply(ratio), totalRatio);
            channelTransferPrice = totalRatioZero ? BigDecimal.ZERO : NumberUtils.divide(pricing.getChannelTransferPrice().multiply(ratio), totalRatio);
            transferPrice = totalRatioZero ? BigDecimal.ZERO
                    : pricing.getTransferPrice() != null ? NumberUtils.divide(pricing.getTransferPrice().multiply(ratio), totalRatio) : null;
            totalPrice = totalRatioZero ? BigDecimal.ZERO : sellingPrice.add(shippingCharges).add(shippingMethodCharges).add(cashOnDeliveryCharges).add(giftWrapCharges);
            channelMrp = pricing.getChannelMrp() != null ? totalRatioZero ? BigDecimal.ZERO : NumberUtils.divide(pricing.getChannelMrp().multiply(ratio), totalRatio) : null;
        }

        public boolean hasNext() {
            return !bundleItemTypes.isEmpty();
        }

        public SaleOrderItemPricing next() {
            BundleItemType bundleItemType = bundleItemTypes.poll();
            SaleOrderItemPricing next;
            if (bundleItemTypes.isEmpty()) {
                next = new SaleOrderItemPricing(this, bundleItemType, BigDecimal.ONE, BigDecimal.ONE, code + '-' + counter++);
            } else {
                next = new SaleOrderItemPricing(this, bundleItemType, bundleItemType.getPriceRatio(), totalRatio, code + '-' + counter++);
                totalRatio = totalRatio.subtract(bundleItemType.getPriceRatio());
                totalPrice = totalPrice.subtract(next.getTotalPrice());
                sellingPrice = sellingPrice.subtract(next.getSellingPrice());
                prepaidAmount = prepaidAmount.subtract(next.getPrepaidAmount());
                discount = discount.subtract(next.getDiscount());
                shippingCharges = shippingCharges.subtract(next.getShippingCharges());
                shippingMethodCharges = shippingMethodCharges.subtract(next.getShippingMethodCharges());
                cashOnDeliveryCharges = cashOnDeliveryCharges.subtract(next.getCashOnDeliveryCharges());
                giftWrapCharges = giftWrapCharges.subtract(next.getGiftWrapCharges());
                voucherValue = voucherValue.subtract(next.getVoucherValue());
                storeCredit = storeCredit.subtract(next.getStoreCredit());
                channelTransferPrice = channelTransferPrice.subtract(next.getChannelTransferPrice());
                transferPrice = transferPrice != null ? transferPrice.subtract(next.getTransferPrice()) : null;
                channelMrp = channelMrp != null ? channelMrp.subtract(next.getChannelMrp()) : null;
            }
            return next;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public BundleItemType getBundleItemType() {
            return bundleItemType;
        }

        public void setBundleItemType(BundleItemType bundleItemType) {
            this.bundleItemType = bundleItemType;
        }

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
        }

        public BigDecimal getSellingPrice() {
            return sellingPrice;
        }

        public void setSellingPrice(BigDecimal sellingPrice) {
            this.sellingPrice = sellingPrice;
        }

        public BigDecimal getPrepaidAmount() {
            return prepaidAmount;
        }

        public void setPrepaidAmount(BigDecimal prepaidAmount) {
            this.prepaidAmount = prepaidAmount;
        }

        public BigDecimal getDiscount() {
            return discount;
        }

        public void setDiscount(BigDecimal discount) {
            this.discount = discount;
        }

        public BigDecimal getShippingCharges() {
            return shippingCharges;
        }

        public void setShippingCharges(BigDecimal shippingCharges) {
            this.shippingCharges = shippingCharges;
        }

        public BigDecimal getShippingMethodCharges() {
            return shippingMethodCharges;
        }

        public void setShippingMethodCharges(BigDecimal shippingMethodCharges) {
            this.shippingMethodCharges = shippingMethodCharges;
        }

        public BigDecimal getCashOnDeliveryCharges() {
            return cashOnDeliveryCharges;
        }

        public void setCashOnDeliveryCharges(BigDecimal cashOnDeliveryCharges) {
            this.cashOnDeliveryCharges = cashOnDeliveryCharges;
        }

        public BigDecimal getGiftWrapCharges() {
            return giftWrapCharges;
        }

        public void setGiftWrapCharges(BigDecimal giftWrapCharges) {
            this.giftWrapCharges = giftWrapCharges;
        }

        public BigDecimal getVoucherValue() {
            return voucherValue;
        }

        public void setVoucherValue(BigDecimal voucherValue) {
            this.voucherValue = voucherValue;
        }

        public BigDecimal getStoreCredit() {
            return storeCredit;
        }

        public void setStoreCredit(BigDecimal storeCredit) {
            this.storeCredit = storeCredit;
        }

        public BigDecimal getChannelTransferPrice() {
            return channelTransferPrice;
        }

        public void setChannelTransferPrice(BigDecimal channelTransferPrice) {
            this.channelTransferPrice = channelTransferPrice;
        }

        public BigDecimal getTransferPrice() {
            return transferPrice;
        }

        public void setTransferPrice(BigDecimal transferPrice) {
            this.transferPrice = transferPrice;
        }

        public BigDecimal getChannelMrp() {
            return channelMrp;
        }

        public void setChannelMrp(BigDecimal channelMrp) {
            this.channelMrp = channelMrp;
        }
    }

    public List<String> getItemDetailFields() {
        return itemDetailFields;
    }

    public void setItemDetailFields(List<String> itemDetailFields) {
        this.itemDetailFields = itemDetailFields;
    }

    public static void main(String[] args) {
        BigDecimal total = new BigDecimal(100);
        BigDecimal newTotal = BigDecimal.ZERO;
        int quantity = 3;
        while (quantity > 0) {
            BigDecimal val = NumberUtils.divide(total, new BigDecimal(quantity));
            newTotal = newTotal.add(val);
            System.out.println(val);
            total = total.subtract(val);
            quantity--;
        }
        System.out.println(newTotal);
    }

    @Override
    public String toString() {
        return "SaleOrderItem{" + "itemSku='" + itemSku + '\'' + ", itemName='" + itemName + '\'' + ", channelProductId='" + channelProductId + '\''
                + ", channelSaleOrderItemCode='" + channelSaleOrderItemCode + '\'' + ", shippingMethodCode='" + shippingMethodCode + '\'' + ", code='" + code + '\'' + ", giftWrap="
                + giftWrap + ", giftMessage='" + giftMessage + '\'' + ", totalPrice=" + totalPrice + ", sellingPrice=" + sellingPrice + ", prepaidAmount=" + prepaidAmount
                + ", discount=" + discount + ", shippingCharges=" + shippingCharges + ", shippingMethodCharges=" + shippingMethodCharges + ", cashOnDeliveryCharges="
                + cashOnDeliveryCharges + ", giftWrapCharges=" + giftWrapCharges + ", voucherCode='" + voucherCode + '\'' + ", voucherValue=" + voucherValue + ", storeCredit="
                + storeCredit + ", channelTransferPrice=" + channelTransferPrice + ", transferPrice=" + transferPrice + ", packetNumber=" + packetNumber + ", onHold=" + onHold
                + ", facilityCode='" + facilityCode + '\'' + ", combinationIdentifier='" + combinationIdentifier + '\'' + ", requiresCustomization=" + requiresCustomization
                + ", shippingAddress=" + shippingAddress + ", customFieldValues=" + customFieldValues + ", itemDetailFields=" + itemDetailFields + '}';
    }
    
}

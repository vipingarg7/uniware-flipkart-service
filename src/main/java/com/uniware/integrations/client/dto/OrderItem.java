package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OrderItem {

    public enum StatusEnum {
        APPROVED("APPROVED"),
        PACKING_IN_PROGRESS("PACKING_IN_PROGRESS"),
        FORM_FAILED("FORM_FAILED"),
        PACKED("PACKED"),
        READY_TO_DISPATCH("READY_TO_DISPATCH"),
        PICKUP_COMPLETE("PICKUP_COMPLETE"),
        CANCELLED("CANCELLED"),
        RETURN_REQUESTED("RETURN_REQUESTED"),
        RETURNED("RETURNED"),
        SHIPPED("SHIPPED"),
        DELIVERED("DELIVERED"),
        COMPLETED("COMPLETED");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }
        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
        @JsonCreator
        public static StatusEnum fromValue(String text) {
            for (StatusEnum b : StatusEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

    }

    public enum ServiceProfileEnum {
        FLIPKART_FULFILMENT("Flipkart_Fulfilment"),
        SELLER_FULFILMENT("Seller_Fulfilment"),
        SMART_FULFILMENT("Smart_Fulfilment"),
        FBF("FBF"),
        NON_FBF("NON_FBF"),
        FBF_LITE("FBF_LITE");

        private String value;

        ServiceProfileEnum(String value) {
            this.value = value;
        }
        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
        @JsonCreator
        public static ServiceProfileEnum fromValue(String text) {
            for (ServiceProfileEnum b : ServiceProfileEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

    }

    public enum PaymentTypeEnum {
        COD("COD"),
        PREPAID("PREPAID");

        private String value;

        PaymentTypeEnum(String value) {
            this.value = value;
        }
        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
        @JsonCreator
        public static PaymentTypeEnum fromValue(String text) {
            for (PaymentTypeEnum b : PaymentTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

    }
    
    @SerializedName("orderId")
    private String orderId = null;

    /**
     * Gets or Sets status
     */
    @SerializedName("status")
    private StatusEnum status = null;

    @SerializedName("packageIds")
    private List<String> packageIds = null;

    @SerializedName("orderItemId")
    private String orderItemId = null;

    @SerializedName("courierReturn")
    private Boolean courierReturn = null;

    @SerializedName("title")
    private String title = null;

    /**
     * Gets or Sets serviceProfile
     */
    @SerializedName("serviceProfile")
    private ServiceProfileEnum serviceProfile = null;

    @SerializedName("hsn")
    private String hsn = null;

    @SerializedName("cancellationGroupId")
    private String cancellationGroupId = null;

    @SerializedName("orderDate")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date orderDate = null;

    /**
     * Gets or Sets paymentType
     */
    @SerializedName("paymentType")
    private PaymentTypeEnum paymentType = null;

    @SerializedName("is_replacement")
    private Boolean isReplacement = null;

    @SerializedName("cancellationSubReason")
    private String cancellationSubReason = null;

    @SerializedName("fsn")
    private String fsn = null;

    @SerializedName("sku")
    private String sku = null;

    @SerializedName("priceComponents")
    private PriceComponent priceComponents = null;

    @SerializedName("cancellationReason")
    private String cancellationReason = null;

    @SerializedName("cancellationDate")
    private Date cancellationDate = null;

    @SerializedName("listingId")
    private String listingId = null;

    @SerializedName("quantity")
    private Integer quantity = null;

    public OrderItem orderId(String orderId) {
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

    public OrderItem status(StatusEnum status) {
        this.status = status;
        return this;
    }

    /**
     * Get status
     * @return status
     **/
    
    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public OrderItem packageIds(List<String> packageIds) {
        this.packageIds = packageIds;
        return this;
    }

    public OrderItem addPackageIdsItem(String packageIdsItem) {
        if (this.packageIds == null) {
            this.packageIds = new java.util.ArrayList<>();
        }
        this.packageIds.add(packageIdsItem);
        return this;
    }

    /**
     * Get packageIds
     * @return packageIds
     **/
    
    public List<String> getPackageIds() {
        return packageIds;
    }

    public void setPackageIds(List<String> packageIds) {
        this.packageIds = packageIds;
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

    public OrderItem courierReturn(Boolean courierReturn) {
        this.courierReturn = courierReturn;
        return this;
    }

    /**
     * Get courierReturn
     * @return courierReturn
     **/
    
    public Boolean isCourierReturn() {
        return courierReturn;
    }

    public void setCourierReturn(Boolean courierReturn) {
        this.courierReturn = courierReturn;
    }

    public OrderItem title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Get title
     * @return title
     **/
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public OrderItem serviceProfile(ServiceProfileEnum serviceProfile) {
        this.serviceProfile = serviceProfile;
        return this;
    }

    /**
     * Get serviceProfile
     * @return serviceProfile
     **/
    
    public ServiceProfileEnum getServiceProfile() {
        return serviceProfile;
    }

    public void setServiceProfile(ServiceProfileEnum serviceProfile) {
        this.serviceProfile = serviceProfile;
    }

    public OrderItem hsn(String hsn) {
        this.hsn = hsn;
        return this;
    }

    /**
     * Get hsn
     * @return hsn
     **/
    
    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public OrderItem cancellationGroupId(String cancellationGroupId) {
        this.cancellationGroupId = cancellationGroupId;
        return this;
    }

    /**
     * Get cancellationGroupId
     * @return cancellationGroupId
     **/
    
    public String getCancellationGroupId() {
        return cancellationGroupId;
    }

    public void setCancellationGroupId(String cancellationGroupId) {
        this.cancellationGroupId = cancellationGroupId;
    }

    public OrderItem orderDate(Date orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    /**
     * Get orderDate
     * @return orderDate
     **/
    
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public OrderItem paymentType(PaymentTypeEnum paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    /**
     * Get paymentType
     * @return paymentType
     **/
    
    public PaymentTypeEnum getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeEnum paymentType) {
        this.paymentType = paymentType;
    }

    public OrderItem isReplacement(Boolean isReplacement) {
        this.isReplacement = isReplacement;
        return this;
    }

    /**
     * Get isReplacement
     * @return isReplacement
     **/
    
    public Boolean isIsReplacement() {
        return isReplacement;
    }

    public void setIsReplacement(Boolean isReplacement) {
        this.isReplacement = isReplacement;
    }

    public OrderItem cancellationSubReason(String cancellationSubReason) {
        this.cancellationSubReason = cancellationSubReason;
        return this;
    }

    /**
     * Get cancellationSubReason
     * @return cancellationSubReason
     **/
    
    public String getCancellationSubReason() {
        return cancellationSubReason;
    }

    public void setCancellationSubReason(String cancellationSubReason) {
        this.cancellationSubReason = cancellationSubReason;
    }

    public OrderItem fsn(String fsn) {
        this.fsn = fsn;
        return this;
    }

    /**
     * Get fsn
     * @return fsn
     **/
    
    public String getFsn() {
        return fsn;
    }

    public void setFsn(String fsn) {
        this.fsn = fsn;
    }

    public OrderItem sku(String sku) {
        this.sku = sku;
        return this;
    }

    /**
     * Get sku
     * @return sku
     **/
    
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public OrderItem priceComponents(PriceComponent priceComponents) {
        this.priceComponents = priceComponents;
        return this;
    }

    /**
     * Get priceComponents
     * @return priceComponents
     **/
    
    public PriceComponent getPriceComponents() {
        return priceComponents;
    }

    public void setPriceComponents(PriceComponent priceComponents) {
        this.priceComponents = priceComponents;
    }

    public OrderItem cancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
        return this;
    }

    /**
     * Get cancellationReason
     * @return cancellationReason
     **/
    
    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public OrderItem cancellationDate(Date cancellationDate) {
        this.cancellationDate = cancellationDate;
        return this;
    }

    /**
     * Get cancellationDate
     * @return cancellationDate
     **/
    
    public Date getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(Date cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public OrderItem listingId(String listingId) {
        this.listingId = listingId;
        return this;
    }

    /**
     * Get listingId
     * @return listingId
     **/
    
    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public OrderItem quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * Get quantity
     * @return quantity
     **/
    
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override public String toString() {
        return "OrderItem{" + "orderId='" + orderId + '\'' + ", status=" + status + ", packageIds=" + packageIds
                + ", orderItemId='" + orderItemId + '\'' + ", courierReturn=" + courierReturn + ", title='" + title
                + '\'' + ", serviceProfile=" + serviceProfile + ", hsn='" + hsn + '\'' + ", cancellationGroupId='"
                + cancellationGroupId + '\'' + ", orderDate=" + orderDate + ", paymentType=" + paymentType
                + ", isReplacement=" + isReplacement + ", cancellationSubReason='" + cancellationSubReason + '\''
                + ", fsn='" + fsn + '\'' + ", sku='" + sku + '\'' + ", priceComponents=" + priceComponents
                + ", cancellationReason='" + cancellationReason + '\'' + ", cancellationDate=" + cancellationDate
                + ", listingId='" + listingId + '\'' + ", quantity=" + quantity + '}';
    }
}

package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Date;
import java.util.Objects;
import org.joda.time.DateTime;

public class OrderItem {

    @JsonProperty("orderId")
    private String orderId = null;

    /**
     * Gets or Sets status
     */
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

    }  @JsonProperty("status")
    private StatusEnum status = null;

    @JsonProperty("packageIds")
    private java.util.List<String> packageIds = null;

    @JsonProperty("orderItemId")
    private String orderItemId = null;

    @JsonProperty("courierReturn")
    private Boolean courierReturn = null;

    @JsonProperty("title")
    private String title = null;

    /**
     * Gets or Sets serviceProfile
     */
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

    }  @JsonProperty("serviceProfile")
    private ServiceProfileEnum serviceProfile = null;

    @JsonProperty("hsn")
    private String hsn = null;

    @JsonProperty("cancellationGroupId")
    private String cancellationGroupId = null;

    @JsonProperty("orderDate")
    private DateTime orderDate = null;

    /**
     * Gets or Sets paymentType
     */
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

    }  @JsonProperty("paymentType")
    private PaymentTypeEnum paymentType = null;

    @JsonProperty("is_replacement")
    private Boolean isReplacement = null;

    @JsonProperty("cancellationSubReason")
    private String cancellationSubReason = null;

    @JsonProperty("fsn")
    private String fsn = null;

    @JsonProperty("sku")
    private String sku = null;

    @JsonProperty("priceComponents")
    private PriceComponent priceComponents = null;

    @JsonProperty("cancellationReason")
    private String cancellationReason = null;

    @JsonProperty("cancellationDate")
    private DateTime cancellationDate = null;

    @JsonProperty("listingId")
    private String listingId = null;

    @JsonProperty("quantity")
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

    public OrderItem packageIds(java.util.List<String> packageIds) {
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
    
    public java.util.List<String> getPackageIds() {
        return packageIds;
    }

    public void setPackageIds(java.util.List<String> packageIds) {
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

    public OrderItem orderDate(DateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    /**
     * Get orderDate
     * @return orderDate
     **/
    
    public DateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(DateTime orderDate) {
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

    public OrderItem cancellationDate(DateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
        return this;
    }

    /**
     * Get cancellationDate
     * @return cancellationDate
     **/
    
    public DateTime getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(DateTime cancellationDate) {
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(this.orderId, orderItem.orderId) &&
                Objects.equals(this.status, orderItem.status) &&
                Objects.equals(this.packageIds, orderItem.packageIds) &&
                Objects.equals(this.orderItemId, orderItem.orderItemId) &&
                Objects.equals(this.courierReturn, orderItem.courierReturn) &&
                Objects.equals(this.title, orderItem.title) &&
                Objects.equals(this.serviceProfile, orderItem.serviceProfile) &&
                Objects.equals(this.hsn, orderItem.hsn) &&
                Objects.equals(this.cancellationGroupId, orderItem.cancellationGroupId) &&
                Objects.equals(this.orderDate, orderItem.orderDate) &&
                Objects.equals(this.paymentType, orderItem.paymentType) &&
                Objects.equals(this.isReplacement, orderItem.isReplacement) &&
                Objects.equals(this.cancellationSubReason, orderItem.cancellationSubReason) &&
                Objects.equals(this.fsn, orderItem.fsn) &&
                Objects.equals(this.sku, orderItem.sku) &&
                Objects.equals(this.priceComponents, orderItem.priceComponents) &&
                Objects.equals(this.cancellationReason, orderItem.cancellationReason) &&
                Objects.equals(this.cancellationDate, orderItem.cancellationDate) &&
                Objects.equals(this.listingId, orderItem.listingId) &&
                Objects.equals(this.quantity, orderItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, status, packageIds, orderItemId, courierReturn, title, serviceProfile, hsn, cancellationGroupId, orderDate, paymentType, isReplacement, cancellationSubReason, fsn, sku, priceComponents, cancellationReason, cancellationDate, listingId, quantity);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OrderItem {\n");

        sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    packageIds: ").append(toIndentedString(packageIds)).append("\n");
        sb.append("    orderItemId: ").append(toIndentedString(orderItemId)).append("\n");
        sb.append("    courierReturn: ").append(toIndentedString(courierReturn)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    serviceProfile: ").append(toIndentedString(serviceProfile)).append("\n");
        sb.append("    hsn: ").append(toIndentedString(hsn)).append("\n");
        sb.append("    cancellationGroupId: ").append(toIndentedString(cancellationGroupId)).append("\n");
        sb.append("    orderDate: ").append(toIndentedString(orderDate)).append("\n");
        sb.append("    paymentType: ").append(toIndentedString(paymentType)).append("\n");
        sb.append("    isReplacement: ").append(toIndentedString(isReplacement)).append("\n");
        sb.append("    cancellationSubReason: ").append(toIndentedString(cancellationSubReason)).append("\n");
        sb.append("    fsn: ").append(toIndentedString(fsn)).append("\n");
        sb.append("    sku: ").append(toIndentedString(sku)).append("\n");
        sb.append("    priceComponents: ").append(toIndentedString(priceComponents)).append("\n");
        sb.append("    cancellationReason: ").append(toIndentedString(cancellationReason)).append("\n");
        sb.append("    cancellationDate: ").append(toIndentedString(cancellationDate)).append("\n");
        sb.append("    listingId: ").append(toIndentedString(listingId)).append("\n");
        sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}

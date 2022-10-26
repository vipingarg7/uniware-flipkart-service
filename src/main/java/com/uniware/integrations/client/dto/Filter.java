package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class Filter {
    @SerializedName("sku")
    private java.util.List<String> sku = null;

    /**
     * Gets or Sets serviceProfiles
     */
    public enum ServiceProfilesEnum {
        FBF("FBF"),
        NON_FBF("NON_FBF"),
        FBF_LITE("FBF_LITE");

        private String value;

        ServiceProfilesEnum(String value) {
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
        public static ServiceProfilesEnum fromValue(String text) {
            for (ServiceProfilesEnum b : ServiceProfilesEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

    }  @SerializedName("serviceProfiles")
    private java.util.List<ServiceProfilesEnum> serviceProfiles = null;

    /**
     * Gets or Sets shipmentTypes
     */
    public enum ShipmentTypesEnum {
        EXPRESS("EXPRESS"),
        NORMAL("NORMAL"),
        SELF("SELF");

        private String value;

        ShipmentTypesEnum(String value) {
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
        public static ShipmentTypesEnum fromValue(String text) {
            for (ShipmentTypesEnum b : ShipmentTypesEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

    }  @SerializedName("shipmentTypes")
    private java.util.List<ShipmentTypesEnum> shipmentTypes = null;

    /**
     * Gets or Sets cancellationType
     */
    public enum CancellationTypeEnum {
        SELLERCANCELLATION("sellerCancellation"),
        BUYERCANCELLATION("buyerCancellation"),
        MARKETPLACECANCELLATION("marketplaceCancellation");

        private String value;

        CancellationTypeEnum(String value) {
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
        public static CancellationTypeEnum fromValue(String text) {
            for (CancellationTypeEnum b : CancellationTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

    }  @SerializedName("cancellationType")
    private CancellationTypeEnum cancellationType = null;

    /**
     * Gets or Sets dispatchServiceTiers
     */
    public enum DispatchServiceTiersEnum {
        EXPRESS("EXPRESS"),
        REGULAR("REGULAR");

        private String value;

        DispatchServiceTiersEnum(String value) {
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
        public static DispatchServiceTiersEnum fromValue(String text) {
            for (DispatchServiceTiersEnum b : DispatchServiceTiersEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

    }  @SerializedName("dispatchServiceTiers")
    private java.util.List<DispatchServiceTiersEnum> dispatchServiceTiers = null;

    @SerializedName("dispatchByDate")
    private DateFilter dispatchByDate = null;

    @SerializedName("cancellationDate")
    private DateFilter cancellationDate = null;

    /**
     * Gets or Sets states
     */
    public enum StatesEnum {
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

        StatesEnum(String value) {
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
        public static StatesEnum fromValue(String text) {
            for (StatesEnum b : StatesEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

    }  @SerializedName("states")
    private java.util.List<StatesEnum> states = null;

    @SerializedName("locationId")
    private String locationId = null;

    @SerializedName("dispatchAfterDate")
    private DateFilter dispatchAfterDate = null;

    /**
     * Gets or Sets type
     */
    public enum TypeEnum {
        PREDISPATCH("preDispatch"),
        POSTDISPATCH("postDispatch"),
        CANCELLED("cancelled");

        private String value;

        TypeEnum(String value) {
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
        public static TypeEnum fromValue(String text) {
            for (TypeEnum b : TypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

    }  @SerializedName("type")
    private TypeEnum type = null;

    @SerializedName("orderDate")
    private DateFilter orderDate = null;

    @SerializedName("modifiedDate")
    private DateFilter modifiedDate = null;

    public Filter sku(java.util.List<String> sku) {
        this.sku = sku;
        return this;
    }

    public Filter addSkuItem(String skuItem) {
        if (this.sku == null) {
            this.sku = new java.util.ArrayList<>();
        }
        this.sku.add(skuItem);
        return this;
    }

    /**
     * Get sku
     * @return sku
     **/
    
    public java.util.List<String> getSku() {
        return sku;
    }

    public void setSku(java.util.List<String> sku) {
        this.sku = sku;
    }

    public Filter serviceProfiles(java.util.List<ServiceProfilesEnum> serviceProfiles) {
        this.serviceProfiles = serviceProfiles;
        return this;
    }

    public Filter addServiceProfilesItem(ServiceProfilesEnum serviceProfilesItem) {
        if (this.serviceProfiles == null) {
            this.serviceProfiles = new java.util.ArrayList<>();
        }
        this.serviceProfiles.add(serviceProfilesItem);
        return this;
    }

    /**
     * Get serviceProfiles
     * @return serviceProfiles
     **/
    
    public java.util.List<ServiceProfilesEnum> getServiceProfiles() {
        return serviceProfiles;
    }

    public void setServiceProfiles(java.util.List<ServiceProfilesEnum> serviceProfiles) {
        this.serviceProfiles = serviceProfiles;
    }

    public Filter shipmentTypes(java.util.List<ShipmentTypesEnum> shipmentTypes) {
        this.shipmentTypes = shipmentTypes;
        return this;
    }

    public Filter addShipmentTypesItem(ShipmentTypesEnum shipmentTypesItem) {
        if (this.shipmentTypes == null) {
            this.shipmentTypes = new java.util.ArrayList<>();
        }
        this.shipmentTypes.add(shipmentTypesItem);
        return this;
    }

    /**
     * Get shipmentTypes
     * @return shipmentTypes
     **/
    
    public java.util.List<ShipmentTypesEnum> getShipmentTypes() {
        return shipmentTypes;
    }

    public void setShipmentTypes(java.util.List<ShipmentTypesEnum> shipmentTypes) {
        this.shipmentTypes = shipmentTypes;
    }

    public Filter cancellationType(CancellationTypeEnum cancellationType) {
        this.cancellationType = cancellationType;
        return this;
    }

    /**
     * Get cancellationType
     * @return cancellationType
     **/
    
    public CancellationTypeEnum getCancellationType() {
        return cancellationType;
    }

    public void setCancellationType(CancellationTypeEnum cancellationType) {
        this.cancellationType = cancellationType;
    }

    public Filter dispatchServiceTiers(java.util.List<DispatchServiceTiersEnum> dispatchServiceTiers) {
        this.dispatchServiceTiers = dispatchServiceTiers;
        return this;
    }

    public Filter addDispatchServiceTiersItem(DispatchServiceTiersEnum dispatchServiceTiersItem) {
        if (this.dispatchServiceTiers == null) {
            this.dispatchServiceTiers = new java.util.ArrayList<>();
        }
        this.dispatchServiceTiers.add(dispatchServiceTiersItem);
        return this;
    }

    /**
     * Get dispatchServiceTiers
     * @return dispatchServiceTiers
     **/
    
    public java.util.List<DispatchServiceTiersEnum> getDispatchServiceTiers() {
        return dispatchServiceTiers;
    }

    public void setDispatchServiceTiers(java.util.List<DispatchServiceTiersEnum> dispatchServiceTiers) {
        this.dispatchServiceTiers = dispatchServiceTiers;
    }

    public Filter dispatchByDate(DateFilter dispatchByDate) {
        this.dispatchByDate = dispatchByDate;
        return this;
    }

    /**
     * Get dispatchByDate
     * @return dispatchByDate
     **/
    
    public DateFilter getDispatchByDate() {
        return dispatchByDate;
    }

    public void setDispatchByDate(DateFilter dispatchByDate) {
        this.dispatchByDate = dispatchByDate;
    }

    public Filter cancellationDate(DateFilter cancellationDate) {
        this.cancellationDate = cancellationDate;
        return this;
    }

    /**
     * Get cancellationDate
     * @return cancellationDate
     **/
    
    public DateFilter getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(DateFilter cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public Filter states(java.util.List<StatesEnum> states) {
        this.states = states;
        return this;
    }

    public Filter addStatesItem(StatesEnum statesItem) {
        if (this.states == null) {
            this.states = new java.util.ArrayList<>();
        }
        this.states.add(statesItem);
        return this;
    }

    /**
     * Get states
     * @return states
     **/
    
    public java.util.List<StatesEnum> getStates() {
        return states;
    }

    public void setStates(java.util.List<StatesEnum> states) {
        this.states = states;
    }

    public Filter locationId(String locationId) {
        this.locationId = locationId;
        return this;
    }

    /**
     * Get locationId
     * @return locationId
     **/
    
    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Filter dispatchAfterDate(DateFilter dispatchAfterDate) {
        this.dispatchAfterDate = dispatchAfterDate;
        return this;
    }

    /**
     * Get dispatchAfterDate
     * @return dispatchAfterDate
     **/
    
    public DateFilter getDispatchAfterDate() {
        return dispatchAfterDate;
    }

    public void setDispatchAfterDate(DateFilter dispatchAfterDate) {
        this.dispatchAfterDate = dispatchAfterDate;
    }

    public Filter type(TypeEnum type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     * @return type
     **/

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public Filter orderDate(DateFilter orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    /**
     * Get orderDate
     * @return orderDate
     **/
    
    public DateFilter getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(DateFilter orderDate) {
        this.orderDate = orderDate;
    }

    public Filter modifiedDate(DateFilter modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    /**
     * Get modifiedDate
     * @return modifiedDate
     **/
    
    public DateFilter getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(DateFilter modifiedDate) {
        this.modifiedDate = modifiedDate;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Filter filter = (Filter) o;
        return Objects.equals(this.sku, filter.sku) &&
                Objects.equals(this.serviceProfiles, filter.serviceProfiles) &&
                Objects.equals(this.shipmentTypes, filter.shipmentTypes) &&
                Objects.equals(this.cancellationType, filter.cancellationType) &&
                Objects.equals(this.dispatchServiceTiers, filter.dispatchServiceTiers) &&
                Objects.equals(this.dispatchByDate, filter.dispatchByDate) &&
                Objects.equals(this.cancellationDate, filter.cancellationDate) &&
                Objects.equals(this.states, filter.states) &&
                Objects.equals(this.locationId, filter.locationId) &&
                Objects.equals(this.dispatchAfterDate, filter.dispatchAfterDate) &&
                Objects.equals(this.type, filter.type) &&
                Objects.equals(this.orderDate, filter.orderDate) &&
                Objects.equals(this.modifiedDate, filter.modifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, serviceProfiles, shipmentTypes, cancellationType, dispatchServiceTiers, dispatchByDate, cancellationDate, states, locationId, dispatchAfterDate, type, orderDate, modifiedDate);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Filter {\n");

        sb.append("    sku: ").append(toIndentedString(sku)).append("\n");
        sb.append("    serviceProfiles: ").append(toIndentedString(serviceProfiles)).append("\n");
        sb.append("    shipmentTypes: ").append(toIndentedString(shipmentTypes)).append("\n");
        sb.append("    cancellationType: ").append(toIndentedString(cancellationType)).append("\n");
        sb.append("    dispatchServiceTiers: ").append(toIndentedString(dispatchServiceTiers)).append("\n");
        sb.append("    dispatchByDate: ").append(toIndentedString(dispatchByDate)).append("\n");
        sb.append("    cancellationDate: ").append(toIndentedString(cancellationDate)).append("\n");
        sb.append("    states: ").append(toIndentedString(states)).append("\n");
        sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
        sb.append("    dispatchAfterDate: ").append(toIndentedString(dispatchAfterDate)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    orderDate: ").append(toIndentedString(orderDate)).append("\n");
        sb.append("    modifiedDate: ").append(toIndentedString(modifiedDate)).append("\n");
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

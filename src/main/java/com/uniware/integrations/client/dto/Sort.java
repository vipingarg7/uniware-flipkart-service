package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class Sort {

    /**
     * Gets or Sets field
     */
    public enum FieldEnum {
        DISPATCHBYDATE("dispatchByDate"),
        ORDERDATE("orderDate"),
        MODIFIEDDATE("modifiedDate"),
        DISPATCHAFTERDATE("dispatchAfterDate");

        private String value;

        FieldEnum(String value) {
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
        public static FieldEnum fromValue(String text) {
            for (FieldEnum b : FieldEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

    }  @SerializedName("field")
    private FieldEnum field = null;

    /**
     * Gets or Sets order
     */
    public enum OrderEnum {
        ASC("asc"),
        DESC("desc");

        private String value;

        OrderEnum(String value) {
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
        public static OrderEnum fromValue(String text) {
            for (OrderEnum b : OrderEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

    }  @SerializedName("order")
    private OrderEnum order = null;

    public Sort field(FieldEnum field) {
        this.field = field;
        return this;
    }

    /**
     * Get field
     * @return field
     **/
    
    public FieldEnum getField() {
        return field;
    }

    public void setField(FieldEnum field) {
        this.field = field;
    }

    public Sort order(OrderEnum order) {
        this.order = order;
        return this;
    }

    /**
     * Get order
     * @return order
     **/
    
    public OrderEnum getOrder() {
        return order;
    }

    public void setOrder(OrderEnum order) {
        this.order = order;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sort sort = (Sort) o;
        return Objects.equals(this.field, sort.field) &&
                Objects.equals(this.order, sort.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, order);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Sort {\n");

        sb.append("    field: ").append(toIndentedString(field)).append("\n");
        sb.append("    order: ").append(toIndentedString(order)).append("\n");
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

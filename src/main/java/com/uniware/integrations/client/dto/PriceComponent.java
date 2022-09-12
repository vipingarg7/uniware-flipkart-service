package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;

public class PriceComponent {

    @JsonProperty("shippingCharge")
    private BigDecimal shippingCharge = null;

    @JsonProperty("customerPrice")
    private BigDecimal customerPrice = null;

    @JsonProperty("totalPrice")
    private BigDecimal totalPrice = null;

    @JsonProperty("flipkartDiscount")
    private BigDecimal flipkartDiscount = null;

    @JsonProperty("sellingPrice")
    private BigDecimal sellingPrice = null;

    public PriceComponent shippingCharge(BigDecimal shippingCharge) {
        this.shippingCharge = shippingCharge;
        return this;
    }

    /**
     * Get shippingCharge
     * @return shippingCharge
     **/
    
    public BigDecimal getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(BigDecimal shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public PriceComponent customerPrice(BigDecimal customerPrice) {
        this.customerPrice = customerPrice;
        return this;
    }

    /**
     * Get customerPrice
     * @return customerPrice
     **/
    
    public BigDecimal getCustomerPrice() {
        return customerPrice;
    }

    public void setCustomerPrice(BigDecimal customerPrice) {
        this.customerPrice = customerPrice;
    }

    public PriceComponent totalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    /**
     * Get totalPrice
     * @return totalPrice
     **/
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PriceComponent flipkartDiscount(BigDecimal flipkartDiscount) {
        this.flipkartDiscount = flipkartDiscount;
        return this;
    }

    /**
     * Get flipkartDiscount
     * @return flipkartDiscount
     **/
    
    public BigDecimal getFlipkartDiscount() {
        return flipkartDiscount;
    }

    public void setFlipkartDiscount(BigDecimal flipkartDiscount) {
        this.flipkartDiscount = flipkartDiscount;
    }

    public PriceComponent sellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    /**
     * Get sellingPrice
     * @return sellingPrice
     **/
    
    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PriceComponent priceComponent = (PriceComponent) o;
        return Objects.equals(this.shippingCharge, priceComponent.shippingCharge) &&
                Objects.equals(this.customerPrice, priceComponent.customerPrice) &&
                Objects.equals(this.totalPrice, priceComponent.totalPrice) &&
                Objects.equals(this.flipkartDiscount, priceComponent.flipkartDiscount) &&
                Objects.equals(this.sellingPrice, priceComponent.sellingPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shippingCharge, customerPrice, totalPrice, flipkartDiscount, sellingPrice);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PriceComponent {\n");

        sb.append("    shippingCharge: ").append(toIndentedString(shippingCharge)).append("\n");
        sb.append("    customerPrice: ").append(toIndentedString(customerPrice)).append("\n");
        sb.append("    totalPrice: ").append(toIndentedString(totalPrice)).append("\n");
        sb.append("    flipkartDiscount: ").append(toIndentedString(flipkartDiscount)).append("\n");
        sb.append("    sellingPrice: ").append(toIndentedString(sellingPrice)).append("\n");
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

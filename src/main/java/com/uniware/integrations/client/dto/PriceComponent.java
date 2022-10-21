package com.uniware.integrations.client.dto;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

public class PriceComponent {

    @SerializedName("shippingCharge")
    private BigDecimal shippingCharge = null;

    @SerializedName("customerPrice")
    private BigDecimal customerPrice = null;

    @SerializedName("totalPrice")
    private BigDecimal totalPrice = null;

    @SerializedName("flipkartDiscount")
    private BigDecimal flipkartDiscount = null;

    @SerializedName("sellingPrice")
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

    @Override public String toString() {
        return "PriceComponent{" + "shippingCharge=" + shippingCharge + ", customerPrice=" + customerPrice
                + ", totalPrice=" + totalPrice + ", flipkartDiscount=" + flipkartDiscount + ", sellingPrice="
                + sellingPrice + '}';
    }
}

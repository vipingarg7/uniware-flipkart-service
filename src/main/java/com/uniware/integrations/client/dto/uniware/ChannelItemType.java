package com.uniware.integrations.client.dto.uniware;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.hibernate.validator.constraints.NotBlank;

public class ChannelItemType {

    @NotBlank
    private String                           channelCode;
    @NotBlank
    private String                           channelProductId;
    private String                           sellerSkuCode;
    private String                           productName;
    private String                           productUrl;
    private String                           size;
    private String                           color;
    private String                           brand;
    private Set<String>                      imageUrls;
    private String                           productDescription;
    private Integer                          blockedInventory;
    private int                              pendency;
    private boolean                          live = true;
    private boolean                          verified;
    private Boolean                          disabled;
    private Integer                          currentInventoryOnChannel;
    private BigDecimal                       sellingPrice;
    private BigDecimal                       mrp;
    private String                           currencyCode;
    @Valid
    private List<Attribute>                  attributes;

    public ChannelItemType() {}

    private ChannelItemType(Builder builder) {
        setChannelCode(builder.channelCode);
        setChannelProductId(builder.channelProductId);
        setSellerSkuCode(builder.sellerSkuCode);
        setProductName(builder.productName);
        setProductUrl(builder.productUrl);
        setSize(builder.size);
        setColor(builder.color);
        setBrand(builder.brand);
        setImageUrls(builder.imageUrls);
        setProductDescription(builder.productDescription);
        setBlockedInventory(builder.blockedInventory);
        setPendency(builder.pendency);
        setLive(builder.live);
        setVerified(builder.verified);
        setDisabled(builder.disabled);
        setCurrentInventoryOnChannel(builder.currentInventoryOnChannel);
        setSellingPrice(builder.sellingPrice);
        setMrp(builder.mrp);
        setCurrencyCode(builder.currencyCode);
        setAttributes(builder.attributes);
    }

    public ChannelItemType addAttribute(Attribute attribute) {
        if ( this.attributes == null ){
            this.attributes = new ArrayList<>();
        }
        this.attributes.add(attribute);
        return this;
    }

    public String getChannelProductId() {
        return channelProductId;
    }

    public void setChannelProductId(String channelProductId) {
        this.channelProductId = channelProductId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getSellerSkuCode() {
        return sellerSkuCode;
    }

    public void setSellerSkuCode(String sellerSkuCode) {
        this.sellerSkuCode = sellerSkuCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Set<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(Set<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Integer getBlockedInventory() {
        return blockedInventory;
    }

    public void setBlockedInventory(Integer blockedInventory) {
        this.blockedInventory = blockedInventory;
    }

    public int getPendency() {
        return pendency;
    }

    public void setPendency(int pendency) {
        this.pendency = pendency;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Integer getCurrentInventoryOnChannel() {
        return currentInventoryOnChannel;
    }

    public void setCurrentInventoryOnChannel(Integer currentInventoryOnChannel) {
        this.currentInventoryOnChannel = currentInventoryOnChannel;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getMrp() {
        return mrp;
    }

    public void setMrp(BigDecimal mrp) {
        this.mrp = mrp;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
        public String toString() {
            return "WsChannelItemType{" + "channelCode='" + channelCode + '\'' + ", channelProductId='" + channelProductId + '\'' + ", sellerSkuCode='" + sellerSkuCode + '\''
                    + ", productName='" + productName + '\'' + ", productUrl='" + productUrl + '\'' + ", size='" + size + '\'' + ", color='" + color
                    + '\'' + ", brand='" + brand + '\'' + ", imageUrls=" + imageUrls + ", productDescription='" + productDescription + '\''
                    + ", blockedInventory=" + blockedInventory + ", pendency=" + pendency + ", live=" + live + ", verified=" + verified + ", disabled=" + disabled
                    + ", currentInventoryOnChannel=" + currentInventoryOnChannel + ", attributes=" + attributes + ", sellingPrice=" + sellingPrice + ", mrp=" + mrp + ", currencyCode=" + currencyCode +'}';
        }

    public static final class Builder {
        private @NotBlank String channelCode;
        private @NotBlank String channelProductId;
        private String sellerSkuCode;
        private String productName;
        private String productUrl;
        private String size;
        private String color;
        private String brand;
        private Set<String> imageUrls;
        private String productDescription;
        private Integer blockedInventory;
        private int pendency;
        private boolean live;
        private boolean verified;
        private Boolean disabled;
        private Integer currentInventoryOnChannel;
        private BigDecimal sellingPrice;
        private BigDecimal mrp;
        private String currencyCode;
        private @Valid List<Attribute> attributes;

        public Builder() {
        }

        public Builder setChannelCode(@NotBlank String val) {
            channelCode = val;
            return this;
        }

        public Builder setChannelProductId(@NotBlank String val) {
            channelProductId = val;
            return this;
        }

        public Builder setSellerSkuCode(String val) {
            sellerSkuCode = val;
            return this;
        }

        public Builder setProductName(String val) {
            productName = val;
            return this;
        }

        public Builder setProductUrl(String val) {
            productUrl = val;
            return this;
        }

        public Builder setSize(String val) {
            size = val;
            return this;
        }

        public Builder setColor(String val) {
            color = val;
            return this;
        }

        public Builder setBrand(String val) {
            brand = val;
            return this;
        }

        public Builder setImageUrls(Set<String> val) {
            imageUrls = val;
            return this;
        }

        public Builder setProductDescription(String val) {
            productDescription = val;
            return this;
        }

        public Builder setBlockedInventory(Integer val) {
            blockedInventory = val;
            return this;
        }

        public Builder setPendency(int val) {
            pendency = val;
            return this;
        }

        public Builder setLive(boolean val) {
            live = val;
            return this;
        }

        public Builder setVerified(boolean val) {
            verified = val;
            return this;
        }

        public Builder setDisabled(Boolean val) {
            disabled = val;
            return this;
        }

        public Builder setCurrentInventoryOnChannel(Integer val) {
            currentInventoryOnChannel = val;
            return this;
        }

        public Builder setSellingPrice(BigDecimal val) {
            sellingPrice = val;
            return this;
        }

        public Builder setMrp(BigDecimal val) {
            mrp = val;
            return this;
        }

        public Builder setCurrencyCode(String val) {
            currencyCode = val;
            return this;
        }

        public Builder setAttributes(@Valid List<Attribute> val) {
            attributes = val;
            return this;
        }

        public ChannelItemType build() {
            return new ChannelItemType(this);
        }
    }

    public static class Attribute {
        @NotBlank
        private String name;

        @NotBlank
        private String value;

        public Attribute(Builder builder) {
            setName(builder.name);
            setValue(builder.value);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public static final class Builder {
            private @NotBlank String name;
            private @NotBlank String value;

            public Builder() {
            }

            public Builder setName(@NotBlank String val) {
                name = val;
                return this;
            }

            public Builder setValue(@NotBlank String val) {
                value = val;
                return this;
            }

            public Attribute build() {
                return new Attribute(this);
            }
        }
    }
}


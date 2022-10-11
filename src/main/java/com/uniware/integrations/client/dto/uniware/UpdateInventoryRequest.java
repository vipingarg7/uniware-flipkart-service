package com.uniware.integrations.client.dto.uniware;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateInventoryRequest {

    private List<Listing> listings;

    public List<Listing> getListings() {
        return listings;
    }

    public void setListings(List<Listing> listings) {
        this.listings = listings;
    }

    public static class Listing {

        @NotEmpty(message = "channelProductId should not be empty ")
        private String channelProductId;

        private String channelSkuCode;

        private List<Attributes> attributes;

        private int quantity;

        private List<Facility> facilities;

        public String getChannelProductId() {
            return channelProductId;
        }

        public void setChannelProductId(String channelProductId) {
            this.channelProductId = channelProductId;
        }

        public String getChannelSkuCode() {
            return channelSkuCode;
        }

        public List<Attributes> getAttributes() {
            return attributes;
        }

        public void setAttributes(List<Attributes> attributes)
        {
            this.attributes = attributes;
        }

        public void setChannelSkuCode(String channelSkuCode) {
            this.channelSkuCode = channelSkuCode;
        }

        public int getQuantity() { return quantity; }

        public void setQuantity(int quantity) { this.quantity = quantity; }

        public List<Facility> getFacilities() {
            return facilities;
        }

        public void setFacilities(List<Facility> facilities)
        {
            this.facilities = facilities;
        }
    }

    public static class Attributes {

        private String name;

        private String value;

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

    }

    public static class Facility {

        @NotNull
        private String code;

        private int quantity;

        private List<Shelf> shelves;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getQuantity() { return quantity; }

        public void setQuantity(int quantity) { this.quantity = quantity; }

        public List<Shelf> getShelves() {
            return shelves;
        }

        public void setShelves(List<Shelf> shelves) {
            this.shelves = shelves;
        }

    }

    public static class Shelf {

        private String code;

        private Integer quantity;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

}

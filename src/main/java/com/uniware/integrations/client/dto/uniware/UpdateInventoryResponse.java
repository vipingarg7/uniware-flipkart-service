package com.uniware.integrations.client.dto.uniware;

import java.util.ArrayList;
import java.util.List;

public class UpdateInventoryResponse {

    public enum Status {
        SUCCESS,
        FAILED
    }
    private List<Listing> listings;

    public UpdateInventoryResponse() {}

    public List<Listing> getListings() {    return listings;    }

    public void setListings(List<Listing> listings) {   this.listings = listings;   }

    public void addListing(Listing listing) { this.getListings().add(listing);  }

    public static class Listing {

        private Status status;
        private String channelProductId;
        private String channelSkuCode;
        private List<Attributes> attributes;
        private List<Error> errors;
        private List<Facility> facilities;

        public Listing addError(Error error) {
            if (this.errors == null)
                this.errors = new ArrayList<>();
            this.errors.add(error);
            return this;
        }
        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {  this.status = status; }

        public String getChannelProductId() {   return channelProductId;    }

        public void setChannelProductId(String channelProductId) {  this.channelProductId = channelProductId;   }

        public String getChannelSkuCode() { return channelSkuCode;  }

        public List<Attributes> getAttributes() {   return attributes;  }

        public void setAttributes(List<Attributes> attributes)  {   this.attributes = attributes;   }

        public void setChannelSkuCode(String channelSkuCode) {  this.channelSkuCode = channelSkuCode;   }

        public List<Facility> getFacilities() { return facilities;  }

        public void setFacilities(List<Facility> facilities)    {   this.facilities = facilities;   }

        public List<Error> getErrors() {    return errors;  }

        public void setErrors(List<Error> errors) { this.errors = errors;   }
    }

    public static class Facility {

        private Status status;
        private String code;
        private String quantity;
        private List<Shelf> shelves;
        private List<Error> errors;

        public Status status() {
            return status;
        }

        public void setStatus(
                Status status) {
            this.status = status;
        }

        public String getCode() {   return code;    }

        public void setCode(String code) {  this.code = code;   }

        public String getQuantity() {   return quantity;    }

        public void setQuantity(String quantity) {  this.quantity = quantity;   }

        public List<Shelf> getShelves() {   return shelves; }

        public void setShelves(List<Shelf> shelves) {   this.shelves = shelves; }

        public Status getStatus() { return status;  }

        public List<Error> getErrors() {    return errors;  }

        public void setErrors(List<Error> errors) { this.errors = errors;   }

        public void addShelf(Shelf shelf) { this.shelves.add(shelf); }

    }

    public static class Shelf {

        private Status status;
        private String code;
        private Integer quantity;
        private List<Error> errors;

        public Shelf() {}

        public Shelf(Status status, String code) {
            this.status = status;
            this.code = code;
        }

        public Shelf (Status status, String code, Integer quantity) {
            this.status = status;
            this.code = code;
            this.quantity = quantity;
        }

        public Status getStatus() { return status;  }

        public void setStatus(Status status) {  this.status = status;}

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

        public List<Error> getErrors() {    return errors;  }

        public void setErrors(List<Error> errors) { this.errors = errors;   }

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



}


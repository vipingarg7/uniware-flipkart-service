package com.uniware.integrations.client.dto.api.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseRequest;
import com.uniware.integrations.client.dto.Form;
import com.uniware.integrations.client.dto.Shipment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateInventoryV3Request extends BaseRequest {

    private Map<String,SkuDetails> skus;

    public Map<String, SkuDetails> getSkus() {
        return skus;
    }

    public void setSkus(Map<String, SkuDetails> skus) {
        this.skus = skus;
    }

    public UpdateInventoryV3Request addSku(String sku, SkuDetails skuDetail) {
        if( this.skus == null)
            this.skus = new HashMap<>();
        skus.put(sku,skuDetail);
        return this;
    }

    public static class SkuDetails {

        @SerializedName("product_id")
        private String productId;

        @SerializedName("locations")
        private List<Location> location;

        public SkuDetails addLocation(Location location) {
            if(this.location == null){
                this.location = new ArrayList<>();
            }
            this.location.add(location);
            return this;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public List<Location> getLocation() {
            return location;
        }

        public void setLocation(List<Location> location)
        {
            this.location = location;
        }
    }

    public static class Location {

        @SerializedName("id")
        private String id;

        @SerializedName("inventory")
        private int  inventory;

        public Location() {}

        public Location(String id,int inventory) {
            this.id = id;
            this.inventory = inventory;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }
    }
}

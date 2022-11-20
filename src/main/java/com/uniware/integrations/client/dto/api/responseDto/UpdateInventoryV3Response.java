package com.uniware.integrations.client.dto.api.responseDto;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.AttributeError;
import com.uniware.integrations.client.dto.BaseResponse;
import com.uniware.integrations.client.dto.Error;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateInventoryV3Response extends BaseResponse {

    private Map<String, InventoryUpdateStatus> skus;

    public Map<String, InventoryUpdateStatus> getSkus() {
        return skus;
    }

    public void setSkus(Map<String, InventoryUpdateStatus> skus)
    {
        this.skus = skus;
    }

    public UpdateInventoryV3Response addSku(String sku,InventoryUpdateStatus inventoryUpdateStatus) {
        if (this.skus == null)
            this.skus = new HashMap<>();
        this.skus.put(sku,inventoryUpdateStatus);
        return this;
    }

    public UpdateInventoryV3Response addSkus(Map<String, InventoryUpdateStatus> updateInventoryV3Response) {
        if (this.skus == null)
            this.skus = new HashMap<>();
        for ( Map.Entry<String, InventoryUpdateStatus> entry :updateInventoryV3Response.entrySet()) {
            Gson gson = new Gson();
            JsonElement jsonElement = gson.toJsonTree(entry.getValue());
            InventoryUpdateStatus inventoryUpdateStatus = gson.fromJson(jsonElement, InventoryUpdateStatus.class);
            addSku(entry.getKey(), inventoryUpdateStatus);
        }
        return this;
    }

    @Override public String toString() {
        return "UpdateInventoryV3Response{" + "skus=" + skus + '}';
    }

    public static class InventoryUpdateStatus {

        @SerializedName("status")
        private String status;

        @SerializedName("errors")
        private List<Error> errors;

        @SerializedName("attribute_errors")
        private List<AttributeError> attributeErrors;

        public String getStatus() { return status;  }

        public void setStatus(String status) {  this.status = status;   }

        public List<Error> getErrors() {
            return errors;
        }

        public void setErrors(List<Error> errors) {
            this.errors = errors;
        }

        public List<AttributeError> getAttributeErrors() {
            return attributeErrors;
        }

        public void setAttributeErrors(List<AttributeError> attributeErrors) {
            this.attributeErrors = attributeErrors;
        }

        @Override public String toString() {
            return "InventoryUpdateStatus{" + "status='" + status + '\'' + ", errors=" + errors + ", attributeErrors="
                    + attributeErrors + '}';
        }

    }
}

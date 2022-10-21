package com.uniware.integrations.client.dto.api.responseDto;

import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.AttributeError;
import com.uniware.integrations.client.dto.BaseResponse;
import com.uniware.integrations.client.dto.Error;
import java.util.List;
import java.util.Map;

public class UpdateInventoryV3Response extends BaseResponse {

    @SerializedName("sku")
    private Map<String, InventoryUpdateStatus> skus;

    @SerializedName("errors")
    private List<Error> errors;

    public Map<String, InventoryUpdateStatus> getSkus() {
        return skus;
    }

    public void setSkus(Map<String, InventoryUpdateStatus> skus)
    {
        this.skus = skus;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    @Override public String toString() {
        return "UpdateInventoryV3Response{" + "skus=" + skus + ", errors=" + errors + '}';
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

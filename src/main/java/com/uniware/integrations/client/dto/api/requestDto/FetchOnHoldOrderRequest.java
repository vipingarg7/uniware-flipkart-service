package com.uniware.integrations.client.dto.api.requestDto;

import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseRequest;

public class FetchOnHoldOrderRequest extends BaseRequest {

    @SerializedName("payload")
    private Payload payload;

    @SerializedName("status")
    private String status;

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override public String toString() {
        return "FetchOnHoldOrderRequest{" + "payload=" + payload + ", status='" + status + '\'' + '}';
    }

    public static class  Payload {
        @SerializedName("pagination")
        private Pagination pagination = null;

        @SerializedName("params")
        private Params params = null;

        public Pagination getPagination() {
            return pagination;
        }

        public void setPagination(Pagination pagination) {
            this.pagination = pagination;
        }

        public Params getParams() {
            return params;
        }

        public void setParams(Params params) {
            this.params = params;
        }

        @Override public String toString() {
            return "Payload{" + "pagination=" + pagination + ", params=" + params + '}';
        }
    }
    public static class  Pagination {

        @SerializedName("page_num")
        private int pageNumber ;

        @SerializedName("page_size")
        private int pageSize;

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        @Override public String toString() {
            return "Pagination{" + "pageNumber=" + pageNumber + ", pageSize=" + pageSize + '}';
        }
    }

    public static class Params {

        @SerializedName("seller_id")
        private String sellerId = null;

        @SerializedName("on_hold")
        private boolean onHold = true;

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public boolean isOnHold() {
            return onHold;
        }

        public void setOnHold(boolean onHold) {
            this.onHold = onHold;
        }

        @Override public String toString() {
            return "Params{" + "sellerId='" + sellerId + '\'' + ", onHold=" + onHold + '}';
        }
    }
}

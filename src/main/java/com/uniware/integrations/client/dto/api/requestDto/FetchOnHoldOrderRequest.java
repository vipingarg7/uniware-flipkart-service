package com.uniware.integrations.client.dto.api.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FetchOnHoldOrderRequest {

    @JsonProperty("payload")
    private Payload payload;

    @JsonProperty("status")
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

    public static class  Payload {
        @JsonProperty("pagination")
        private Pagination pagination = null;

        @JsonProperty("params")
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

    }
    public static class  Pagination {

        @JsonProperty("page_num")
        private int pageNumber ;

        @JsonProperty("page_size")
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
    }

    public static class Params {

        @JsonProperty("seller_id")
        private String sellerId = null;

        @JsonProperty("on_hold")
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
    }
}

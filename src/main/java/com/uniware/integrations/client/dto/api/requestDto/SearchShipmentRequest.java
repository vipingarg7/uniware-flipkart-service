package com.uniware.integrations.client.dto.api.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uniware.integrations.client.dto.BaseRequest;
import com.uniware.integrations.client.dto.Filter;
import com.uniware.integrations.client.dto.Pagination;
import com.uniware.integrations.client.dto.Sort;
import java.util.Objects;

public class SearchShipmentRequest extends BaseRequest {

    @JsonProperty("sort")
    private Sort sort = null;

    @JsonProperty("pagination")
    private Pagination pagination = null;

    @JsonProperty("orderDateValid")
    private Boolean orderDateValid = null;

    @JsonProperty("dispatchByDateValid")
    private Boolean dispatchByDateValid = null;

    @JsonProperty("dispatchAfterDateValid")
    private Boolean dispatchAfterDateValid = null;

    @JsonProperty("skuValid")
    private Boolean skuValid = null;

    @JsonProperty("filter")
    private Filter filter = null;

    @JsonProperty("cancellationDateValid")
    private Boolean cancellationDateValid = null;

    @JsonProperty("sellerId")
    private String sellerId = null;

    public SearchShipmentRequest sort(Sort sort) {
        this.sort = sort;
        return this;
    }

    /**
     * Get sort
     * @return sort
     **/
    
    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public SearchShipmentRequest pagination(Pagination pagination) {
        this.pagination = pagination;
        return this;
    }

    /**
     * Get pagination
     * @return pagination
     **/
    
    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public SearchShipmentRequest orderDateValid(Boolean orderDateValid) {
        this.orderDateValid = orderDateValid;
        return this;
    }

    /**
     * Get orderDateValid
     * @return orderDateValid
     **/
    
    public Boolean isOrderDateValid() {
        return orderDateValid;
    }

    public void setOrderDateValid(Boolean orderDateValid) {
        this.orderDateValid = orderDateValid;
    }

    public SearchShipmentRequest dispatchByDateValid(Boolean dispatchByDateValid) {
        this.dispatchByDateValid = dispatchByDateValid;
        return this;
    }

    /**
     * Get dispatchByDateValid
     * @return dispatchByDateValid
     **/
    
    public Boolean isDispatchByDateValid() {
        return dispatchByDateValid;
    }

    public void setDispatchByDateValid(Boolean dispatchByDateValid) {
        this.dispatchByDateValid = dispatchByDateValid;
    }

    public SearchShipmentRequest dispatchAfterDateValid(Boolean dispatchAfterDateValid) {
        this.dispatchAfterDateValid = dispatchAfterDateValid;
        return this;
    }

    /**
     * Get dispatchAfterDateValid
     * @return dispatchAfterDateValid
     **/
    
    public Boolean isDispatchAfterDateValid() {
        return dispatchAfterDateValid;
    }

    public void setDispatchAfterDateValid(Boolean dispatchAfterDateValid) {
        this.dispatchAfterDateValid = dispatchAfterDateValid;
    }

    public SearchShipmentRequest skuValid(Boolean skuValid) {
        this.skuValid = skuValid;
        return this;
    }

    /**
     * Get skuValid
     * @return skuValid
     **/
    
    public Boolean isSkuValid() {
        return skuValid;
    }

    public void setSkuValid(Boolean skuValid) {
        this.skuValid = skuValid;
    }

    public SearchShipmentRequest filter(Filter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Get filter
     * @return filter
     **/
    
    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public SearchShipmentRequest cancellationDateValid(Boolean cancellationDateValid) {
        this.cancellationDateValid = cancellationDateValid;
        return this;
    }

    /**
     * Get cancellationDateValid
     * @return cancellationDateValid
     **/
    
    public Boolean isCancellationDateValid() {
        return cancellationDateValid;
    }

    public void setCancellationDateValid(Boolean cancellationDateValid) {
        this.cancellationDateValid = cancellationDateValid;
    }

    public SearchShipmentRequest sellerId(String sellerId) {
        this.sellerId = sellerId;
        return this;
    }

    /**
     * Get sellerId
     * @return sellerId
     **/
    
    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SearchShipmentRequest searchShipmentRequest = (SearchShipmentRequest) o;
        return Objects.equals(this.sort, searchShipmentRequest.sort) &&
                Objects.equals(this.pagination, searchShipmentRequest.pagination) &&
                Objects.equals(this.orderDateValid, searchShipmentRequest.orderDateValid) &&
                Objects.equals(this.dispatchByDateValid, searchShipmentRequest.dispatchByDateValid) &&
                Objects.equals(this.dispatchAfterDateValid, searchShipmentRequest.dispatchAfterDateValid) &&
                Objects.equals(this.skuValid, searchShipmentRequest.skuValid) &&
                Objects.equals(this.filter, searchShipmentRequest.filter) &&
                Objects.equals(this.cancellationDateValid, searchShipmentRequest.cancellationDateValid) &&
                Objects.equals(this.sellerId, searchShipmentRequest.sellerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sort, pagination, orderDateValid, dispatchByDateValid, dispatchAfterDateValid, skuValid, filter, cancellationDateValid, sellerId);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SearchShipmentRequest {\n");

        sb.append("    sort: ").append(toIndentedString(sort)).append("\n");
        sb.append("    pagination: ").append(toIndentedString(pagination)).append("\n");
        sb.append("    orderDateValid: ").append(toIndentedString(orderDateValid)).append("\n");
        sb.append("    dispatchByDateValid: ").append(toIndentedString(dispatchByDateValid)).append("\n");
        sb.append("    dispatchAfterDateValid: ").append(toIndentedString(dispatchAfterDateValid)).append("\n");
        sb.append("    skuValid: ").append(toIndentedString(skuValid)).append("\n");
        sb.append("    filter: ").append(toIndentedString(filter)).append("\n");
        sb.append("    cancellationDateValid: ").append(toIndentedString(cancellationDateValid)).append("\n");
        sb.append("    sellerId: ").append(toIndentedString(sellerId)).append("\n");
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

package com.uniware.integrations.client.dto.api.responseDto;

import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseResponse;
import com.uniware.integrations.client.dto.Shipment;
import java.util.List;
import java.util.Objects;

public class SearchShipmentV3Response extends BaseResponse {

    @SerializedName("shipments")
    private List<Shipment> shipments = null;

    @SerializedName("nextPageUrl")
    private String nextPageUrl = null;

    @SerializedName("hasMore")
    private Boolean hasMore = null;

    public SearchShipmentV3Response shipments(List<Shipment> shipments) {
        this.shipments = shipments;
        return this;
    }

    public SearchShipmentV3Response addShipmentsItem(Shipment shipmentsItem) {
        if (this.shipments == null) {
            this.shipments = new java.util.ArrayList<>();
        }
        this.shipments.add(shipmentsItem);
        return this;
    }

    /**
     * Get shipments
     * @return shipments
     **/
    
    public List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }

    public SearchShipmentV3Response nextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
        return this;
    }

    /**
     * Get nextPageUrl
     * @return nextPageUrl
     **/
    
    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public SearchShipmentV3Response hasMore(Boolean hasMore) {
        this.hasMore = hasMore;
        return this;
    }

    /**
     * Get hasMore
     * @return hasMore
     **/
    
    public Boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SearchShipmentV3Response searchShipmentV3Response = (SearchShipmentV3Response) o;
        return Objects.equals(this.shipments, searchShipmentV3Response.shipments) &&
                Objects.equals(this.nextPageUrl, searchShipmentV3Response.nextPageUrl) &&
                Objects.equals(this.hasMore, searchShipmentV3Response.hasMore);
    }

    @Override public String toString() {
        return "SearchShipmentV3Response{" + "shipments=" + shipments + ", nextPageUrl='" + nextPageUrl + '\''
                + ", hasMore=" + hasMore + '}';
    }

}

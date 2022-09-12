package com.uniware.integrations.client.dto.api.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uniware.integrations.client.dto.Shipment;
import java.util.Objects;

public class SearchShipmentResponseV3 {

    @JsonProperty("shipments")
    private java.util.List<Shipment> shipments = null;

    @JsonProperty("nextPageUrl")
    private String nextPageUrl = null;

    @JsonProperty("hasMore")
    private Boolean hasMore = null;

    public SearchShipmentResponseV3 shipments(java.util.List<Shipment> shipments) {
        this.shipments = shipments;
        return this;
    }

    public SearchShipmentResponseV3 addShipmentsItem(Shipment shipmentsItem) {
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
    
    public java.util.List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(java.util.List<Shipment> shipments) {
        this.shipments = shipments;
    }

    public SearchShipmentResponseV3 nextPageUrl(String nextPageUrl) {
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

    public SearchShipmentResponseV3 hasMore(Boolean hasMore) {
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
        SearchShipmentResponseV3 searchShipmentResponse = (SearchShipmentResponseV3) o;
        return Objects.equals(this.shipments, searchShipmentResponse.shipments) &&
                Objects.equals(this.nextPageUrl, searchShipmentResponse.nextPageUrl) &&
                Objects.equals(this.hasMore, searchShipmentResponse.hasMore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shipments, nextPageUrl, hasMore);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SearchShipmentResponse {\n");

        sb.append("    shipments: ").append(toIndentedString(shipments)).append("\n");
        sb.append("    nextPageUrl: ").append(toIndentedString(nextPageUrl)).append("\n");
        sb.append("    hasMore: ").append(toIndentedString(hasMore)).append("\n");
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

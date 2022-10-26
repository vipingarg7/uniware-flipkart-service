package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PackRequest {

    @SerializedName("subShipments")
    private List<SubShipments> subShipments = null;

    @SerializedName("taxItems")
    private List<TaxItem> taxItems = null;

    @SerializedName("invoices")
    private List<Invoice> invoices = null;

    @SerializedName("serialNumbers")
    private List<SerialNumber> serialNumbers = null;

    @SerializedName("locationId")
    private String locationId = null;

    @SerializedName("shipmentId")
    private String shipmentId = null;

    public PackRequest subShipments(List<SubShipments> subShipments) {
        this.subShipments = subShipments;
        return this;
    }

    public PackRequest addSubShipmentsItem(SubShipments subShipmentsItem) {
        if (this.subShipments == null) {
            this.subShipments = new ArrayList<>();
        }
        this.subShipments.add(subShipmentsItem);
        return this;
    }

    /**
     * Get subShipments
     * @return subShipments
     **/
    
    public List<SubShipments> getSubShipments() {
        return subShipments;
    }

    public void setSubShipments(List<SubShipments> subShipments) {
        this.subShipments = subShipments;
    }

    public PackRequest taxItems(List<TaxItem> taxItems) {
        this.taxItems = taxItems;
        return this;
    }

    public PackRequest addTaxItemsItem(TaxItem taxItemsItem) {
        if (this.taxItems == null) {
            this.taxItems = new ArrayList<>();
        }
        this.taxItems.add(taxItemsItem);
        return this;
    }

    /**
     * Get taxItems
     * @return taxItems
     **/
    
    public List<TaxItem> getTaxItems() {
        return taxItems;
    }

    public void setTaxItems(List<TaxItem> taxItems) {
        this.taxItems = taxItems;
    }

    public PackRequest invoices(List<Invoice> invoices) {
        this.invoices = invoices;
        return this;
    }

    public PackRequest addInvoicesItem(Invoice invoicesItem) {
        if (this.invoices == null) {
            this.invoices = new ArrayList<>();
        }
        this.invoices.add(invoicesItem);
        return this;
    }

    /**
     * Get invoices
     * @return invoices
     **/
    
    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public PackRequest serialNumbers(List<SerialNumber> serialNumbers) {
        this.serialNumbers = serialNumbers;
        return this;
    }

    public PackRequest addSerialNumbersItem(SerialNumber serialNumbersItem) {
        if (this.serialNumbers == null) {
            this.serialNumbers = new ArrayList<>();
        }
        this.serialNumbers.add(serialNumbersItem);
        return this;
    }

    /**
     * Get serialNumbers
     * @return serialNumbers
     **/
    
    public List<SerialNumber> getSerialNumbers() {
        return serialNumbers;
    }

    public void setSerialNumbers(List<SerialNumber> serialNumbers) {
        this.serialNumbers = serialNumbers;
    }

    public PackRequest locationId(String locationId) {
        this.locationId = locationId;
        return this;
    }

    /**
     * Get locationId
     * @return locationId
     **/
    
    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public PackRequest shipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
        return this;
    }

    /**
     * Get shipmentId
     * @return shipmentId
     **/
    
    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PackRequest packRequest = (PackRequest) o;
        return Objects.equals(this.subShipments, packRequest.subShipments) &&
                Objects.equals(this.taxItems, packRequest.taxItems) &&
                Objects.equals(this.invoices, packRequest.invoices) &&
                Objects.equals(this.serialNumbers, packRequest.serialNumbers) &&
                Objects.equals(this.locationId, packRequest.locationId) &&
                Objects.equals(this.shipmentId, packRequest.shipmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subShipments, taxItems, invoices, serialNumbers, locationId, shipmentId);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PackRequest {\n");

        sb.append("    subShipments: ").append(toIndentedString(subShipments)).append("\n");
        sb.append("    taxItems: ").append(toIndentedString(taxItems)).append("\n");
        sb.append("    invoices: ").append(toIndentedString(invoices)).append("\n");
        sb.append("    serialNumbers: ").append(toIndentedString(serialNumbers)).append("\n");
        sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
        sb.append("    shipmentId: ").append(toIndentedString(shipmentId)).append("\n");
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

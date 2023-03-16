package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public PackRequest addSubShipmentsItem(SubShipments subShipmentsItem) {
        if (this.subShipments == null) {
            this.subShipments = new ArrayList<>();
        }
        this.subShipments.add(subShipmentsItem);
        return this;
    }

    public PackRequest addTaxItemsItem(TaxItem taxItemsItem) {
        if (this.taxItems == null) {
            this.taxItems = new ArrayList<>();
        }
        this.taxItems.add(taxItemsItem);
        return this;
    }

    public PackRequest addInvoicesItem(Invoice invoicesItem) {
        if (this.invoices == null) {
            this.invoices = new ArrayList<>();
        }
        this.invoices.add(invoicesItem);
        return this;
    }

    public PackRequest addSerialNumbersItem(SerialNumber serialNumbersItem) {
        if (this.serialNumbers == null) {
            this.serialNumbers = new ArrayList<>();
        }
        this.serialNumbers.add(serialNumbersItem);
        return this;
    }

}

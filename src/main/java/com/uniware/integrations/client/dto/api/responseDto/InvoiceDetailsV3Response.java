package com.uniware.integrations.client.dto.api.responseDto;

import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseResponse;
import com.uniware.integrations.client.dto.Invoice;
import java.util.List;
import java.util.Objects;

public class InvoiceDetailsV3Response extends BaseResponse {

    @SerializedName("invoices")
    private List<Invoice> invoices = null;

    public InvoiceDetailsV3Response invoices(List<Invoice> invoices) {
        this.invoices = invoices;
        return this;
    }

    public InvoiceDetailsV3Response addInvoicesItem(Invoice invoicesItem) {
        if (this.invoices == null) {
            this.invoices = new java.util.ArrayList<>();
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvoiceDetailsV3Response invoiceDetailsV3Response = (InvoiceDetailsV3Response) o;
        return Objects.equals(this.invoices, invoiceDetailsV3Response.invoices);
    }

    @Override public String toString() {
        return "InvoiceDetailsV3Response{" + "invoices=" + invoices + '}';
    }
}

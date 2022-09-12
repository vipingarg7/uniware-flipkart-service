package com.uniware.integrations.client.dto.api.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uniware.integrations.client.dto.Invoice;
import java.util.Objects;

public class InvoiceDetailsResponseV3 {

    @JsonProperty("invoices")
    private java.util.List<Invoice> invoices = null;

    public InvoiceDetailsResponseV3 invoices(java.util.List<Invoice> invoices) {
        this.invoices = invoices;
        return this;
    }

    public InvoiceDetailsResponseV3 addInvoicesItem(Invoice invoicesItem) {
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
    public java.util.List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(java.util.List<Invoice> invoices) {
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
        InvoiceDetailsResponseV3 invoiceDetailsResponseV3 = (InvoiceDetailsResponseV3) o;
        return Objects.equals(this.invoices, invoiceDetailsResponseV3.invoices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoices);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class InvoiceDetailsResponseV3 {\n");

        sb.append("    invoices: ").append(toIndentedString(invoices)).append("\n");
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

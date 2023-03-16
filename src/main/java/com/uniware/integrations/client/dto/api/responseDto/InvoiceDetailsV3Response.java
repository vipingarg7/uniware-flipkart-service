package com.uniware.integrations.client.dto.api.responseDto;

import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseResponse;
import com.uniware.integrations.client.dto.Invoice;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDetailsV3Response extends BaseResponse {

    @SerializedName("invoices")
    private List<Invoice> invoices = null;

    public InvoiceDetailsV3Response addInvoicesItem(Invoice invoicesItem) {
        if (this.invoices == null) {
            this.invoices = new java.util.ArrayList<>();
        }
        this.invoices.add(invoicesItem);
        return this;
    }

}

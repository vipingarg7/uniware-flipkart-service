package com.uniware.integrations.client.dto.uniware;

import com.uniware.integrations.uniware.dto.rest.api.ShippingPackage;
import lombok.Data;

@Data
public class GenerateInvoiceRequest {
    
    private ShippingPackage shippingPackage;

}

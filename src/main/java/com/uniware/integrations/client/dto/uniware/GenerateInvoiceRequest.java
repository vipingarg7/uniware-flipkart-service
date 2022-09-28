package com.uniware.integrations.client.dto.uniware;

import java.math.BigDecimal;
import java.util.List;

public class GenerateInvoiceRequest {
    
    private ShippingPackage shippingPackage;

    public ShippingPackage getShippingPackage() {
        return shippingPackage;
    }

    public void setShippingPackage(ShippingPackage shippingPackage) {
        this.shippingPackage = shippingPackage;
    }

}

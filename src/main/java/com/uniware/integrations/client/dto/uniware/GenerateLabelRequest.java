package com.uniware.integrations.client.dto.uniware;

import com.uniware.integrations.uniware.dto.rest.api.SaleOrderItem;
import java.util.List;

public class GenerateLabelRequest {

    private String saleOrderCode;
    private String shippingPackageCode;

    public String getSaleOrderCode() {
        return saleOrderCode;
    }

    public void setSaleOrderCode(String saleOrderCode) {
        this.saleOrderCode = saleOrderCode;
    }

    public String getShippingPackageCode() {
        return shippingPackageCode;
    }

    public void setShippingPackageCode(String shippingPackageCode) {
        this.shippingPackageCode = shippingPackageCode;
    }

}

package com.uniware.integrations.client.dto.uniware;

import java.util.List;

public class CloseShippingManifestRequest {

    private String shippingManifestCode;
    private List<ShippingManifestItems> shippingManifestItems;

    public String getShippingManifestCode() {
        return shippingManifestCode;
    }

    public void setShippingManifestCode(String shippingManifestCode) {
        this.shippingManifestCode = shippingManifestCode;
    }

    public List<ShippingManifestItems> getShippingManifestItems() {
        return shippingManifestItems;
    }

    public void setShippingManifestItems(List<ShippingManifestItems> shippingManifestItems)
    {
        this.shippingManifestItems = shippingManifestItems;
    }

    public static class ShippingManifestItems {

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


}

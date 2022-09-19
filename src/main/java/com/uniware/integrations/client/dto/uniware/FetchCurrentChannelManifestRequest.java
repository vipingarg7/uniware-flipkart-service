package com.uniware.integrations.client.dto.uniware;

public class FetchCurrentChannelManifestRequest {

    private String shippingManifestCode;
    private String shippingProviderCode;

    public String getShippingManifestCode() {
        return shippingManifestCode;
    }

    public void setShippingManifestCode(String shippingManifestCode) {
        this.shippingManifestCode = shippingManifestCode;
    }

    public String getShippingProviderCode() {
        return shippingProviderCode;
    }

    public void setShippingProviderCode(String shippingProviderCode) {
        this.shippingProviderCode = shippingProviderCode;
    }
}

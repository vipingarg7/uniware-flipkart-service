package com.uniware.integrations.client.dto.uniware;

import lombok.Data;

@Data
public class ShippingProviderInfo {

    private String trackingNumber;
    private String shippingProvider;
    private String shippingLabelLink;
    private String labelFormat;

}

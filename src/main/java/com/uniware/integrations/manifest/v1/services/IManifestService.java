package com.uniware.integrations.manifest.v1.services;

import com.unicommerce.platform.integration.manifest.models.response.FetchCurrentManifestResponse;
import com.uniware.integrations.flipkart.services.FlipkartService;

public interface IManifestService extends FlipkartService {

    FetchCurrentManifestResponse fetchCurrentManifest(String shippingProviderCode);
    
}

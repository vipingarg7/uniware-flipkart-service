package com.uniware.integrations.manifest.v1.controller;

import com.unicommerce.platform.integration.manifest.controllers.IManifestController;
import com.unicommerce.platform.integration.manifest.models.response.FetchCurrentManifestResponse;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.manager.FlipkartManager;
import com.uniware.integrations.manifest.v1.services.IManifestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flipkart/v1")
public class ManifestController implements IManifestController {

    private static final String module = "MANIFEST";
    private static final String version = "v1";

    @Autowired
    private FlipkartManager flipkartManager;

    @Override public FetchCurrentManifestResponse fetchCurrentManifest(String shippingProviderCode) {
        return ((IManifestService)flipkartManager.getFlipkartModel(module,version, FlipkartRequestContext.current().getChannelSource())).fetchCurrentManifest(shippingProviderCode);
    }

}

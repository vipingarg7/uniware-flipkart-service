package com.uniware.integrations.pendency.v1.controller;

import com.unicommerce.platform.integration.pendency.controllers.IPendencyController;
import com.unicommerce.platform.integration.pendency.models.response.FetchPendenciesResponse;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.manager.FlipkartManager;
import com.uniware.integrations.manifest.v1.services.IManifestService;
import com.uniware.integrations.pendency.v1.services.IPendencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flipkart/v1")
public class PendencyController implements IPendencyController {

    private static final String module = "PENDENCY";
    private static final String version = "v1";

    @Autowired
    private FlipkartManager flipkartManager;

    @Override public FetchPendenciesResponse fetchPendencies() {
        return ((IPendencyService)flipkartManager.getFlipkartModel(module,version, FlipkartRequestContext.current().getChannelSource())).fetchPendency();

    }
}

package com.uniware.integrations.pendency.v1.services;

import com.unicommerce.platform.integration.pendency.models.response.FetchPendenciesResponse;
import com.uniware.integrations.flipkart.services.FlipkartService;

public interface IPendencyService extends FlipkartService {

    public FetchPendenciesResponse fetchPendency();


}

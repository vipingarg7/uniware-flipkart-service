package com.uniware.integrations.client.service.impl;

import com.uniware.integrations.client.service.SalesFlipkartService;
import com.uniware.integrations.core.dto.api.Response;
import java.util.Map;

/**
 * Created by vipin on 26/05/22.
 */

public abstract class AbstractSalesFlipkartService implements SalesFlipkartService {

    @Override public Response preConfiguration(Map<String, String> headers, String payload, String connectorName) {
        return null;
    }

    @Override public Response postConfiguration(Map<String, String> headers, String payload, String connectorName) {
        return null;
    }

}

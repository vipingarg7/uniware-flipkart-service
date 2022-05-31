package com.uniware.integrations.client.service;

import com.uniware.integrations.core.dto.api.Response;
import java.util.Map;

/**
 * Created by vipin on 20/05/22.
 */
public interface SalesFlipkartService extends FlipkartService {

    Response preConfiguration(Map<String, String> headers, String payload, String connectorName);

    Response postConfiguration(Map<String, String> headers, String payload, String connectorName);
}


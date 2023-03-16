package com.uniware.integrations.auth.v1.services;

import com.unicommerce.platform.integration.auth.models.request.PostConfigurationRequest;
import com.unicommerce.platform.integration.auth.models.request.PreConfigurationRequest;
import com.unicommerce.platform.integration.auth.models.request.VerifyConnectorRequest;
import com.unicommerce.platform.integration.auth.models.response.PostConfigurationResponse;
import com.unicommerce.platform.integration.auth.models.response.PreConfigurationResponse;
import com.unicommerce.platform.integration.auth.models.response.VerifyConnectorResponse;
import com.uniware.integrations.flipkart.services.FlipkartService;

public interface IConnectorAuthService extends FlipkartService {

    PreConfigurationResponse preConfiguration(String ConnectorName, PreConfigurationRequest preConfigurationRequest);

    PostConfigurationResponse postConfiguration(String connectorName, PostConfigurationRequest postConfigurationRequest);

    // TODO : Cache Implementation
    VerifyConnectorResponse connectorVerification(String connectorName, VerifyConnectorRequest verifyConnectorRequest);

}

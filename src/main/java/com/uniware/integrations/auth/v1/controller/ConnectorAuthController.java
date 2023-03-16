package com.uniware.integrations.auth.v1.controller;

import com.unicommerce.platform.integration.auth.controllers.IConnectorAuthController;
import com.unicommerce.platform.integration.auth.models.request.PostConfigurationRequest;
import com.unicommerce.platform.integration.auth.models.request.PreConfigurationRequest;
import com.unicommerce.platform.integration.auth.models.request.VerifyConnectorRequest;
import com.unicommerce.platform.integration.auth.models.response.PostConfigurationResponse;
import com.unicommerce.platform.integration.auth.models.response.PreConfigurationResponse;
import com.unicommerce.platform.integration.auth.models.response.VerifyConnectorResponse;
import com.uniware.integrations.auth.v1.services.IConnectorAuthService;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.manager.FlipkartManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flipkart/v1")
public class ConnectorAuthController implements IConnectorAuthController {


    // todo : logging
    private static final String module = "AUTH";
    private static final String version = "v1";
    @Autowired
    private FlipkartManager flipkartManager;

    @Override public VerifyConnectorResponse verifyConnector(String code, VerifyConnectorRequest connectorVerificationRequest) {
        return ((IConnectorAuthService)flipkartManager.getFlipkartModel(module,version, FlipkartRequestContext.current().getChannelSource())).connectorVerification(code, connectorVerificationRequest);
    }

    @Override public PreConfigurationResponse preConfigureConnector(String code, PreConfigurationRequest preConfigurationRequest) {
        return ((IConnectorAuthService)flipkartManager.getFlipkartModel(module,version, FlipkartRequestContext.current().getChannelSource())).preConfiguration(code, preConfigurationRequest);
    }

    @Override public PostConfigurationResponse postConfigureConnector(String code, PostConfigurationRequest postConfigurationRequest) {
        return ((IConnectorAuthService)flipkartManager.getFlipkartModel(module,version, FlipkartRequestContext.current().getChannelSource())).postConfiguration(code, postConfigurationRequest);
    }
}

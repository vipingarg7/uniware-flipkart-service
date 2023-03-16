package com.uniware.integrations.auth.v1.services.impl;

import com.unicommerce.platform.integration.auth.models.request.PostConfigurationRequest;
import com.unicommerce.platform.integration.auth.models.request.PreConfigurationRequest;
import com.unicommerce.platform.integration.auth.models.request.VerifyConnectorRequest;
import com.unicommerce.platform.integration.auth.models.response.PostConfigurationResponse;
import com.unicommerce.platform.integration.auth.models.response.PreConfigurationResponse;
import com.unicommerce.platform.integration.auth.models.response.VerifyConnectorResponse;
import com.uniware.integrations.auth.v1.services.IConnectorAuthService;
import com.uniware.integrations.client.annotation.FlipkartClient;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.flipkart.services.FlipkartHelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FlipkartClient(module = "AUTH", version = "v1", channelSource = ChannelSource.FLIPKART_FA)
public class faConnectorAuthServiceImpl implements IConnectorAuthService {

    @Autowired
    private FlipkartHelperService flipkartHelperService;
    private static final Logger LOGGER = LoggerFactory.getLogger(faConnectorAuthServiceImpl.class);


    @Override public VerifyConnectorResponse connectorVerification(String connectorName, VerifyConnectorRequest verifyConnectorRequest) {
        return flipkartHelperService.connectorVerification(verifyConnectorRequest, connectorName);
    }

    @Override public PreConfigurationResponse preConfiguration(String ConnectorName, PreConfigurationRequest preConfigurationRequest) {
        return flipkartHelperService.preConfiguration(preConfigurationRequest);
    }

    @Override public PostConfigurationResponse postConfiguration(String ConnectorName, PostConfigurationRequest postConfigurationRequest) {
        return flipkartHelperService.postConfiguration(postConfigurationRequest);
    }

}

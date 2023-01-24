package com.uniware.integrations.client.service.impl;

import com.unifier.core.utils.DateUtils;
import com.unifier.core.utils.StringUtils;
import com.uniware.integrations.client.annotation.FlipkartClient;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.constants.EnvironmentPropertiesConstant;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.dto.api.responseDto.AuthTokenResponse;
import com.uniware.integrations.client.dto.api.responseDto.LocationDetailsResponse;
import com.uniware.integrations.client.service.FlipkartHelperService;
import com.uniware.integrations.core.dto.api.Response;
import com.uniware.integrations.uniware.authentication.connector.request.dto.ConnectorVerificationRequest;
import com.uniware.integrations.uniware.authentication.postConfig.request.dto.PostConfigurationRequest;
import com.uniware.integrations.uniware.authentication.postConfig.response.dto.PostConfigurationResponse;
import com.uniware.integrations.uniware.authentication.preConfig.request.dto.PreConfigurationRequest;
import com.uniware.integrations.uniware.authentication.preConfig.response.dto.PreConfigurationResponse;
import com.uniware.integrations.uniware.catalog.request.dto.CatalogPreProcessorRequest;
import com.uniware.integrations.uniware.catalog.request.dto.CatalogSyncRequest;
import com.uniware.integrations.utils.ResponseUtil;
import com.uniware.integrations.web.exception.BadRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "FlipkartSmartServiceImpl")
@FlipkartClient(version = "v1",channelSource = { ChannelSource.FLIPKART_SMART})
public class FlipkartSmartServiceImpl extends AbstractSalesFlipkartService {

    @Autowired FlipkartHelperService flipkartHelperService;

    @Override public Response preConfiguration(Map<String, String> headers, PreConfigurationRequest preConfigurationRequest) {
        return flipkartHelperService.preConfiguration(preConfigurationRequest);
    }

    @Override public Response postConfiguration(Map<String, String> headers, PostConfigurationRequest postConfigurationRequest) {
        return flipkartHelperService.postConfiguration(postConfigurationRequest);
    }

    @Override public Response connectorVerification(Map<String, String> headers, ConnectorVerificationRequest connectorVerificationRequest) {
        return flipkartHelperService.connectorVerification(connectorVerificationRequest);
    }

    @Override public Response catalogSyncPreProcessor(Map<String, String> headers, CatalogPreProcessorRequest catalogPreProcessorRequest) {
        return flipkartHelperService.catalogSyncPreProcessor(headers,catalogPreProcessorRequest);
    }

    @Override public Response fetchCatalog(Map<String, String> headers, CatalogSyncRequest catalogSyncRequest) {
        return flipkartHelperService.fetchCatalog(headers, catalogSyncRequest);
    }

}

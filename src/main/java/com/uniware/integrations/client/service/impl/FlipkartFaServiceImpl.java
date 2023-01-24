package com.uniware.integrations.client.service.impl;

import com.uniware.integrations.client.annotation.FlipkartClient;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.service.FlipkartHelperService;
import com.uniware.integrations.core.dto.api.Response;
import com.uniware.integrations.uniware.authentication.connector.request.dto.ConnectorVerificationRequest;
import com.uniware.integrations.uniware.catalog.request.dto.CatalogPreProcessorRequest;
import com.uniware.integrations.uniware.catalog.request.dto.CatalogSyncRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by vipin on 20/05/22.
 */
@Service(value = "FlipkartFaServiceImpl")
@FlipkartClient(version = "v1",channelSource = { ChannelSource.FLIPKART_FA})
public class FlipkartFaServiceImpl extends AbstractSalesFlipkartService {

    @Autowired FlipkartHelperService flipkartHelperService;

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

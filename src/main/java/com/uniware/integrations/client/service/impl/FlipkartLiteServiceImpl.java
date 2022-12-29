package com.uniware.integrations.client.service.impl;

import com.uniware.integrations.client.annotation.FlipkartClient;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.core.dto.api.Response;
import com.uniware.integrations.uniware.catalog.request.dto.CatalogPreProcessorRequest;
import com.uniware.integrations.uniware.catalog.request.dto.CatalogSyncRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "flipkartLiteServiceImpl")
@FlipkartClient(version = "v1",channelSource = { ChannelSource.FLIPKART_LITE})
public class FlipkartLiteServiceImpl extends AbstractSalesFlipkartService {

    @Autowired
    FlipkartDropshipServiceImpl flipkartDropshipService;

    @Override public Response catalogSyncPreProcessor(
            Map<String, String> headers, CatalogPreProcessorRequest catalogPreProcessorRequest) {
        return flipkartDropshipService.catalogSyncPreProcessor(headers,catalogPreProcessorRequest);
    }

    @Override public Response fetchCatalog(Map<String, String> headers, CatalogSyncRequest catalogSyncRequest) {
        return flipkartDropshipService.fetchCatalog(headers, catalogSyncRequest);
    }

}

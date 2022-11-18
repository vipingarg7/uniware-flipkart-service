package com.uniware.integrations.client.service;

import com.uniware.integrations.client.dto.uniware.CatalogPreProcessorRequest;
import com.uniware.integrations.client.dto.uniware.CatalogSyncRequest;
import com.uniware.integrations.client.dto.uniware.CloseShippingManifestRequest;
import com.uniware.integrations.uniware.authentication.connector.request.dto.ConnectorVerificationRequest;
import com.uniware.integrations.client.dto.uniware.DispatchShipmentRequest;
import com.uniware.integrations.client.dto.uniware.FetchCurrentChannelManifestRequest;
import com.uniware.integrations.client.dto.uniware.FetchOrderRequest;
import com.uniware.integrations.client.dto.uniware.FetchPendencyRequest;
import com.uniware.integrations.client.dto.uniware.GenerateInvoiceRequest;
import com.uniware.integrations.client.dto.uniware.GenerateLabelRequest;
import com.uniware.integrations.uniware.authentication.postConfig.request.dto.PostConfigurationRequest;
import com.uniware.integrations.uniware.authentication.preConfig.request.dto.PreConfigurationRequest;
import com.uniware.integrations.client.dto.uniware.UpdateInventoryRequest;
import com.uniware.integrations.core.dto.api.Response;
import java.util.Map;

/**
 * Created by vipin on 20/05/22.
 */
public interface SalesFlipkartService extends FlipkartService {

    Response preConfiguration(Map<String, String> headers, PreConfigurationRequest preConfigurationRequest);

    Response postConfiguration(Map<String, String> headers, PostConfigurationRequest postConfigurationRequest);

    Response connectorVerification(Map<String, String> headers, ConnectorVerificationRequest connectorVerificationRequest);
    
    Response catalogSyncPreProcessor(Map<String, String> headers, CatalogPreProcessorRequest catalogPreProcessorRequest);
    
    Response fetchPendency(Map<String, String> headers, FetchPendencyRequest fetchPendencyRequest);

    Response fetchOrders(Map<String, String> headers, FetchOrderRequest orderSyncRequest);

    Response generateInvoice(Map<String, String> headers, GenerateInvoiceRequest generateInvoiceRequest);

    Response generateShipLabel(Map<String, String> headers, GenerateLabelRequest generateLabelRequest);

    Response dispatchShipments(Map<String, String> headers, DispatchShipmentRequest dispatchShipmentRequest);

    Response closeShippingManifest(Map<String, String> headers, CloseShippingManifestRequest closeShippingManifestRequest);

    Response updateInventory(Map<String, String> headers, UpdateInventoryRequest updateInventoryRequest);

    Response fetchCatalog(Map<String, String> headers, CatalogSyncRequest catalogSyncRequest);

    Response fetchCurrentChannelManifest(Map<String, String> headers, FetchCurrentChannelManifestRequest fetchCurrentChannelManifestRequest);
}


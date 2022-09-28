package com.uniware.integrations.client.service;

import com.uniware.integrations.client.dto.uniware.CatalogPreProcessorRequest;
import com.uniware.integrations.client.dto.uniware.CatalogSyncRequest;
import com.uniware.integrations.client.dto.uniware.CloseShippingManifestRequest;
import com.uniware.integrations.client.dto.uniware.DispatchShipmentRequest;
import com.uniware.integrations.client.dto.uniware.FetchCurrentChannelManifestRequest;
import com.uniware.integrations.client.dto.uniware.FetchOrderRequest;
import com.uniware.integrations.client.dto.uniware.FetchPendencyRequest;
import com.uniware.integrations.client.dto.uniware.GenerateInvoiceRequest;
import com.uniware.integrations.client.dto.uniware.GenerateLabelRequest;
import com.uniware.integrations.client.dto.uniware.UpdateInventoryRequest;
import com.uniware.integrations.core.dto.api.Response;
import java.util.Map;

/**
 * Created by vipin on 20/05/22.
 */
public interface SalesFlipkartService extends FlipkartService {

    Response preConfiguration(Map<String, String> headers, String payload, String connectorName);

    Response postConfiguration(Map<String, String> headers, String payload, String connectorName);

    Response connectorVerification(Map<String, String> headers, String payload, String connectorName);
    
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


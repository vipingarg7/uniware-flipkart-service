package com.uniware.integrations.client.service.impl;

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
import com.uniware.integrations.client.service.SalesFlipkartService;
import com.uniware.integrations.core.dto.api.Response;
import java.util.Map;

/**
 * Created by vipin on 26/05/22.
 */

public abstract class AbstractSalesFlipkartService implements SalesFlipkartService {

    @Override public Response preConfiguration(Map<String, String> headers, String payload, String connectorName) { return null; }

    @Override public Response postConfiguration(Map<String, String> headers, String payload, String connectorName) { return null; }

    @Override public Response connectorVerification(Map<String, String> headers, String payload){ return null; };

    @Override public Response catalogSyncPreProcessor(Map<String, String> headers, CatalogPreProcessorRequest catalogPreProcessorRequest){ return null; };

    @Override public Response fetchCatalog(Map<String, String> headers, CatalogSyncRequest catalogSyncRequest) { return null; };
    @Override public Response fetchPendency(Map<String, String> headers, FetchPendencyRequest fetchPendencyRequest) { return null; };

    @Override public Response fetchOrders(Map<String, String> headers, FetchOrderRequest orderSyncRequest) { return null; }

    @Override public Response generateInvoice(Map<String, String> headers, GenerateInvoiceRequest generateInvoiceRequest) { return null; };

    @Override public Response generateShipLabel(Map<String, String> headers, GenerateLabelRequest generateLabelRequest) { return null; };

    @Override public Response dispatchShipments(Map<String, String> headers, DispatchShipmentRequest dispatchShipmentRequest) { return null; };

    @Override public Response closeShippingManifest(Map<String, String> headers, CloseShippingManifestRequest closeShippingManifestRequest) { return null; };

    @Override public Response updateInventory(Map<String, String> headers, UpdateInventoryRequest updateInventoryRequest) { return null; };

    @Override public Response fetchCurrentChannelManifest(Map<String, String> headers, FetchCurrentChannelManifestRequest fetchCurrentChannelManifestRequest) {  return null; };
}

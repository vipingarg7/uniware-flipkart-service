package com.uniware.integrations.client.service.impl;

import com.uniware.integrations.uniware.catalog.request.dto.CatalogPreProcessorRequest;
import com.uniware.integrations.uniware.catalog.request.dto.CatalogSyncRequest;
import com.uniware.integrations.client.dto.uniware.CloseShippingManifestRequest;
import com.uniware.integrations.uniware.authentication.connector.request.dto.ConnectorVerificationRequest;
import com.uniware.integrations.client.dto.uniware.DispatchShipmentRequest;
import com.uniware.integrations.uniware.manifest.currentChannel.request.dto.CurrentChannelManifestRequest;
import com.uniware.integrations.uniware.order.request.dto.FetchOrderRequest;
import com.uniware.integrations.client.dto.uniware.GenerateInvoiceRequest;
import com.uniware.integrations.client.dto.uniware.GenerateLabelRequest;
import com.uniware.integrations.uniware.authentication.postConfig.request.dto.PostConfigurationRequest;
import com.uniware.integrations.uniware.authentication.preConfig.request.dto.PreConfigurationRequest;
import com.uniware.integrations.uniware.inventory.request.dto.UpdateInventoryRequest;
import com.uniware.integrations.client.service.SalesFlipkartService;
import com.uniware.integrations.core.dto.api.Response;
import java.util.Map;

/**
 * Created by vipin on 26/05/22.
 */

public abstract class AbstractSalesFlipkartService implements SalesFlipkartService {

    @Override public Response preConfiguration(Map<String, String> headers, PreConfigurationRequest preConfigurationRequest) { return null; }

    @Override public Response postConfiguration(Map<String, String> headers, PostConfigurationRequest postConfigurationRequest) { return null; }

    @Override public Response connectorVerification(Map<String, String> headers, ConnectorVerificationRequest connectorVerificationRequest){ return null; };

    @Override public Response catalogSyncPreProcessor(Map<String, String> headers, CatalogPreProcessorRequest catalogPreProcessorRequest){ return null; };

    @Override public Response fetchCatalog(Map<String, String> headers, CatalogSyncRequest catalogSyncRequest) { return null; };
    @Override public Response fetchPendency(Map<String, String> headers) { return null; };

    @Override public Response fetchOrders(Map<String, String> headers, FetchOrderRequest orderSyncRequest) { return null; }

    @Override public Response generateInvoice(Map<String, String> headers, GenerateInvoiceRequest generateInvoiceRequest) { return null; };

    @Override public Response generateShipLabel(Map<String, String> headers, GenerateLabelRequest generateLabelRequest) { return null; };

    @Override public Response dispatchShipments(Map<String, String> headers, DispatchShipmentRequest dispatchShipmentRequest) { return null; };

    @Override public Response closeShippingManifest(Map<String, String> headers, CloseShippingManifestRequest closeShippingManifestRequest) { return null; };

    @Override public Response updateInventory(Map<String, String> headers, UpdateInventoryRequest updateInventoryRequest) { return null; };

    @Override public Response fetchCurrentChannelManifest(Map<String, String> headers, CurrentChannelManifestRequest currentChannelManifestRequest) {  return null; };
}

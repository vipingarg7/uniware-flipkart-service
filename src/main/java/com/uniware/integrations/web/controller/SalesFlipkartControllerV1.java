package com.uniware.integrations.web.controller;

import com.uniware.integrations.client.dto.uniware.CatalogPreProcessorRequest;
import com.uniware.integrations.client.dto.uniware.CatalogSyncRequest;
import com.uniware.integrations.client.dto.uniware.CloseShippingManifestRequest;
import com.uniware.integrations.uniware.authentication.connector.request.dto.ConnectorVerificationRequest;
import com.uniware.integrations.client.dto.uniware.DispatchShipmentRequest;
import com.uniware.integrations.uniware.manifest.currentChannel.request.dto.CurrentChannelManifestRequest;
import com.uniware.integrations.client.dto.uniware.FetchOrderRequest;
import com.uniware.integrations.client.dto.uniware.FetchPendencyRequest;
import com.uniware.integrations.client.dto.uniware.GenerateInvoiceRequest;
import com.uniware.integrations.client.dto.uniware.GenerateLabelRequest;
import com.uniware.integrations.uniware.authentication.postConfig.request.dto.PostConfigurationRequest;
import com.uniware.integrations.uniware.authentication.preConfig.request.dto.PreConfigurationRequest;
import com.uniware.integrations.client.dto.uniware.UpdateInventoryRequest;
import com.uniware.integrations.client.service.SalesFlipkartService;
import com.uniware.integrations.core.dto.api.Response;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by vipin on 20/05/22.
 */

@RestController
@RequestMapping("flipkart/v1")
public class SalesFlipkartControllerV1 extends BaseController {

    @PostMapping(value = "/connector/preconfig", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> connectorPreConfiguration(@RequestHeader Map<String,String> headers, @RequestBody PreConfigurationRequest preConfigurationRequest) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).preConfiguration(headers, preConfigurationRequest));
    }

    @PostMapping(value = "/connector/postconfig", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> connectorPostConfiguration(@RequestHeader Map<String,String> headers, @RequestBody PostConfigurationRequest postConfigurationRequest) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).postConfiguration(headers, postConfigurationRequest));
    }

    // todo - change request payload, connectorname
    @PostMapping(value = "connector/verify", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> connectorVerification(@RequestHeader Map<String,String> headers, @RequestBody
            ConnectorVerificationRequest connectorVerificationRequest) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).connectorVerification(headers, connectorVerificationRequest));
    }

    @PostMapping(value = "/pendency/get", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> fetchPendency(@RequestHeader Map<String,String> headers, @RequestBody FetchPendencyRequest fetchPendencyRequest) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).fetchPendency(headers, fetchPendencyRequest));
    }

    @PostMapping(value = "/orders/fetch", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> fetchOrders(@RequestHeader Map<String,String> headers,@RequestBody FetchOrderRequest orderSyncRequest ) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).fetchOrders(headers, orderSyncRequest));
    }

    @PostMapping(value = "/catalog/preprocessor", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> catalogSyncPreProcess(@RequestHeader Map<String,String> headers, @RequestBody CatalogPreProcessorRequest catalogPreProcessorRequest) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).catalogSyncPreProcessor(headers, catalogPreProcessorRequest));
    }

    @PostMapping(value = "/catalog/fetch", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> fetchCatalog(@RequestHeader Map<String,String> headers, @RequestBody CatalogSyncRequest catalogSyncRequest) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).fetchCatalog(headers, catalogSyncRequest));
    }

    @PostMapping(value = "/invoice/generate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> generateInvoice(@RequestHeader Map<String,String> headers, @RequestBody GenerateInvoiceRequest generateInvoiceRequest) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).generateInvoice(headers, generateInvoiceRequest));
    }

    @PostMapping(value = "/label/generate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> generateShipLabel(@RequestHeader Map<String,String> headers, @RequestBody GenerateLabelRequest generateLabelRequest) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).generateShipLabel(headers, generateLabelRequest));
    }

    @PostMapping(value = "/dispatch/shipment",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> dispatchShipments(@RequestHeader Map<String,String> headers, @RequestBody DispatchShipmentRequest dispatchShipmentRequest) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).dispatchShipments(headers, dispatchShipmentRequest));
    }

    @PostMapping(value = "/manifest/close", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> closeShippingManifest(@RequestHeader Map<String,String> headers, @RequestBody CloseShippingManifestRequest closeShippingManifestRequest) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).closeShippingManifest(headers, closeShippingManifestRequest));
    }

    @PutMapping(value = "/inventory/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateInventory(@RequestHeader Map<String,String> headers, @RequestBody UpdateInventoryRequest updateInventoryRequest) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).updateInventory(headers, updateInventoryRequest));
    }

    @PostMapping(value = "/manifest/currentChannel", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> fetchCurrentChannelManifest(@RequestHeader Map<String,String> headers, @RequestBody CurrentChannelManifestRequest currentChannelManifestRequest) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).fetchCurrentChannelManifest(headers, currentChannelManifestRequest));
    }

}

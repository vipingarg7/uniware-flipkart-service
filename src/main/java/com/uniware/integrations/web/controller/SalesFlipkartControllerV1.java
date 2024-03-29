package com.uniware.integrations.web.controller;

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
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping(value = "/connector/preconfig/{connectorName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> connectorPreconfiguration(@RequestHeader Map<String,String> headers, @RequestBody String payload, @PathVariable String connectorName) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).preConfiguration(headers, payload, connectorName));
    }

    @PostMapping(value = "/connector/postconfig/{connectorName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> connectorPostconfiguration(@RequestHeader Map<String,String> headers, @RequestBody String payload, @PathVariable String connectorName) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).postConfiguration(headers, payload, connectorName));
    }

    @PostMapping(value = "/connector/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> connectorVerification(@RequestHeader Map<String,String> headers, @RequestBody String payload) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).connectorVerification(headers, payload));
    }

    @PostMapping(value = "/pendency/fetch", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> fetchPendency(@RequestHeader Map<String,String> headers, @RequestHeader FetchPendencyRequest fetchPendencyRequest) {
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

    @PutMapping(value = "/inventory", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateInventory(@RequestHeader Map<String,String> headers, @RequestBody UpdateInventoryRequest updateInventoryRequest) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).updateInventory(headers, updateInventoryRequest));
    }

    @PostMapping(value = "/manifest/currentChannelManifest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> fetchCurrentChannelManifest(@RequestHeader Map<String,String> headers, @RequestBody FetchCurrentChannelManifestRequest fetchCurrentChannelManifestRequest) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).fetchCurrentChannelManifest(headers, fetchCurrentChannelManifestRequest));
    }

}

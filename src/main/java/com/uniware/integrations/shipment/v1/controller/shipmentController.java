package com.uniware.integrations.shipment.v1.controller;

import com.unicommerce.platform.integration.shipment.controllers.IShipmentController;
import com.unicommerce.platform.integration.shipment.models.request.DispatchShipmentRequest;
import com.unicommerce.platform.integration.shipment.models.request.InvoiceCreateRequest;
import com.unicommerce.platform.integration.shipment.models.request.LabelCreateRequest;
import com.unicommerce.platform.integration.shipment.models.response.DispatchShipmentResponse;
import com.unicommerce.platform.integration.shipment.models.response.InvoiceCreateResponse;
import com.unicommerce.platform.integration.shipment.models.response.LabelCreateResponse;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.manager.FlipkartManager;
import com.uniware.integrations.shipment.v1.services.IShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flipkart/v1")
public class shipmentController implements IShipmentController {

    private static final String module = "SHIPMENT";
    private static final String version = "v1";

    @Autowired
    private FlipkartManager flipkartManager;


    @Override public InvoiceCreateResponse createInvoice(InvoiceCreateRequest invoiceCreateRequest) {
        return ((IShipmentService)flipkartManager.getFlipkartModel(module,version, FlipkartRequestContext.current().getChannelSource())).createInvoice(invoiceCreateRequest);
    }

    @Override public LabelCreateResponse createLabel(LabelCreateRequest labelCreateRequest) {
        return ((IShipmentService)flipkartManager.getFlipkartModel(module,version, FlipkartRequestContext.current().getChannelSource())).createLabel(labelCreateRequest);
    }

    @Override public DispatchShipmentResponse dispatchShipment(DispatchShipmentRequest dispatchShipmentRequest) {
        return ((IShipmentService)flipkartManager.getFlipkartModel(module,version, FlipkartRequestContext.current().getChannelSource())).dispatchShipment(dispatchShipmentRequest);
    }

}

package com.uniware.integrations.shipment.v1.services;

import com.unicommerce.platform.integration.shipment.models.request.DispatchShipmentRequest;
import com.unicommerce.platform.integration.shipment.models.request.InvoiceCreateRequest;
import com.unicommerce.platform.integration.shipment.models.request.LabelCreateRequest;
import com.unicommerce.platform.integration.shipment.models.response.DispatchShipmentResponse;
import com.unicommerce.platform.integration.shipment.models.response.InvoiceCreateResponse;
import com.unicommerce.platform.integration.shipment.models.response.LabelCreateResponse;
import com.uniware.integrations.flipkart.services.FlipkartService;

public interface IShipmentService extends FlipkartService {

    InvoiceCreateResponse createInvoice(InvoiceCreateRequest invoiceCreateRequest);

    LabelCreateResponse createLabel(LabelCreateRequest labelCreateRequest);

    DispatchShipmentResponse dispatchShipment(DispatchShipmentRequest dispatchShipmentRequest);

}

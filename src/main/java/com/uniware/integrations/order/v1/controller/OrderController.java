package com.uniware.integrations.order.v1.controller;

import com.unicommerce.platform.integration.manifest.models.response.FetchCurrentManifestResponse;
import com.unicommerce.platform.integration.order.controllers.IOrderController;
import com.unicommerce.platform.integration.order.models.request.CancelOrderRequest;
import com.unicommerce.platform.integration.order.models.request.FetchOrdersRequest;
import com.unicommerce.platform.integration.order.models.response.CancelOrderResponse;
import com.unicommerce.platform.integration.order.models.response.FetchOrdersResponse;
import com.unicommerce.platform.integration.order.models.response.StatusSyncResponse;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.manager.FlipkartManager;
import com.uniware.integrations.order.v1.services.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flipkart/v1")
public class OrderController implements IOrderController {

    private static final String module = "ORDER";
    private static final String version = "v1";

    @Autowired
    private FlipkartManager flipkartManager;

    @Override public FetchOrdersResponse fetchOrders(FetchOrdersRequest fetchOrdersRequest) {
        return ((IOrderService)flipkartManager.getFlipkartModel(module,version, FlipkartRequestContext.current().getChannelSource())).fetchOrders(fetchOrdersRequest);
    }

    // TODO : Add message not supported

    @Override public StatusSyncResponse statusSyncMetadata() {
        return null;
    }

    @Override public CancelOrderResponse cancelOrder(CancelOrderRequest cancelOrderRequest) {
        return null;
    }

}

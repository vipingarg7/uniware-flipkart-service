package com.uniware.integrations.order.v1.services;

import com.unicommerce.platform.integration.order.models.request.FetchOrdersRequest;
import com.unicommerce.platform.integration.order.models.response.FetchOrdersResponse;
import com.uniware.integrations.flipkart.services.FlipkartService;

public interface IOrderService extends FlipkartService {

    FetchOrdersResponse fetchOrders(FetchOrdersRequest orderSyncRequest);

}

package com.uniware.integrations.inventory.v1.services;

import com.unicommerce.platform.integration.inventory.models.request.UpdateInventoryRequest;
import com.unicommerce.platform.integration.inventory.models.response.UpdateInventoryResponse;
import com.uniware.integrations.flipkart.services.FlipkartService;

public interface IInventoryService extends FlipkartService {

    UpdateInventoryResponse updateInventory(UpdateInventoryRequest updateInventoryRequest);

}

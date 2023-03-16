package com.uniware.integrations.inventory.v1.controller;

import com.unicommerce.platform.integration.inventory.controllers.IInventoryController;
import com.unicommerce.platform.integration.inventory.models.request.UpdateInventoryRequest;
import com.unicommerce.platform.integration.inventory.models.response.UpdateInventoryResponse;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.manager.FlipkartManager;
import com.uniware.integrations.inventory.v1.services.IInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flipkart/v1")
public class InventoryController implements IInventoryController {

    private static final String module = "INVENTORY";
    private static final String version = "v1";
    @Autowired
    private FlipkartManager flipkartManager;

    @Override
    public UpdateInventoryResponse updateInventory(UpdateInventoryRequest inventoryUpdateRequest) {
        return ((IInventoryService)flipkartManager.getFlipkartModel(module,version, FlipkartRequestContext.current().getChannelSource())).updateInventory(inventoryUpdateRequest);
    }
}

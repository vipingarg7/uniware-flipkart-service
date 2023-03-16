package com.uniware.integrations.task.v1.contorller;

import com.unicommerce.platform.integration.task.controllers.ITaskController;
import com.unicommerce.platform.integration.task.models.TaskResult;
import com.unicommerce.platform.integration.task.models.response.TaskResponse;
import com.uniware.integrations.catalog.v1.services.ICatalogService;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.manager.FlipkartManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.unicommerce.platform.integration.task.models.TaskResult.Entity.LISTING;

@RestController
@RequestMapping("/flipkart/v1")
public class TaskController implements ITaskController {

    private static final String version = "v1";
    @Autowired
    private FlipkartManager flipkartManager;


    @Override public TaskResponse enqueueTask(String entity) {

        switch ( TaskResult.Entity.valueOf(entity.toUpperCase()) ) {
        case LISTING:
            return ((ICatalogService)flipkartManager.getFlipkartModel("CATALOG",version, FlipkartRequestContext.current().getChannelSource())).enqueueReport();
        default:
            throw new RuntimeException("Invalid entity");
        }

    }

    @Override public TaskResponse pollTask(String entity, String id) {
        switch ( TaskResult.Entity.valueOf(entity.toUpperCase()) ) {
        case LISTING:
            return ((ICatalogService)flipkartManager.getFlipkartModel("CATALOG",version, FlipkartRequestContext.current().getChannelSource())).pollReportStatus();
        default:
            throw new RuntimeException("Invalid entity");
        }
    }
}

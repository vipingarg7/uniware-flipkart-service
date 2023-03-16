package com.uniware.integrations.catalog.v1.services;

import com.unicommerce.platform.integration.task.models.response.TaskResponse;
import com.uniware.integrations.flipkart.services.FlipkartService;

public interface ICatalogService extends FlipkartService {

    TaskResponse enqueueReport();

    TaskResponse pollReportStatus();

}

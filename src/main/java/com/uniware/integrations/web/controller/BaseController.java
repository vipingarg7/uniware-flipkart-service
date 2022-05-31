package com.uniware.integrations.web.controller;

import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.manager.FlipkartManager;
import com.uniware.integrations.client.service.FlipkartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by vipin on 20/05/22.
 */

@Controller
public class BaseController {

    @Autowired
    private FlipkartManager flipkartManager;

    protected FlipkartService getFlipkartModel(){
        FlipkartService flipkartService =(flipkartManager.getFlipkartModel(FlipkartRequestContext.current().getApiVersion(), FlipkartRequestContext.current().getChannelSource()));
        return flipkartService;
    }

}

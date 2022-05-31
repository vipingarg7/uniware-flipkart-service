package com.uniware.integrations.client.service.impl;

import com.uniware.integrations.client.annotation.FlipkartClient;
import com.uniware.integrations.client.constants.ChannelSource;
import org.springframework.stereotype.Service;

/**
 * Created by vipin on 20/05/22.
 */
@Service(value = "FlipkartFaServiceImpl")
@FlipkartClient(version = "v1",channelSource = { ChannelSource.FLIPKART_FA})
public class FlipkartFaServiceImpl extends AbstractSalesFlipkartService {



}

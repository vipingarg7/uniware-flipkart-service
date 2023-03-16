package com.uniware.integrations.client.annotation;

import com.uniware.integrations.client.constants.ChannelSource;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by vipin on 20/05/22.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FlipkartClient {

    // module is integration module like - connectorAuth, order etc
    String module();

    // version is used of versioning of service
    String version();

    // channelSource is different type of sources like - flipkart_dropship, flipkart_omni, flipkart_fa etc
    ChannelSource[] channelSource();
}

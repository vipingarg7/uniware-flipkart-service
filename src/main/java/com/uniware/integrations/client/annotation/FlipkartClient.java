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
    String version();
    ChannelSource[] channelSource();
}

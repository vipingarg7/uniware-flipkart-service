package com.uniware.integrations.client.config;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * Created by vipin on 25/05/22.
 */

@PropertySources(
        {
                @PropertySource("classpath:/flipkart_dropship/flipkart_dropship-${spring.profiles.active}.properties"),
                @PropertySource("classpath:/flipkart_dropship/flipkart_dropship-${spring.profiles.active}.properties"),
        }
)
public class ConfigurationManager {


}

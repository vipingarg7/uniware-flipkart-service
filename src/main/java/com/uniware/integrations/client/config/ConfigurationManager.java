package com.uniware.integrations.client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * Created by vipin on 25/05/22.
 */

@PropertySources(
        {
                @PropertySource("classpath:/flipkart/flipkart-prod.properties"),
        }
)
@Configuration
public class ConfigurationManager {

}

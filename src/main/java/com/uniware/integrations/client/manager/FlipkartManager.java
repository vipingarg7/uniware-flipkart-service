package com.uniware.integrations.client.manager;

import com.uniware.integrations.client.annotation.FlipkartClient;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.flipkart.services.FlipkartService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by admin on 20/05/22.
 */

@Component
public class FlipkartManager {

    Map<ChannelSource,Map<String, FlipkartService>> flipkartModelsMap = new HashMap();

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    private void load(){
        Map<String, Object>  beansMap = applicationContext.getBeansWithAnnotation(FlipkartClient.class);
        beansMap.forEach((beanName,bean)->{
            FlipkartClient flipkartClient = applicationContext.findAnnotationOnBean(beanName,FlipkartClient.class);
            for (ChannelSource channelSource: flipkartClient.channelSource()) {
                flipkartModelsMap.putIfAbsent(channelSource,new HashMap<String, FlipkartService>());
                Map<String, FlipkartService> flipkartServiceVersionMap= flipkartModelsMap.get(channelSource);
                String moduleVersionIdentifier = getModuleVersionIdentifier(flipkartClient.module(),flipkartClient.version());
                if( null == flipkartServiceVersionMap.get(moduleVersionIdentifier) ) {
                    flipkartServiceVersionMap.put(moduleVersionIdentifier,(FlipkartService) bean);
                } else {
                    throw new RuntimeException("Multiple versions found for ->" +flipkartClient.toString());
                }
            }
        });
    }

    public FlipkartService getFlipkartModel(String module, String version, ChannelSource channelSource) {
        Map<String, FlipkartService> flipkartServiceVersionMap = flipkartModelsMap.get(channelSource);
        String moduleVersionIdentifier = getModuleVersionIdentifier(module, version);
        if (flipkartServiceVersionMap != null) {
            return flipkartServiceVersionMap.get(moduleVersionIdentifier);
        } else {
            return null;
        }
    }

    public String getModuleVersionIdentifier(String module, String version) {
        return new StringBuilder().append(module).append('_').append(version).toString();
    }

}

package com.uniware.integrations.client.dto.uniware;

import java.util.Map;
import lombok.Data;

/**
 * Created by vipin on 25/05/22.
 */
@Data
public class PreConfigurationResponse {

    private String url;
    private String method;
    private Map<String,String> params;

}

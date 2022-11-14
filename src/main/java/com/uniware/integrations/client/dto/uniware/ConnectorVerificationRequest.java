package com.uniware.integrations.client.dto.uniware;

import java.util.Map;
import lombok.Data;

@Data
public class ConnectorVerificationRequest {

    private String name;
    private Map<String,String> parameters;

}

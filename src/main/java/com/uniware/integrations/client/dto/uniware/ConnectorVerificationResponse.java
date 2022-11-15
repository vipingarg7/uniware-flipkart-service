package com.uniware.integrations.client.dto.uniware;

import java.util.Map;
import lombok.Data;

@Data
public class ConnectorVerificationResponse {

    private Map<String,String> params;
}

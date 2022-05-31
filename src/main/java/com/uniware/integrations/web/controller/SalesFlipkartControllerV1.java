package com.uniware.integrations.web.controller;

import com.uniware.integrations.client.service.SalesFlipkartService;
import com.uniware.integrations.core.dto.api.Response;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by vipin on 20/05/22.
 */

@RestController
@RequestMapping("flipkart/v1")
public class SalesFlipkartControllerV1 extends BaseController {

    @PostMapping(value = "/connector/preconfig/{connectorName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> connectorPreconfiguration(@RequestHeader Map<String,String> headers, @RequestBody String payload, @PathVariable String connectorName) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).preConfiguration(headers, payload, connectorName));
    }

    @PostMapping(value = "/connector/postconfig/{connectorName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> connectorPostconfiguration(@RequestHeader Map<String,String> headers, @RequestBody String payload, @PathVariable String connectorName) {
        return ResponseEntity.ok().body(((SalesFlipkartService)getFlipkartModel()).postConfiguration(headers, payload, connectorName));
    }
}

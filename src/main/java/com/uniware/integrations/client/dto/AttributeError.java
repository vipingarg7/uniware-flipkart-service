package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttributeError extends Error {

    @JsonProperty("attribute")
    private String attribute;

    public AttributeError(String severity, String code, String description, String path, String attribute) {
        super(severity, code, description, path);
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}

package com.uniware.integrations.client.dto;

import com.google.gson.annotations.SerializedName;

public class AttributeError extends Error {

    @SerializedName("attribute")
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

    @Override public String toString() {
        return "AttributeError{" + "attribute='" + attribute + '\'' + '}';
    }
}

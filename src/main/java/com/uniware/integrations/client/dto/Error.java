package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {

    @JsonProperty("severity")
    private String severity;
    @JsonProperty("code")
    private String code;
    @JsonProperty("description")
    private String description;
    @JsonProperty("path")
    private String path;

    public Error(String severity, String code, String description, String path){
        this.severity = severity;
        this.code = code;
        this.description = description;
    }

    public void setSeverity(String severity){
        this.severity = severity;
    }

    public String getSeverity(){
        return this.severity;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

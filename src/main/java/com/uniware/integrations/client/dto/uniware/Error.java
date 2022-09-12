package com.uniware.integrations.client.dto.uniware;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {

    private String code;
    private String description;

    public Error(String code, String description){
        this.code = code;
        this.description = description;
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

}
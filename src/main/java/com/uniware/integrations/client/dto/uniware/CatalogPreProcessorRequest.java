package com.uniware.integrations.client.dto.uniware;

public class CatalogPreProcessorRequest {

    private boolean isAsync = false;

    public boolean isAsync() {
        return isAsync;
    }

    public void setAsync(boolean async) {
        isAsync = async;
    }
}

package com.uniware.integrations.client.dto.uniware;

public class CatalogPreProcessorRequest {

    private boolean isAsyncRun = false;

    public boolean isAsyncRun() {
        return isAsyncRun;
    }

    public void setAsyncRun(boolean asyncRun) {
        isAsyncRun = asyncRun;
    }
}

package com.uniware.integrations.client.dto.uniware;

import javax.validation.constraints.NotNull;

public class CatalogSyncRequest {

    private Boolean manualSync;
    @NotNull
    private String  stockFilePath;
    private Integer pageNumber;
    private Integer pageSize;

    public Boolean getManualSync() {
        return manualSync;
    }

    public void setManualSync(Boolean manualSync) {
        this.manualSync = manualSync;
    }

    public String getStockFilePath() {
        return stockFilePath;
    }

    public void setStockFilePath(String stockFilePath) {
        this.stockFilePath = stockFilePath;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}

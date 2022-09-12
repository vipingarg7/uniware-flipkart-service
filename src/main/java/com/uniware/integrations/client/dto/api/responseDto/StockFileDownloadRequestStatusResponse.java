package com.uniware.integrations.client.dto.api.responseDto;


public class StockFileDownloadRequestStatusResponse{

    private String sellerId;
    private String lastDownloadTimestamp;
    private String downloadState;
    private int processed_count;
    private int totalCount;


    public StockFileDownloadRequestStatusResponse(){}

    public StockFileDownloadRequestStatusResponse(String sellerId, String lastDownloadTimestamp, String downloadState, int processed_count, int totalCount){
        this.sellerId = sellerId;
        this.lastDownloadTimestamp = lastDownloadTimestamp;
        this.downloadState = downloadState;
        this.processed_count = processed_count;
        this.totalCount = totalCount;
    }

    public void setSellerId(String sellerId){
        this.sellerId = sellerId;
    }

    public String getSellerId(){
        return this.sellerId;
    }

    public void setLastDownloadTimestamp(String lastDownloadTimestamp){
        this.lastDownloadTimestamp = lastDownloadTimestamp;
    }

    public String getLastDownloadTimestamp(){
        return this.lastDownloadTimestamp;
    }

    public void setDownloadState(String downloadState){
        this.downloadState = downloadState;
    }

    public String getDownloadState(){
        return this.downloadState;
    }

    public void setProcessed_count(int processed_count){
        this.processed_count = processed_count;
    }

    public int getProcessed_count(){
        return this.processed_count;
    }

    public void setTotalCount(int totalCount){
        this.totalCount = totalCount;
    }

    public int getTotalCount(){
        return this.totalCount;
    }

}
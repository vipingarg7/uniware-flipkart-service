package com.uniware.integrations.client.dto.api.responseDto;

import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseResponse;

public class StockFileDownloadRequestStatusResponse extends BaseResponse {

    @SerializedName("sellerId")
    private String sellerId;
    @SerializedName("lastDownloadTimestamp")
    private String lastDownloadTimestamp;
    @SerializedName("downloadState")
    private String downloadState;
    @SerializedName("processed_count")
    private int processed_count;
    @SerializedName("totalCount")
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

    @Override public String toString() {
        return "StockFileDownloadRequestStatusResponse{" + "sellerId='" + sellerId + '\'' + ", lastDownloadTimestamp='"
                + lastDownloadTimestamp + '\'' + ", downloadState='" + downloadState + '\'' + ", processed_count="
                + processed_count + ", totalCount=" + totalCount + '}';
    }
}
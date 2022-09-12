package com.uniware.integrations.client.dto.api.responseDto;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class StockFileDownloadNUploadHistoryResponse {

    @JsonProperty("stockFileResponseList")
    private List<StockFileResponseList> stockFileResponseList;

    public StockFileDownloadNUploadHistoryResponse(){}

    public StockFileDownloadNUploadHistoryResponse(List<StockFileResponseList> stockFileResponseList){
        this.stockFileResponseList = stockFileResponseList;
    }

    public void setStockFileResponseList(List<StockFileResponseList> stockFileResponseList){
        this.stockFileResponseList = stockFileResponseList;
    }
    public List<StockFileResponseList> getStockFileResponseList(){
        return this.stockFileResponseList;
    }

    public class StockFileResponseList {
        
        @JsonProperty("file_link")
        private String fileLink;

        @JsonProperty("file_operation_type")
        private String fileOperationType;

        @JsonProperty("file_name")
        private String fileName;

        @JsonProperty("error_count")
        private int errorCount;

        @JsonProperty("total_count")
        private int totalCount;

        @JsonProperty("file_format")
        private String fileFormat;

        @JsonProperty("uploaded_on")
        private int uploadedOn;

        @JsonProperty("feed_state")
        private String feedState;

        @JsonProperty("error_rows_exists")
        private boolean errorRowsExists;

        public StockFileResponseList(){}

        public StockFileResponseList(String fileLink, String fileOperationType, String fileName, int errorCount, int totalCount, String fileFormat, int uploadedOn, String feedState, boolean errorRowsExists){
            this.fileLink = fileLink;
            this.fileOperationType = fileOperationType;
            this.fileName = fileName;
            this.errorCount = errorCount;
            this.totalCount = totalCount;
            this.fileFormat = fileFormat;
            this.uploadedOn = uploadedOn;
            this.feedState = feedState;
            this.errorRowsExists = errorRowsExists;
        }

        public void setFileLink(String fileLink){
            this.fileLink = fileLink;
        }

        public String getFileLink(){
            return this.fileLink;
        }

        public void setFileOperationType(String fileOperationType){
            this.fileOperationType = fileOperationType;
        }

        public String getFileOperationType(){
            return this.fileOperationType;
        }

        public void setFileName(String fileName){
            this.fileName = fileName;
        }

        public String getFileName(){
            return this.fileName;
        }

        public void setErrorCount(int errorCount){
            this.errorCount = errorCount;
        }

        public int getErrorCount(){
            return this.errorCount;
        }

        public void setTotalCount(int totalCount){
            this.totalCount = totalCount;
        }

        public int getTotalCount(){
            return this.totalCount;
        }

        public void setFileFormat(String fileFormat){
            this.fileFormat = fileFormat;
        }

        public String getFileFormat(){
            return this.fileFormat;
        }

        public void setUploadedOn(int uploadedOn){
            this.uploadedOn = uploadedOn;
        }

        public int getUploadedOn(){
            return this.uploadedOn;
        }

        public void setFeedState(String feedState){
            this.feedState = feedState;
        }

        public String getFeedState(){
            return this.feedState;
        }

        public void setErrorRowsExists(boolean errorRowsExists){
            this.errorRowsExists = errorRowsExists;
        }

        public boolean getErrorRowsExists(){
            return this.errorRowsExists;
        }
    }
}
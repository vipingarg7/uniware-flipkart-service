package com.uniware.integrations.client.dto.api.responseDto;


import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseResponse;
import java.util.List;

public class StockFileDownloadNUploadHistoryResponse extends BaseResponse {

    @SerializedName("stock_file_response_list")
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

    @Override public String toString() {
        return "StockFileDownloadNUploadHistoryResponse{" + "stockFileResponseList=" + stockFileResponseList + '}';
    }

    public class StockFileResponseList {
        
        @SerializedName("file_link")
        private String fileLink;

        @SerializedName("file_operation_type")
        private String fileOperationType;

        @SerializedName("file_name")
        private String fileName;

        @SerializedName("error_count")
        private int errorCount;

        @SerializedName("total_count")
        private int totalCount;

        @SerializedName("file_format")
        private String fileFormat;

        @SerializedName("uploaded_on")
        private long uploadedOn;

        @SerializedName("feed_state")
        private String feedState;

        @SerializedName("error_rows_exists")
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

        public void setUploadedOn(long uploadedOn){
            this.uploadedOn = uploadedOn;
        }

        public long getUploadedOn(){
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

        @Override public String toString() {
            return "StockFileResponseList{" + "fileLink='" + fileLink + '\'' + ", fileOperationType='"
                    + fileOperationType + '\'' + ", fileName='" + fileName + '\'' + ", errorCount=" + errorCount
                    + ", totalCount=" + totalCount + ", fileFormat='" + fileFormat + '\'' + ", uploadedOn=" + uploadedOn
                    + ", feedState='" + feedState + '\'' + ", errorRowsExists=" + errorRowsExists + '}';
        }
    }
}
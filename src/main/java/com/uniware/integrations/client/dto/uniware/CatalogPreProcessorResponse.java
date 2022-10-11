package com.uniware.integrations.client.dto.uniware;

public class CatalogPreProcessorResponse {

    public enum Status {
        FAILED,
        PROCESSING,
        COMPLETE
    }
    private String filePath;
    private Status reportStatus;
    private AsyncDetails asyncDetails;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Status getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(Status reportStatus) {
        this.reportStatus = reportStatus;
    }

    public AsyncDetails getAsyncDetails() {
        return asyncDetails;
    }

    public void setAsyncDetails(AsyncDetails asyncDetails) {
        this.asyncDetails = asyncDetails;
    }

    public static class AsyncDetails {
        private int nextRunInMs;
        private int retryCount;

        public int getNextRunInMs() {
            return nextRunInMs;
        }

        public void setNextRunInMs(int nextRunInMs) {
            this.nextRunInMs = nextRunInMs;
        }

        public int getRetryCount() {
            return retryCount;
        }

        public void setRetryCount(int retryCount) {
            this.retryCount = retryCount;
        }
    }
}

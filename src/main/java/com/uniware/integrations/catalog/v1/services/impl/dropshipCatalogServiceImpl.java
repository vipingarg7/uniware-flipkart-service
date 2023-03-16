package com.uniware.integrations.catalog.v1.services.impl;

import com.google.gson.Gson;
import com.unicommerce.platform.aws.S3Service;
import com.unicommerce.platform.integration.NameValuePair;
import com.unicommerce.platform.integration.catalog.models.ChannelItemType;
import com.unicommerce.platform.integration.task.models.TaskResult;
import com.unicommerce.platform.integration.task.models.response.TaskResponse;
import com.unicommerce.platform.utils.StringUtils;
import com.unicommerce.platform.web.context.TenantRequestContext;
import com.unifier.core.fileparser.DelimitedFileParser;
import com.unifier.core.fileparser.ExcelSheetParser;
import com.unifier.core.fileparser.Row;
import com.unifier.core.utils.DateUtils;
import com.uniware.integrations.catalog.v1.services.ICatalogService;
import com.uniware.integrations.client.annotation.FlipkartClient;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.dto.api.requestDto.EnqueDownloadRequest;
import com.uniware.integrations.client.dto.api.responseDto.StockFileDownloadNUploadHistoryResponse;
import com.uniware.integrations.client.dto.api.responseDto.StockFileDownloadRequestStatusResponse;
import com.uniware.integrations.flipkart.services.FlipkartHelperService;
import com.uniware.integrations.flipkart.services.FlipkartSellerPanelService;
import com.unicommerce.platform.integration.Error;
import com.uniware.integrations.web.exception.FailureResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.unicommerce.platform.integration.task.models.TaskResult.Status.RUNNING;
import static com.uniware.integrations.client.constants.flipkartConstants.CATALOG;
import static com.uniware.integrations.client.constants.flipkartConstants.COMPLETED;
import static com.uniware.integrations.client.constants.flipkartConstants.DOWNLOADING;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_AND_SELLER;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_AND_SELLER_SMART;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_FA;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_LITE;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_OMNI;
import static com.uniware.integrations.client.constants.flipkartConstants.FLIPKART_SMART;
import static com.uniware.integrations.client.constants.flipkartConstants.SELLER;
import static com.uniware.integrations.client.constants.flipkartConstants.SELLERSMART;
import static com.uniware.integrations.client.constants.flipkartConstants.SELLER_SMART;
import static com.uniware.integrations.client.constants.flipkartConstants.TMP_DIR;
import static com.uniware.integrations.client.constants.flipkartConstants._2_GUD;

@Service
@FlipkartClient(module = "CATALOG", version = "v1", channelSource = ChannelSource.FLIPKART_DROPSHIP)
public class dropshipCatalogServiceImpl implements ICatalogService {

    @Autowired private FlipkartHelperService flipkartHelperService;
    @Autowired private FlipkartSellerPanelService flipkartSellerPanelService;
    @Autowired private S3Service s3Service;
    @Autowired private Gson gson;

    private Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();
    private static final String BUCKET_NAME = "uni-flipkart-integration";
    private static final Map<String, List<String>> sourceCodeToFulfilmentModeList = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(dropshipCatalogServiceImpl.class);

    static {
        for ( ChannelSource channelSource: ChannelSource.values() ) {

            if ( com.unifier.core.utils.StringUtils.equalsIngoreCaseAny(channelSource.getChannelSourceCode(),FLIPKART,_2_GUD,FLIPKART_OMNI)){
                sourceCodeToFulfilmentModeList.put(channelSource.getChannelSourceCode(),
                        Arrays.asList(SELLER,FLIPKART_AND_SELLER));

            }
            else if ( com.unifier.core.utils.StringUtils.equalsIngoreCaseAny(channelSource.getChannelSourceCode(),FLIPKART_LITE,FLIPKART_SMART)) {
                sourceCodeToFulfilmentModeList.put(channelSource.getChannelSourceCode(),Arrays.asList(SELLERSMART,SELLER_SMART,
                        FLIPKART_AND_SELLER_SMART));

            }
            else if ( com.unifier.core.utils.StringUtils.equalsIngoreCaseAny(channelSource.getChannelSourceCode(),FLIPKART_FA) ) {
                sourceCodeToFulfilmentModeList.put(channelSource.getChannelSourceCode(),Arrays.asList(FLIPKART,FLIPKART_AND_SELLER_SMART,FLIPKART_AND_SELLER));
            }
        }
    }

    @Override
    public TaskResponse enqueueReport() {

        TaskResponse taskResponse = new TaskResponse();
        TaskResult taskResult = new TaskResult();
        taskResult.setEntity(TaskResult.Entity.LISTING);

        boolean loginSuccess = flipkartSellerPanelService.sellerPanelLogin(FlipkartRequestContext.current().getUserName(), FlipkartRequestContext.current().getPassword());
        if (!loginSuccess) {
            taskResponse.setSuccessful(false);
            taskResponse.setErrors(Collections.singletonList(new Error("", "Failed to login on flipkart")));
            return taskResponse;
        }

        // check if already a file is under generation process or not.
        StockFileDownloadRequestStatusResponse stockFileDownloadRequestStatusResponse = flipkartSellerPanelService.getStockFileDownloadRequestStatus();
        if (DOWNLOADING.equalsIgnoreCase(stockFileDownloadRequestStatusResponse.getDownloadState())) {
            LOGGER.info("File generation state is - DOWNLOADING. Will retry after sometime...");
            taskResult.setStatus(RUNNING);
            taskResponse.setTask(taskResult);
            taskResponse.setSuccessful(true);
            return taskResponse;
        }

        boolean fetchCsrfToken = flipkartSellerPanelService.fetchCsrfToken();
        if (!fetchCsrfToken) {
            taskResponse.setSuccessful(false);
            taskResponse.setErrors(Collections.singletonList(new Error("", "failed to fetch on csrf token")));
            return taskResponse;
        }

        String lockKey = FlipkartRequestContext.current().getSellerId();
        Lock lock = lockMap.computeIfAbsent(lockKey, key-> new ReentrantLock());
        try {
            boolean isLockAcquired = lock.tryLock(10, TimeUnit.SECONDS);
            if ( isLockAcquired ) {
                stockFileDownloadRequestStatusResponse = flipkartSellerPanelService.getStockFileDownloadRequestStatus();
                if (DOWNLOADING.equalsIgnoreCase(stockFileDownloadRequestStatusResponse.getDownloadState())) {
                    LOGGER.info("File generation state is - DOWNLOADING. Will retry after sometime...");
                    taskResult.setStatus(RUNNING);
                    taskResponse.setTask(taskResult);
                    taskResponse.setSuccessful(true);
                    return taskResponse;
                }
                EnqueDownloadRequest enqueDownloadRequest = prepareEnqueStockFileRequest();
                flipkartSellerPanelService.requestStockFile(enqueDownloadRequest);
                LOGGER.info("initiated listing file generation");
            } else {
                LOGGER.error("failed to acquire lock while enqueue report");
                throw new FailureResponse("failed to acquire lock while enqueue report. Please retry after sometime");
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            lock.unlock();
        }

        stockFileDownloadRequestStatusResponse = flipkartSellerPanelService.getStockFileDownloadRequestStatus();
        return getListingFileGenerationStatus(taskResponse, taskResult, stockFileDownloadRequestStatusResponse);
    }

    // TODO : Error Code
    @Override
    public TaskResponse pollReportStatus() {

        TaskResponse taskResponse = new TaskResponse();
        TaskResult taskResult = new TaskResult();
        taskResult.setEntity(TaskResult.Entity.LISTING);

        // check if already a file is under generation process or not.
        StockFileDownloadRequestStatusResponse stockFileDownloadRequestStatusResponse = flipkartSellerPanelService.getStockFileDownloadRequestStatus();
        return getListingFileGenerationStatus(taskResponse, taskResult, stockFileDownloadRequestStatusResponse);
    }

    private TaskResponse getListingFileGenerationStatus(TaskResponse taskResponse, TaskResult taskResult, StockFileDownloadRequestStatusResponse stockFileDownloadRequestStatusResponse)
    {
        switch ( stockFileDownloadRequestStatusResponse.getDownloadState()) {
        case DOWNLOADING:
            LOGGER.info("File generation state is - DOWNLOADING. Will retry after sometime...");
            taskResult.setStatus(RUNNING);
            taskResponse.setTask(taskResult);
            taskResponse.setSuccessful(true);
            return taskResponse;
        case COMPLETED:
            LOGGER.info("File generation state is - COMPLETED.");
            String outputFileName = StringUtils.join('_', TenantRequestContext.current().getHttpSenderIdentifier(), CATALOG, FlipkartRequestContext.current().getSellerId(),FlipkartRequestContext.current().getChannelSource().getChannelSourceCode()) + ".jsonl";
            String outputFilePath = TMP_DIR + outputFileName;
            String lockKey = FlipkartRequestContext.current().getSellerId() + "_" + FlipkartRequestContext.current().getChannelSource().getChannelSourceCode();
            Lock lock = lockMap.computeIfAbsent(lockKey, key-> new ReentrantLock());
            try {
                boolean isLockAcquired = lock.tryLock(10, TimeUnit.SECONDS);
                if ( isLockAcquired ) {
                    String s3Url = flipkartHelperService.checkIfFileExistOnS3(outputFileName);
                    if (StringUtils.isNotBlank(s3Url)) {
                        taskResult.setStatus(TaskResult.Status.COMPLETED);
                        taskResult.addUrl(s3Url);
                        taskResponse.setTask(taskResult);
                        taskResponse.setSuccessful(true);
                        return taskResponse;
                    }

                    StockFileDownloadNUploadHistoryResponse stockFileDownloadNUploadHistoryResponse = flipkartSellerPanelService.getStockFileDownloadNUploadHistory();
                    String stockfilePath = getStockFile(stockFileDownloadNUploadHistoryResponse);
                    if ( stockfilePath == null ) {
                        taskResponse.setSuccessful(false);
                        taskResponse.setErrors(Collections.singletonList(new Error("", "failed to generate listing report")));
                        return taskResponse;
                    }
                    LOGGER.info("Stock file generation completed, filePath {}", stockfilePath);
                    processStockFile(stockfilePath, outputFilePath);
                    s3Url = s3Service.uploadFile(new File(outputFilePath),BUCKET_NAME);
                    taskResult.setStatus(TaskResult.Status.COMPLETED);
                    taskResult.addUrl(s3Url);
                    taskResponse.setTask(taskResult);
                    taskResponse.setSuccessful(true);
                    return taskResponse;
                } else {
                    LOGGER.info("failed to acquire lock. Will retry in next async run");
                    taskResult.setStatus(RUNNING);
                    taskResponse.setTask(taskResult);
                    taskResponse.setSuccessful(true);
                    return taskResponse;
                }
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            finally {
                lock.unlock();
            }
        default:
            taskResponse.setSuccessful(false);
            taskResponse.setErrors(Collections.singletonList(new Error("", "Invalid Status")));
            return taskResponse;
        }
    }

    private void processStockFile(String stockFilePath, String outputFilePath) {

        String fileFormat = stockFilePath.substring(stockFilePath.lastIndexOf('.')+1);
        Iterator<Row> rows;
        try {
            if ( stockFilePath.endsWith(".csv")){
                rows = new DelimitedFileParser(stockFilePath).parse(true);
            }
            else if ( stockFilePath.endsWith(".xls") ) {
                rows = new ExcelSheetParser(stockFilePath).parse(true);
            } else {
                LOGGER.error("file format not supported {}",fileFormat);
                throw new FailureResponse("file format not supported, fileFormat : " +fileFormat);
            }
        } catch ( Exception ex ) {
            LOGGER.error("Exception occur while parsing stock file, exception : {}", ex.getMessage());
            throw new FailureResponse("Exception occur while parsing stock file, exception : {}" + ex.getMessage());
        }

        long startTime = System.currentTimeMillis();
        try (FileWriter fWriter = new FileWriter(outputFilePath, Boolean.TRUE)) {
            while ( rows.hasNext() ) {
                Row row = rows.next();
                boolean isValid = validateRow(row);
                if (isValid) {
                    ChannelItemType channelItemType = ChannelItemType.builder()
                            .channelCode(TenantRequestContext.current().getChannelCode())
                            .channelProductId(flipkartHelperService.getChannelProductIdBySourceCode(row,TenantRequestContext.current().getSourceCode()))
                            .sellerSkuCode(row.getColumnValue("Seller SKU Id"))
                            .productName(row.getColumnValue("Product Title"))
                            .live(StringUtils.isBlank(row.getColumnValue("Inactive Reason")))
                            .sellingPrice(new BigDecimal(row.getColumnValue("Your Selling Price")))
                            .mrp(new BigDecimal(row.getColumnValue("MRP")))
                            .currencyCode("INR")
                            .build();


                    if (StringUtils.equalsIngoreCaseAny(TenantRequestContext.current().getSourceCode(), "FLIPKART_DROPSHIP", "FLIPKART_OMNI") ){
                        channelItemType.setAttributes(Collections.singletonList(
                                new NameValuePair().builder().name("FSN")
                                        .value(row.getColumnValue("Flipkart Serial Number")).build()));
                    }
                    gson.toJson(channelItemType, fWriter);
                }
            }

            fWriter.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        long endTime = System.currentTimeMillis();
        LOGGER.info("Listing file generation, StartTime : {}, EndTime : {}, TimeTaken : {}", startTime,endTime,(endTime-startTime));

    }

    private boolean validateRow(Row row) {

        if ( row.getColumnValue("Listing Archival") != null
                && ("ARCHIVED").equalsIgnoreCase(row.getColumnValue("Listing Archival"))) {
            LOGGER.info("Skipped row : {}, as either its  Listing Archival is null or has value ARCHIVED",row);
            return false;
        }   else if ( row.getColumnValue("Fulfillment By") != null
                && sourceCodeToFulfilmentModeList.get(TenantRequestContext.current().getSourceCode()).stream().noneMatch(row.getColumnValue("Fulfillment By")::equalsIgnoreCase)) {
            LOGGER.info("Skipped row : {}, Fulfiment mode not match",row);
            return false;
        }

        if ( FLIPKART_OMNI.equalsIgnoreCase(TenantRequestContext.current().getSourceCode())
                && !SELLER.equalsIgnoreCase(row.getColumnValue("Shipping Provider"))) {
            LOGGER.info("Skipped row : {}, For FLIPKART_OMNI we only fetch listings where SHIPPING_PROVDER : SELLER",row);
            return false;
        }
        return true;
    }

    private EnqueDownloadRequest prepareEnqueStockFileRequest() {

        EnqueDownloadRequest.Refiner refiner = new EnqueDownloadRequest.Refiner();

        EnqueDownloadRequest.InternalState internalState = new EnqueDownloadRequest.InternalState.Builder().setExactValue(new EnqueDownloadRequest.ExactValue.Builder().setValue("ACTIVE").build()).setValueType("EXACT").build();
        refiner.addInternalState(internalState);

        return new EnqueDownloadRequest.Builder().setState("LISTING_UI_GROUP").setRefiner(refiner).setVerticalGroup(new EnqueDownloadRequest.VerticalGroup()).build();
    }

    private String getStockFile(StockFileDownloadNUploadHistoryResponse stockFileDownloadNUploadHistoryResponse) {

        Optional<StockFileDownloadNUploadHistoryResponse.StockFileResponseList> stockFileResponseList = stockFileDownloadNUploadHistoryResponse.getStockFileResponseList().stream()
                .filter( e -> !e.getErrorRowsExists()
                        &&  ("GENERATED").equalsIgnoreCase(e.getFileOperationType())
                        && StringUtils.equalsIngoreCaseAny(e.getFileFormat(),"CSV","XLS")
                        && DateUtils.isToday(DateUtils.getDateFromEpoch(e.getUploadedOn())))
                .findFirst();
        if ( stockFileResponseList.isPresent()){
            StockFileDownloadNUploadHistoryResponse.StockFileResponseList fileDetails = stockFileResponseList.get();
            String fileLink = fileDetails.getFileLink();
            String fileFormat = fileDetails.getFileFormat();
            String fileName = fileDetails.getFileName();
            LOGGER.info("Stock file found with following details. FileName - {}, fileFormat - {}, FileLink - {}",fileName, fileFormat, fileLink);

            String downloadFilePath = flipkartHelperService.getFilePath(CATALOG,fileFormat);

            return flipkartSellerPanelService.downloadStockFile(fileName,fileLink,downloadFilePath);
        }
        else {
            LOGGER.info("Unable to found valid stock file. Either of following properties not match - operationType = GENERATED, errorRowExist = false, fileFormat - CSV or XLS ");
        }
        return null;
    }

}

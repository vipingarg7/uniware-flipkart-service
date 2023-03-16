package com.uniware.integrations.pendency.v1.services.impl;

import com.unicommerce.platform.integration.ScriptErrorCode;
import com.unicommerce.platform.integration.pendency.models.Pendency;
import com.unicommerce.platform.integration.pendency.models.response.FetchPendenciesResponse;
import com.unifier.core.fileparser.DelimitedFileParser;
import com.unifier.core.fileparser.Row;
import com.unifier.core.utils.DateUtils;
import com.uniware.integrations.client.annotation.FlipkartClient;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.dto.DateFilter;
import com.uniware.integrations.client.dto.Filter;
import com.uniware.integrations.client.dto.OrderItem;
import com.uniware.integrations.client.dto.Pagination;
import com.uniware.integrations.client.dto.Shipment;
import com.uniware.integrations.client.dto.Sort;
import com.uniware.integrations.client.dto.api.requestDto.SearchShipmentRequest;
import com.uniware.integrations.client.dto.api.responseDto.SearchShipmentV3Response;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentDetailsV3WithSubPackages;
import com.unicommerce.platform.integration.Error;
import com.uniware.integrations.flipkart.services.FlipkartHelperService;
import com.uniware.integrations.flipkart.services.FlipkartSellerApiService;
import com.uniware.integrations.flipkart.services.FlipkartSellerPanelService;
import com.uniware.integrations.pendency.v1.services.IPendencyService;
import com.uniware.integrations.web.exception.FailureResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.uniware.integrations.client.constants.flipkartConstants.CSV;
import static com.uniware.integrations.client.constants.flipkartConstants.DATE_PATTERN;
import static com.uniware.integrations.client.constants.flipkartConstants.PENDENCY;

@Service
@FlipkartClient(module = "PENDENCY", version = "v1", channelSource = ChannelSource.FLIPKART_DROPSHIP)
public class dropshipPendencyServiceImpl implements IPendencyService {

    @Autowired private FlipkartHelperService flipkartHelperService;
    @Autowired private FlipkartSellerApiService flipkartSellerApiService;
    @Autowired private FlipkartSellerPanelService flipkartSellerPanelService;

    private static final Logger LOGGER = LoggerFactory.getLogger(dropshipPendencyServiceImpl.class);

    @Override
    public FetchPendenciesResponse fetchPendency() {

        FetchPendenciesResponse fetchPendencyResponse = new FetchPendenciesResponse();

        boolean loginSuccess = flipkartSellerPanelService.sellerPanelLogin(FlipkartRequestContext.current().getUserName(), FlipkartRequestContext.current().getPassword());
        if (!loginSuccess) {
            fetchPendencyResponse.setSuccessful(false);
            fetchPendencyResponse.addError(new Error(ScriptErrorCode.CHANNEL_ERROR.name(),"Login Session could not be established"));
            return fetchPendencyResponse;
        }

        Map<String, Pendency> channelProductIdToPendency = new HashMap<>();

        getPendencyOfApprovedShipments(channelProductIdToPendency);
        getPendencyOfOnHoldOrdersFromReport(channelProductIdToPendency);

        List<Pendency> pendencies = new ArrayList<>(channelProductIdToPendency.values());
        fetchPendencyResponse.addPendencies(pendencies);
        fetchPendencyResponse.setSuccessful(true);

        return fetchPendencyResponse;
    }


    private void getPendencyOfApprovedShipments(Map<String, Pendency> channelProductIdToPendency) {

        Set<String> shipmentIdSet = new HashSet<>();
        Date orderDateOfLastOrderOfLastpage = null;
        String nextPageUrl = null;
        boolean postApiCall = true;
        boolean hasMore = false;
        int pageNumber = 1;
        SearchShipmentV3Response searchShipmentV3Response = null;
        Date skipSelfShipOrdersBeforeDate = DateUtils.addToDate(DateUtils.getCurrentTime(), Calendar.DATE,2);

        do {
            LOGGER.info("Fetching pendency for page number  : {}", pageNumber);
            hasMore = false;

            if (postApiCall) {
                SearchShipmentRequest searchShipmentRequest = prepareFetchPendencyRequestForApprovedShipments(orderDateOfLastOrderOfLastpage);
                searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentPost(searchShipmentRequest);
                postApiCall = false;
            }
            else {
                searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentGet(nextPageUrl);
            }
            if ( searchShipmentV3Response != null ) {
                for (Shipment shipment : searchShipmentV3Response.getShipments()) {

                    // fetching these shipments as orders in uniware.
                    if ( "SELF".equalsIgnoreCase(shipment.getShipmentType()) && DateUtils.diff(shipment.getDispatchAfterDate() ,skipSelfShipOrdersBeforeDate,
                            DateUtils.Resolution.MILLISECOND) > 0)
                        continue;

                    // Adding shipments in a HashSet to avoid duplicity of shipments in api response
                    if ( shipmentIdSet.add(shipment.getShipmentId())) {
                        int qty = 0;
                        for (OrderItem orderItem : shipment.getOrderItems()) {
                            String listingId = orderItem.getListingId();
                            qty += orderItem.getQuantity();
                            if (channelProductIdToPendency.computeIfPresent(listingId, (key, val) -> val.addRequiredInventory(orderItem.getQuantity())) == null) {
                                Pendency pendency = new Pendency();
                                pendency.setChannelProductId(listingId);
                                pendency.setProductName(orderItem.getTitle());
                                pendency.setRequiredInventory(orderItem.getQuantity());
                                pendency.setSellerSkuCode(orderItem.getSku());
                                channelProductIdToPendency.put(listingId,pendency);
                            }
                        }
                        LOGGER.info("Pendency count for order : {} is : {}", shipment.getOrderItems().get(0).getOrderId(), qty);
                    }
                    if ( (pageNumber % 249 ) == 0) {
                        Date orderDate = shipment.getOrderItems().get(0).getOrderDate();
                        orderDateOfLastOrderOfLastpage = orderDateOfLastOrderOfLastpage != null ? orderDateOfLastOrderOfLastpage : orderDate;
                        if ( (orderDate.getTime() - orderDateOfLastOrderOfLastpage.getTime()) > 0){
                            orderDateOfLastOrderOfLastpage = orderDate;
                        }
                        postApiCall = true;
                    }
                }

                nextPageUrl = searchShipmentV3Response.getNextPageUrl();
                if ( searchShipmentV3Response.isHasMore()) {
                    hasMore = true;
                }
            }

            pageNumber++;
        } while (hasMore);

        LOGGER.info("Fetched pendency till page number :{}", --pageNumber);

    }

    private SearchShipmentRequest prepareFetchPendencyRequestForApprovedShipments(Date fromOrderDate) {

        Filter filter = new Filter();
        filter.addStatesItem(Filter.StatesEnum.APPROVED);
        filter.locationId(FlipkartRequestContext.current().getLocationId());
        filter.type(Filter.TypeEnum.PREDISPATCH);
        filter.addShipmentTypesItem(Filter.ShipmentTypesEnum.SELF);
        filter.addShipmentTypesItem(Filter.ShipmentTypesEnum.NORMAL);
        filter.addShipmentTypesItem(Filter.ShipmentTypesEnum.EXPRESS);

        DateFilter dispatchAfterDateFilter = new DateFilter();
        dispatchAfterDateFilter.from(DateUtils.dateToString(DateUtils.getCurrentTime(),DATE_PATTERN));
        filter.dispatchAfterDate(dispatchAfterDateFilter);

        if ( fromOrderDate != null) {
            DateFilter orderDateFilter = new DateFilter();
            orderDateFilter.from(DateUtils.dateToString(fromOrderDate,DATE_PATTERN));
            orderDateFilter.to(DateUtils.dateToString(DateUtils.getCurrentTime(),DATE_PATTERN));
            filter.orderDate(orderDateFilter);
        }

        Sort sort = new Sort();
        sort.setField(Sort.FieldEnum.ORDERDATE);
        sort.setOrder(Sort.OrderEnum.ASC);

        Pagination pagination = new Pagination();
        pagination.pageSize(20);

        SearchShipmentRequest searchShipmentRequest = new SearchShipmentRequest();
        searchShipmentRequest.setFilter(filter);
        searchShipmentRequest.setPagination(pagination);
        searchShipmentRequest.setSort(sort);

        return searchShipmentRequest;
    }

    // Fetching onhold orders from report instead of scraping API, assuming number of ONHOLD orders count is low.
    private void getPendencyOfOnHoldOrdersFromReport(Map<String,Pendency> channelProductIdToPendency) {

        String onHoldOrderReport = flipkartHelperService.getFilePath(PENDENCY,CSV);
        flipkartSellerPanelService.getOnHoldOrdersReport(onHoldOrderReport);
        LOGGER.info("Onhold orders report downloaded, filepath {}", onHoldOrderReport);
        Iterator<Row> rows = null;
        try {
            rows = new DelimitedFileParser(onHoldOrderReport).parse();
        }
        catch (Exception ex) {
            LOGGER.error("Exception occur while parsing stock file, exception : {}", ex);
            throw new FailureResponse("Exception occur while parsing on hold orders report" + ex.getMessage());
        }

        int shipmentBatchSize = 20;
        boolean hasAnyOnHoldShipment = false;
        ArrayList<ArrayList<String>> shipmentIdsInBatches= new ArrayList<>();
        int counter = 0;
        int batchCounter = 0;
        while (rows.hasNext()) {
            Row row = rows.next();
            if ( shipmentIdsInBatches.size() == batchCounter ) {
                shipmentIdsInBatches.add(new ArrayList<>());
            }
            shipmentIdsInBatches.get(batchCounter).add(row.getColumnValue("Shipment ID"));
            counter++;
            if ( counter % shipmentBatchSize == 0) {
                counter = 0;
                batchCounter++;
            }
            hasAnyOnHoldShipment = true;
        }

        if ( !hasAnyOnHoldShipment ) {
            LOGGER.info("There is not any shipment in ONHOLD state");
            return;
        }

        int totolPendencyForOnholdOrders = 0;
        for (List<String> shipmentIds: shipmentIdsInBatches) {
            ShipmentDetailsV3WithSubPackages shipmentDetails = flipkartSellerApiService.getShipmentDetailsWithSubPackages(shipmentIds);
            for (Shipment shipment: shipmentDetails.getShipments()) {
                int qty = 0;
                for (OrderItem orderItem: shipment.getOrderItems()) {
                    String listingId = orderItem.getListingId();
                    qty += orderItem.getQuantity();
                    if (channelProductIdToPendency.computeIfPresent(listingId, (key, val) -> val.addRequiredInventory(orderItem.getQuantity())) == null) {
                        Pendency pendency = new Pendency();
                        pendency.setChannelProductId(listingId);
                        pendency.setProductName(orderItem.getTitle());
                        pendency.setRequiredInventory(orderItem.getQuantity());
                        pendency.setSellerSkuCode(orderItem.getSku().replaceAll("&quot;", ""));
                        channelProductIdToPendency.put(listingId,pendency);
                    }
                    LOGGER.info("Pendency count for order : {} is : {}", shipment.getOrderItems().get(0).getOrderId(), qty);
                }
                totolPendencyForOnholdOrders += qty;
            }
        }
        LOGGER.info("Pendency of Onhold orders : {}",totolPendencyForOnholdOrders);

    }

}

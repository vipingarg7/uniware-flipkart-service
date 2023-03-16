package com.uniware.integrations.order.v1.services.impl;

import com.unicommerce.platform.integration.DateRange;
import com.unicommerce.platform.integration.Filters;
import com.unicommerce.platform.integration.order.models.AddressDetail;
import com.unicommerce.platform.integration.order.models.AddressRef;
import com.unicommerce.platform.integration.order.models.SaleOrder;
import com.unicommerce.platform.integration.order.models.SaleOrderItem;
import com.unicommerce.platform.integration.order.models.request.FetchOrdersRequest;
import com.unicommerce.platform.integration.order.models.response.FetchOrdersResponse;
import com.unicommerce.platform.web.context.TenantRequestContext;
import com.unifier.core.utils.DateUtils;
import com.unifier.core.utils.JsonUtils;
import com.unifier.core.utils.NumberUtils;
import com.unifier.core.utils.StringUtils;
import com.uniware.integrations.client.annotation.FlipkartClient;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.dto.Address;
import com.uniware.integrations.client.dto.DateFilter;
import com.uniware.integrations.client.dto.Filter;
import com.uniware.integrations.client.dto.OrderItem;
import com.uniware.integrations.client.dto.Pagination;
import com.uniware.integrations.client.dto.PriceComponent;
import com.uniware.integrations.client.dto.Shipment;
import com.uniware.integrations.client.dto.ShipmentDetails;
import com.uniware.integrations.client.dto.Sort;
import com.uniware.integrations.client.dto.api.requestDto.SearchShipmentRequest;
import com.uniware.integrations.client.dto.api.responseDto.SearchShipmentV3Response;
import com.uniware.integrations.client.dto.api.responseDto.ShipmentDetailsV3WithAddressResponse;
import com.uniware.integrations.flipkart.services.FlipkartSellerApiService;
import com.uniware.integrations.order.v1.services.IOrderService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.uniware.integrations.client.constants.flipkartConstants.DATE_PATTERN;
import static com.uniware.integrations.client.constants.flipkartConstants.DEFAULT_PHONE_NUMBER;

@Service
@FlipkartClient(module = "ORDER", version = "v1", channelSource = ChannelSource.FLIPKART_DROPSHIP)
public class dropshipOrderServiceImpl implements IOrderService {

    @Autowired private FlipkartSellerApiService flipkartSellerApiService;
    private static final Logger LOGGER = LoggerFactory.getLogger(dropshipOrderServiceImpl.class);

    // TODO : Have to check FetchOrdersRequest variable name with Ashish
    @Override public FetchOrdersResponse fetchOrders(FetchOrdersRequest fetchOrdersRequest) {

        FetchOrdersResponse fetchOrdersResponse = new FetchOrdersResponse();

        // Bw 4:00 - 5:00 AM daily fetch orders of last 5 days by override the date in filter in request, else fetch as per the date filter in request body.
        if ( DateUtils.getCurrentHour() == 4 ) {
            Filters filter = new Filters();
            filter.setDateRange(new DateRange(DateUtils.addToDate(DateUtils.getCurrentTime(), Calendar.DATE,-5),DateUtils.getCurrentTime()));
            fetchOrdersRequest.setFilters(filter);
        }

        // TODO : Add validation on metadata

        int pageNumber = (int) fetchOrdersRequest.getMetadata().get("pageNumber");

        if ( fetchOrdersRequest.getMetadata() == null || fetchOrdersRequest.getMetadata().get("hasMoreNormalShipments") == null || (boolean) fetchOrdersRequest.getMetadata().get("hasMoreNormalShipments")) {
            LOGGER.info("Fetching normal shipments, pageNumber: {}", pageNumber);
            getNormalShipments(fetchOrdersRequest, fetchOrdersResponse);
        }
        if ( fetchOrdersRequest.getMetadata() == null || fetchOrdersRequest.getMetadata().get("hasMoreExpressShipments") == null || (boolean) fetchOrdersRequest.getMetadata().get("hasMoreExpressShipments")) {
            LOGGER.info("Fetching express shipments, pageNumber: {}", pageNumber);
            getExpressShipments(fetchOrdersRequest, fetchOrdersResponse);
        }
        if ( fetchOrdersRequest.getMetadata() == null || fetchOrdersRequest.getMetadata().get("hasMoreSelfShipments") == null || (boolean) fetchOrdersRequest.getMetadata().get("hasMoreSelfShipments")) {
            LOGGER.info("Fetching self shipments, pageNumber: {}", pageNumber);
            getSelfShipments(fetchOrdersRequest, fetchOrdersResponse);
        }

        if ( (boolean) fetchOrdersResponse.getMetadata().get("hasMoreNormalShipments") || (boolean) fetchOrdersResponse.getMetadata().get("hasMoreExpressShipments") || (boolean) fetchOrdersResponse.getMetadata().get("hasMoreSelfShipments")) {
            fetchOrdersResponse.getMetadata().put("totalPages",pageNumber+1);
        }

        fetchOrdersResponse.setSuccessful(true);
        return fetchOrdersResponse;
    }

    private void getNormalShipments(FetchOrdersRequest fetchOrdersRequest, FetchOrdersResponse fetchOrdersResponse) {

        SearchShipmentV3Response searchShipmentV3Response;

        int pageNumber = (int) fetchOrdersRequest.getMetadata().get("pageNumber");
        Date orderDateOfLastOrderOfLastPage = null;

        if ( (pageNumber % 249) == 1 ) {

            if (pageNumber != 1) {
                Object dateObject = fetchOrdersRequest.getMetadata().get("orderDateOfLastOrderOfLastPageOfNormalShipments");
                if ( dateObject == null ) {
                    LOGGER.info("orderDateOfLastOrderOfLastPage can't be null for page number more than 1, pageNumber : {}",pageNumber);
                    return;
                }
                orderDateOfLastOrderOfLastPage = new Date((Long) dateObject);

            }

            SearchShipmentRequest searchShipmentRequest = prepareSearchShimentRequest(Filter.ShipmentTypesEnum.NORMAL, fetchOrdersRequest.getFilters(), orderDateOfLastOrderOfLastPage);
            searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentPost(searchShipmentRequest);
        }
        else {
            String nextPageUrl = (String) fetchOrdersRequest.getMetadata().get("nextPageUrlForNormalShipments");
            searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentGet(nextPageUrl);
        }

        if (searchShipmentV3Response != null ){
            if ( searchShipmentV3Response.getShipments().size() > 0 ) {
                List<SaleOrder> saleOrderList = createSaleOrder(searchShipmentV3Response);
                fetchOrdersResponse = fetchOrdersResponse.addOrders(saleOrderList);

                if ( (pageNumber % 249) == 0){
                    SaleOrder saleOrder = Collections.min(saleOrderList, Comparator.comparing(
                            SaleOrder::getDisplayOrderDateTime));
                    fetchOrdersResponse.getMetadata().put("orderDateOfLastOrderOfLastPageOfNormalShipments",DateUtils.addToDate(saleOrder.getDisplayOrderDateTime(),Calendar.MINUTE,-10));
                }
            }

            if ( searchShipmentV3Response.isHasMore()){
                fetchOrdersResponse.getMetadata().put("nextPageUrlForNormalShipments", searchShipmentV3Response.getNextPageUrl());
                fetchOrdersResponse.getMetadata().put("hasMoreNormalShipments",true);
            } else{
                fetchOrdersResponse.getMetadata().put("hasMoreNormalShipments",false);
            }
        }
    }

    private void getSelfShipments(FetchOrdersRequest fetchOrdersRequest, FetchOrdersResponse fetchOrdersResponse) {

        SearchShipmentV3Response searchShipmentV3Response;

        int pageNumber = (int) fetchOrdersRequest.getMetadata().get("pageNumber");
        Date orderDateOfLastOrderOfLastPage = null;

        if ( (pageNumber % 249) == 1 ) {

            if (pageNumber != 1) {
                Object dateObject = fetchOrdersRequest.getMetadata().get("orderDateOfLastOrderOfLastPageOfSelfShipments");
                if ( dateObject == null ) {
                    LOGGER.info("orderDateOfLastOrderOfLastPage can't be null for page number more than 1, pageNumber : {}",pageNumber);
                    return;
                }
                orderDateOfLastOrderOfLastPage = new Date((Long) dateObject);
            }

            SearchShipmentRequest searchShipmentRequest = prepareSearchShimentRequest(Filter.ShipmentTypesEnum.SELF,
                    fetchOrdersRequest.getFilters(), orderDateOfLastOrderOfLastPage);
            searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentPost(searchShipmentRequest);
        }
        else {
            String nextPageUrl = (String) fetchOrdersRequest.getMetadata().get("nextPageUrlForSelfShipments");
            searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentGet(nextPageUrl);
        }

        if (searchShipmentV3Response != null ){
            if ( searchShipmentV3Response.getShipments().size() > 0 ) {
                List<SaleOrder> saleOrderList = createSaleOrder(searchShipmentV3Response);
                fetchOrdersResponse = fetchOrdersResponse.addOrders(saleOrderList);

                if ( (pageNumber % 249) == 0 ){
                    SaleOrder saleOrder = Collections.min(saleOrderList, Comparator.comparing(
                            SaleOrder::getDisplayOrderDateTime));
                    fetchOrdersResponse.getMetadata().put("orderDateOfLastOrderOfLastPageOfSelfShipments",DateUtils.addToDate(saleOrder.getDisplayOrderDateTime(),Calendar.MINUTE,-10));
                }
            }

            if ( searchShipmentV3Response.isHasMore()){
                fetchOrdersResponse.getMetadata().put("nextPageUrlForSelfShipments", searchShipmentV3Response.getNextPageUrl());
                fetchOrdersResponse.getMetadata().put("hasMoreSelfShipments",true);
            } else{
                fetchOrdersResponse.getMetadata().put("hasMoreSelfShipments",false);
            }
        }
    }

    private void getExpressShipments(FetchOrdersRequest fetchOrdersRequest, FetchOrdersResponse fetchOrdersResponse) {

        SearchShipmentV3Response searchShipmentV3Response;

        int pageNumber = (int) fetchOrdersRequest.getMetadata().get("pageNumber");
        Date orderDateOfLastOrderOfLastPage = null;

        if ( pageNumber % 249 == 1 ) {

            if (pageNumber != 1) {
                Object dateObject = fetchOrdersRequest.getMetadata().get("orderDateOfLastOrderOfLastPageOfExpressShipments");
                if ( dateObject == null ) {
                    LOGGER.info("orderDateOfLastOrderOfLastPage can't be null for page number more than 1, pageNumber : {}",pageNumber);
                    return;
                }
                orderDateOfLastOrderOfLastPage = new Date((Long) dateObject);
            }

            SearchShipmentRequest searchShipmentRequest = prepareSearchShimentRequest(Filter.ShipmentTypesEnum.EXPRESS,
                    fetchOrdersRequest.getFilters(), orderDateOfLastOrderOfLastPage);
            searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentPost(searchShipmentRequest);
        }
        else {
            String nextPageUrl = (String) fetchOrdersRequest.getMetadata().get("nextPageUrlForExpressShipments");
            searchShipmentV3Response = flipkartSellerApiService.searchPreDispatchShipmentGet(nextPageUrl);
        }

        if (searchShipmentV3Response != null ){
            if ( searchShipmentV3Response.getShipments().size() > 0 ) {
                List<SaleOrder> saleOrderList = createSaleOrder(searchShipmentV3Response);
                fetchOrdersResponse = fetchOrdersResponse.addOrders(saleOrderList);

                if ( (pageNumber % 249) == 0 ){
                    SaleOrder saleOrder = Collections.min(saleOrderList, Comparator.comparing(
                            SaleOrder::getDisplayOrderDateTime));
                    fetchOrdersResponse.getMetadata().put("orderDateOfLastOrderOfLastPageOfExpressShipments",DateUtils.addToDate(saleOrder.getDisplayOrderDateTime(),Calendar.MINUTE,-10));
                }
            }

            if ( searchShipmentV3Response.isHasMore()){
                fetchOrdersResponse.getMetadata().put("nextPageUrlForExpressShipments", searchShipmentV3Response.getNextPageUrl());
                fetchOrdersResponse.getMetadata().put("hasMoreExpressShipments",true);
            } else{
                fetchOrdersResponse.getMetadata().put("hasMoreExpressShipments",false);
            }
        }
    }

    private SearchShipmentRequest prepareSearchShimentRequest(Filter.ShipmentTypesEnum shipmentType, Filters requestFilter, Date orderDateOfLastOrderOfLastPage) {

        DateFilter DispatchAfterDateFilter = new DateFilter();
        DispatchAfterDateFilter.from(DateUtils.dateToString((requestFilter.getDateRange().getFrom()),DATE_PATTERN));
        if ( Filter.ShipmentTypesEnum.SELF.equals(shipmentType) ) {
            DispatchAfterDateFilter.to(DateUtils.dateToString(DateUtils.addToDate(DateUtils.getCurrentTime(),Calendar.DATE,2 ),DATE_PATTERN));
        } else {
            DispatchAfterDateFilter.to(DateUtils.dateToString(DateUtils.getCurrentTime(),DATE_PATTERN));
        }

        DateFilter orderDateFilter = new DateFilter();
        orderDateFilter.from(DateUtils.dateToString(DateUtils.addToDate(DateUtils.getCurrentDate(), Calendar.DATE, -150),DATE_PATTERN));
        if (orderDateOfLastOrderOfLastPage != null )
            orderDateFilter.to((DateUtils.dateToString(orderDateOfLastOrderOfLastPage,DATE_PATTERN)));
        else
            orderDateFilter.to((DateUtils.dateToString(DateUtils.getCurrentTime(),DATE_PATTERN)));

        Filter filter = new Filter();
        filter.type(Filter.TypeEnum.PREDISPATCH);
        filter.dispatchAfterDate(DispatchAfterDateFilter);
        filter.orderDate(orderDateFilter);
        filter.locationId(FlipkartRequestContext.current().getLocationId());
        filter.addStatesItem(Filter.StatesEnum.APPROVED);
        filter.addShipmentTypesItem(shipmentType);

        Sort sort = new Sort();
        sort.setField(Sort.FieldEnum.ORDERDATE);
        sort.setOrder(Sort.OrderEnum.DESC);

        Pagination pagination = new Pagination();
        pagination.pageSize(20);

        SearchShipmentRequest searchShipmentRequest = new SearchShipmentRequest();
        searchShipmentRequest.setFilter(filter);
        searchShipmentRequest.setPagination(pagination);
        searchShipmentRequest.setSort(sort);
        return searchShipmentRequest;
    }

    private List<SaleOrder> createSaleOrder(SearchShipmentV3Response searchShipmentV3Response) {

        List<String> shipmentIds = searchShipmentV3Response.getShipments().stream().map(Shipment::getShipmentId).collect(Collectors.toList());
        ShipmentDetailsV3WithAddressResponse shipmentDetailsWithAddressSearchResponse = flipkartSellerApiService.getShipmentDetailsWithAddress(shipmentIds);

        Map<String, ShipmentDetails> shipmentIdToShipmentAddressDetails = shipmentDetailsWithAddressSearchResponse.getShipments().stream().collect(
                Collectors.toMap(ShipmentDetails::getShipmentId, Function.identity() ));

        List<SaleOrder> saleOrderList = new ArrayList<>();

        for (Shipment shipment: searchShipmentV3Response.getShipments()) {

            ShipmentDetails shipmentDetailsWithAddress = shipmentIdToShipmentAddressDetails.get(shipment.getShipmentId());

            String orderId = null;
            for (OrderItem orderItem : shipment.getOrderItems()) {
                PriceComponent priceComponent = orderItem.getPriceComponents();
                if ( NumberUtils.greaterThan(priceComponent.getSellingPrice(), BigDecimal.ZERO)
                        || NumberUtils.greaterThan(priceComponent.getTotalPrice(),BigDecimal.ZERO)
                        || NumberUtils.greaterThan(priceComponent.getShippingCharge(),BigDecimal.ZERO)
                        || NumberUtils.greaterThan(priceComponent.getCustomerPrice(),BigDecimal.ZERO)
                        || NumberUtils.greaterThan(priceComponent.getFlipkartDiscount(),BigDecimal.ZERO) ){
                    orderId = orderItem.getOrderItemId();
                    break;
                }
            }

            SaleOrder saleOrder = new SaleOrder();
            saleOrder.setCode(orderId);
            saleOrder.setDisplayOrderCode(shipment.getOrderItems().get(0).getOrderId());
            saleOrder.setDisplayOrderDateTime(shipment.getOrderItems().get(0).getOrderDate());
            saleOrder.setFulfillmentTat(shipment.getDispatchByDate());
            saleOrder.setChannel(TenantRequestContext.current().getChannelCode());
            if ( StringUtils.isNotBlank(shipment.getOrderItems().get(0).getPaymentType().name())) {
                saleOrder.setCashOnDelivery("COD".equalsIgnoreCase(shipment.getOrderItems().get(0).getPaymentType().name()));
            } else {
                saleOrder.setCashOnDelivery(true);
            }
            String customerPhoneNumber = shipmentDetailsWithAddress.getDeliveryAddress().getContactNumber();
            saleOrder.setNotificationMobile(StringUtils.isNotBlank(customerPhoneNumber) ? customerPhoneNumber : DEFAULT_PHONE_NUMBER);
            Map<String, Object> shipmentIdJson = new HashMap<>();
            Map<String,String> tempMap = new HashMap<>();
            tempMap.put("value",shipment.getShipmentId());
            shipmentIdJson.put("shipmentId",tempMap);
            saleOrder.setAdditionalInfo(JsonUtils.objectToString(shipmentIdJson));

            if ( "EXPRESS".equalsIgnoreCase(shipment.getShipmentType()) ) {
                saleOrder.setPriority(1);
            }
            if ( "SELF".equalsIgnoreCase(shipment.getShipmentType()) ) {
                saleOrder.setThirdPartyShipping(false);
            }

            List<SaleOrderItem> saleOrderItems = new ArrayList<>();
            for (OrderItem orderItem: shipment.getOrderItems()) {
                if ( "CANCELLED".equalsIgnoreCase(orderItem.getStatus().name())) {
                    LOGGER.info("Skipping orderItem : {}, orderCode : {}, since it's status is CANCELLED",orderItem.getOrderItemId(),orderItem.getOrderId());
                    continue;
                }
                for (int count = 1; count <= orderItem.getQuantity(); count++) {
                    SaleOrderItem saleOrderItem = new SaleOrderItem();
                    saleOrderItem.setCode(orderItem.getOrderItemId() + "-" + count);
                    saleOrderItem.setChannelSaleOrderItemCode(orderItem.getOrderItemId());
                    saleOrderItem.setItemSku(orderItem.getSku().replaceAll("&quot;", ""));
                    saleOrderItem.setChannelProductId(orderItem.getListingId().replaceAll("&quot;", ""));

                    String itemName = orderItem.getTitle() + " " + orderItem.getSku().replaceAll("&quot;", "");
                    saleOrderItem.setItemName(itemName.length() > 200 ? itemName.substring(0,200) : itemName);
                    saleOrderItem.setShippingMethodCode("STD");

                    BigDecimal quantity = BigDecimal.valueOf(orderItem.getQuantity());
                    PriceComponent priceComponent = orderItem.getPriceComponents();
                    BigDecimal sellingPrice = priceComponent.getSellingPrice();
                    BigDecimal customerPrice = priceComponent.getCustomerPrice();
                    BigDecimal shippingCharges = priceComponent.getShippingCharge();
                    BigDecimal totalPrice = priceComponent.getTotalPrice();
                    BigDecimal discount = priceComponent.getFlipkartDiscount();

                    //in v3, customer price includes shipping charges, so to get the true customer price, we always need to subtract shipping charges from it.
                    discount = customerPrice.subtract(shippingCharges).subtract(sellingPrice);

                    if (NumberUtils.lessThan(discount,new BigDecimal(0)))
                        discount =  discount.multiply(new BigDecimal(-1));

                    if (NumberUtils.lessThan(customerPrice.subtract(shippingCharges),sellingPrice))
                        sellingPrice = customerPrice.subtract(shippingCharges);

                    saleOrderItem.setSellingPrice(NumberUtils.divide(sellingPrice,quantity));
                    saleOrderItem.setDiscount(NumberUtils.divide(discount,quantity));
                    saleOrderItem.setShippingCharges(NumberUtils.divide(shippingCharges,quantity));
                    saleOrderItem.setTotalPrice(NumberUtils.divide(totalPrice,quantity));
                    saleOrderItem.setPacketNumber(1);
                    saleOrderItems.add(saleOrderItem);
                }
            }
            saleOrder.setSaleOrderItems(saleOrderItems);

            List<AddressDetail> addresses = new ArrayList<>();
            addresses.add(getAddressDetails(shipmentDetailsWithAddress.getDeliveryAddress(),"1"));
            saleOrder.setAddresses(addresses);

            AddressRef shippingAddressRef = new AddressRef();
            shippingAddressRef.setReferenceId(addresses.get(0).getId());
            saleOrder.setShippingAddress(shippingAddressRef);

            AddressRef billingAddressRef = new AddressRef();
            billingAddressRef.setReferenceId(addresses.get(0).getId());
            saleOrder.setBillingAddress(billingAddressRef);

            saleOrderList.add(saleOrder);
        }

        return saleOrderList;
    }

    private AddressDetail getAddressDetails(Address address,String id) {
        AddressDetail addressDetail = new AddressDetail();
        addressDetail.setName(address.getFirstName() + " " + address.getLastName());
        addressDetail.setAddressLine1(address.getAddressLine1());

        if ( StringUtils.isNotBlank(address.getAddressLine2())) {
            addressDetail.setAddressLine2(address.getAddressLine2());
        }

        addressDetail.setCity(address.getCity());
        addressDetail.setState(address.getState());
        addressDetail.setCountry("IN");
        addressDetail.setPincode(address.getPinCode());

        if ( StringUtils.isNotBlank(address.getContactNumber())) {
            addressDetail.setPhone(address.getContactNumber());
        } else {
            addressDetail.setPhone(DEFAULT_PHONE_NUMBER);
        }

        addressDetail.setId(id);
        return addressDetail;

    }

}

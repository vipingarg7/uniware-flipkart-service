package com.uniware.integrations.client.dto.uniware;

import com.uniware.integrations.uniware.dto.rest.api.SaleOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class FetchOrderResponse {

    private List<SaleOrder>     orders;
    private String              nextToken;
    private boolean             hasMoreOrders = false;
    private Map<String,Object>  metadata;
    private int                 totalPages;

    public FetchOrderResponse addOrders(List<SaleOrder> orders) {
        if (this.orders == null) {
            this.orders = new ArrayList<>();
        }
        this.orders.addAll(orders);
        return this;
    }

    public Map<String, Object> getMetadata() {
        if ( this.metadata == null )
            this.metadata = new HashMap<>();
        return metadata;
    }

}

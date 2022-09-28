package com.uniware.integrations.client.dto.uniware;

import java.util.ArrayList;
import java.util.List;

public class CatalogSyncResponse {

    private List<ChannelItemType> channelItemTypes;
    private boolean hasMore = false;
    private int totalPages;

    public CatalogSyncResponse addChannelItemType(ChannelItemType cit) {
        if ( channelItemTypes == null) {
            this.channelItemTypes = new ArrayList<>();
        }
        this.channelItemTypes.add(cit);
        return this;
    }

    public List<ChannelItemType> getChannelItemTypes() {
        return channelItemTypes;
    }

    public void setChannelItemTypes(List<ChannelItemType> channelItemTypes) {
        this.channelItemTypes = channelItemTypes;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}

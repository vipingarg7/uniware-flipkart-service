package com.uniware.integrations.client.dto.api.requestDto;

import java.util.List;
import lombok.Data;

@Data
public class ListingFilterRequest {

    private String searchText;
    private SearchFilter searchFilter;
    private Column column;

    @Data
    public static class SearchFilter{
        private String internal_state;
        private List<String> service_profile;
    }

    @Data
    public static class Column{
        private Pagination pagination;
    }

    @Data
    public static class Pagination{
        private int batchNo;
        private int batchSize;
    }
}

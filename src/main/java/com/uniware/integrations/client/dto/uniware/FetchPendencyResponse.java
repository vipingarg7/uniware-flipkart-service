package com.uniware.integrations.client.dto.uniware;

import java.util.List;
import lombok.Data;

@Data
public class FetchPendencyResponse {

    private List<Pendency> pendencyList;

    public List<Pendency> getPendencyList() {
        return pendencyList;
    }

    public void setPendencyList(List<Pendency> pendencyList) {
        this.pendencyList = pendencyList;
    }
}

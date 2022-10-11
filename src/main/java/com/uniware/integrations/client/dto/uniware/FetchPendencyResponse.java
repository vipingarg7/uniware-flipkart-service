package com.uniware.integrations.client.dto.uniware;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class FetchPendencyResponse {

    private List<Pendency> pendencyList;

    public FetchPendencyResponse addPendency(Pendency pendency){
        if (this.pendencyList == null ) {
            this.pendencyList = new ArrayList<>();
        }
        this.pendencyList.add(pendency);
        return this;
    }

    public FetchPendencyResponse addPendency(List<Pendency> pendencyList){
        if (this.pendencyList == null ) {
            this.pendencyList = new ArrayList<>();
        }
        this.pendencyList.addAll(pendencyList);
        return this;
    }

    public List<Pendency> getPendencyList() {
        return pendencyList;
    }

    public void setPendencyList(List<Pendency> pendencyList) {
        this.pendencyList = pendencyList;
    }
}

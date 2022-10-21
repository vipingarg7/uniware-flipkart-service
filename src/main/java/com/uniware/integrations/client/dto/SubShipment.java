package com.uniware.integrations.client.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SubShipment {

    @SerializedName("packages")
    private List<ModelPackage> packages = null;

    @SerializedName("subShipmentId")
    private String subShipmentId = null;

    public SubShipment packages(List<ModelPackage> packages) {
        this.packages = packages;
        return this;
    }

    public SubShipment addPackagesItem(ModelPackage packagesItem) {
        if (this.packages == null) {
            this.packages = new java.util.ArrayList<>();
        }
        this.packages.add(packagesItem);
        return this;
    }

    /**
     * Get packages
     * @return packages
     **/
    
    public List<ModelPackage> getPackages() {
        return packages;
    }

    public void setPackages(List<ModelPackage> packages) {
        this.packages = packages;
    }

    public SubShipment subShipmentId(String subShipmentId) {
        this.subShipmentId = subShipmentId;
        return this;
    }

    /**
     * Get subShipmentId
     * @return subShipmentId
     **/
    
    public String getSubShipmentId() {
        return subShipmentId;
    }

    public void setSubShipmentId(String subShipmentId) {
        this.subShipmentId = subShipmentId;
    }

    @Override public String toString() {
        return "SubShipment{" + "packages=" + packages + ", subShipmentId='" + subShipmentId + '\'' + '}';
    }
}

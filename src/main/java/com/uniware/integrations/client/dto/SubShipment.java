package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SubShipment {

    @JsonProperty("packages")
    private java.util.List<ModelPackage> packages = null;

    @JsonProperty("subShipmentId")
    private String subShipmentId = null;

    public SubShipment packages(java.util.List<ModelPackage> packages) {
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
    
    public java.util.List<ModelPackage> getPackages() {
        return packages;
    }

    public void setPackages(java.util.List<ModelPackage> packages) {
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubShipment subShipment = (SubShipment) o;
        return Objects.equals(this.packages, subShipment.packages) &&
                Objects.equals(this.subShipmentId, subShipment.subShipmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packages, subShipmentId);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SubShipment {\n");

        sb.append("    packages: ").append(toIndentedString(packages)).append("\n");
        sb.append("    subShipmentId: ").append(toIndentedString(subShipmentId)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }


}

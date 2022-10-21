package com.uniware.integrations.client.dto;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class ModelPackage {

    @SerializedName("packageSku")
    private String packageSku = null;

    @SerializedName("packageTitle")
    private String packageTitle = null;

    @SerializedName("packageId")
    private String packageId = null;

    @SerializedName("dimensions")
    private Dimensions dimensions = null;

    public ModelPackage packageSku(String packageSku) {
        this.packageSku = packageSku;
        return this;
    }

    /**
     * Get packageSku
     * @return packageSku
     **/
    
    public String getPackageSku() {
        return packageSku;
    }

    public void setPackageSku(String packageSku) {
        this.packageSku = packageSku;
    }

    public ModelPackage packageTitle(String packageTitle) {
        this.packageTitle = packageTitle;
        return this;
    }

    /**
     * Get packageTitle
     * @return packageTitle
     **/
    
    public String getPackageTitle() {
        return packageTitle;
    }

    public void setPackageTitle(String packageTitle) {
        this.packageTitle = packageTitle;
    }

    public ModelPackage packageId(String packageId) {
        this.packageId = packageId;
        return this;
    }

    /**
     * Get packageId
     * @return packageId
     **/
    
    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public ModelPackage dimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
        return this;
    }

    /**
     * Get dimensions
     * @return dimensions
     **/
    
    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    @Override public String toString() {
        return "ModelPackage{" + "packageSku='" + packageSku + '\'' + ", packageTitle='" + packageTitle + '\''
                + ", packageId='" + packageId + '\'' + ", dimensions=" + dimensions + '}';
    }

}

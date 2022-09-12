package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ModelPackage {

    @JsonProperty("packageSku")
    private String packageSku = null;

    @JsonProperty("packageTitle")
    private String packageTitle = null;

    @JsonProperty("packageId")
    private String packageId = null;

    @JsonProperty("dimensions")
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModelPackage _package = (ModelPackage) o;
        return Objects.equals(this.packageSku, _package.packageSku) &&
                Objects.equals(this.packageTitle, _package.packageTitle) &&
                Objects.equals(this.packageId, _package.packageId) &&
                Objects.equals(this.dimensions, _package.dimensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageSku, packageTitle, packageId, dimensions);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ModelPackage {\n");

        sb.append("    packageSku: ").append(toIndentedString(packageSku)).append("\n");
        sb.append("    packageTitle: ").append(toIndentedString(packageTitle)).append("\n");
        sb.append("    packageId: ").append(toIndentedString(packageId)).append("\n");
        sb.append("    dimensions: ").append(toIndentedString(dimensions)).append("\n");
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

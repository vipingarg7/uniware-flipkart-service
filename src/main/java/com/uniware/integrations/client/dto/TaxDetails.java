package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;

public class TaxDetails {

    @JsonProperty("igstRate")
    private BigDecimal igstRate = null;

    @JsonProperty("cgstRate")
    private BigDecimal cgstRate = null;

    @JsonProperty("sgstRate")
    private BigDecimal sgstRate = null;

    public TaxDetails igstRate(BigDecimal igstRate) {
        this.igstRate = igstRate;
        return this;
    }

    /**
     * Get igstRate
     * @return igstRate
     **/

    public BigDecimal getIgstRate() {
        return igstRate;
    }

    public void setIgstRate(BigDecimal igstRate) {
        this.igstRate = igstRate;
    }

    public TaxDetails cgstRate(BigDecimal cgstRate) {
        this.cgstRate = cgstRate;
        return this;
    }

    /**
     * Get cgstRate
     * @return cgstRate
     **/

    public BigDecimal getCgstRate() {
        return cgstRate;
    }

    public void setCgstRate(BigDecimal cgstRate) {
        this.cgstRate = cgstRate;
    }

    public TaxDetails sgstRate(BigDecimal sgstRate) {
        this.sgstRate = sgstRate;
        return this;
    }

    /**
     * Get sgstRate
     * @return sgstRate
     **/

    public BigDecimal getSgstRate() {
        return sgstRate;
    }

    public void setSgstRate(BigDecimal sgstRate) {
        this.sgstRate = sgstRate;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaxDetails taxDetails = (TaxDetails) o;
        return Objects.equals(this.igstRate, taxDetails.igstRate) &&
                Objects.equals(this.cgstRate, taxDetails.cgstRate) &&
                Objects.equals(this.sgstRate, taxDetails.sgstRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(igstRate, cgstRate, sgstRate);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TaxDetails {\n");

        sb.append("    igstRate: ").append(toIndentedString(igstRate)).append("\n");
        sb.append("    cgstRate: ").append(toIndentedString(cgstRate)).append("\n");
        sb.append("    sgstRate: ").append(toIndentedString(sgstRate)).append("\n");
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

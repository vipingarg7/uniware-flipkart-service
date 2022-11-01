package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.Objects;

public class DateFilter {

    @SerializedName("to")
    private String to = null;

    @SerializedName("from")
    private String from = null;

    public DateFilter to(String to) {
        this.to = to;
        return this;
    }

    /**
     * Get to
     * @return to
     **/
    
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public DateFilter from(String from) {
        this.from = from;
        return this;
    }

    /**
     * Get from
     * @return from
     **/
    
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DateFilter dateFilter = (DateFilter) o;
        return Objects.equals(this.to, dateFilter.to) &&
                Objects.equals(this.from, dateFilter.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to, from);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DateFilter {\n");

        sb.append("    to: ").append(toIndentedString(to)).append("\n");
        sb.append("    from: ").append(toIndentedString(from)).append("\n");
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

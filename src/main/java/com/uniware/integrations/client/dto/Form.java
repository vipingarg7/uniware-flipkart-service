package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class Form {

    @SerializedName("link")
    private String link = null;

    @SerializedName("name")
    private String name = null;

    @SerializedName("automated")
    private Boolean automated = null;

    public Form link(String link) {
        this.link = link;
        return this;
    }

    /**
     * Get link
     * @return link
     **/
    
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Form name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     * @return name
     **/
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Form automated(Boolean automated) {
        this.automated = automated;
        return this;
    }

    /**
     * Get automated
     * @return automated
     **/
    
    public Boolean isAutomated() {
        return automated;
    }

    public void setAutomated(Boolean automated) {
        this.automated = automated;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Form form = (Form) o;
        return Objects.equals(this.link, form.link) &&
                Objects.equals(this.name, form.name) &&
                Objects.equals(this.automated, form.automated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, name, automated);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Form {\n");

        sb.append("    link: ").append(toIndentedString(link)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    automated: ").append(toIndentedString(automated)).append("\n");
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

package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class BuyerDetails {

    @JsonProperty("lastName")
    private String lastName = null;

    @JsonProperty("firstName")
    private String firstName = null;

    public BuyerDetails lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * Get lastName
     * @return lastName
     **/
    
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BuyerDetails firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * Get firstName
     * @return firstName
     **/
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BuyerDetails buyerDetails = (BuyerDetails) o;
        return Objects.equals(this.lastName, buyerDetails.lastName) &&
                Objects.equals(this.firstName, buyerDetails.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BuyerDetails {\n");

        sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
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

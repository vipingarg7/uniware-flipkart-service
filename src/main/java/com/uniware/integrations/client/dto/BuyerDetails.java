package com.uniware.integrations.client.dto;

import com.google.gson.annotations.SerializedName;

public class BuyerDetails {

    @SerializedName("lastName")
    private String lastName = null;

    @SerializedName("firstName")
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

    @Override public String toString() {
        return "BuyerDetails{" + "lastName='" + lastName + '\'' + ", firstName='" + firstName + '\'' + '}';
    }
}

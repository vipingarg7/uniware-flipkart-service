package com.uniware.integrations.client.dto;

import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("city")
    private String city = null;

    @SerializedName("contactNumber")
    private String contactNumber = null;

    @SerializedName("firstName")
    private String firstName = null;

    @SerializedName("lastName")
    private String lastName = null;

    @SerializedName("pinCode")
    private String pinCode = null;

    @SerializedName("state")
    private String state = null;

    @SerializedName("addressLine2")
    private String addressLine2 = null;

    @SerializedName("stateCode")
    private String stateCode = null;

    @SerializedName("addressLine1")
    private String addressLine1 = null;

    @SerializedName("landmark")
    private String landmark = null;

    public Address city(String city) {
        this.city = city;
        return this;
    }

    /**
     * Get city
     * @return city
     **/
    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Address contactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }

    /**
     * Get contactNumber
     * @return contactNumber
     **/
    
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Address firstName(String firstName) {
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

    public Address lastName(String lastName) {
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

    public Address pinCode(String pinCode) {
        this.pinCode = pinCode;
        return this;
    }

    /**
     * Get pinCode
     * @return pinCode
     **/
    
    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Address state(String state) {
        this.state = state;
        return this;
    }

    /**
     * Get state
     * @return state
     **/
    
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Address addressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        return this;
    }

    /**
     * Get addressLine2
     * @return addressLine2
     **/
    
    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public Address stateCode(String stateCode) {
        this.stateCode = stateCode;
        return this;
    }

    /**
     * Get stateCode
     * @return stateCode
     **/
    
    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public Address addressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        return this;
    }

    /**
     * Get addressLine1
     * @return addressLine1
     **/
    
    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public Address landmark(String landmark) {
        this.landmark = landmark;
        return this;
    }

    /**
     * Get landmark
     * @return landmark
     **/
    
    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    @Override public String toString() {
        return "Address{" + "city='" + city + '\'' + ", contactNumber='" + contactNumber + '\'' + ", firstName='"
                + firstName + '\'' + ", lastName='" + lastName + '\'' + ", pinCode='" + pinCode + '\'' + ", state='"
                + state + '\'' + ", addressLine2='" + addressLine2 + '\'' + ", stateCode='" + stateCode + '\''
                + ", addressLine1='" + addressLine1 + '\'' + ", landmark='" + landmark + '\'' + '}';
    }
}

package com.uniware.integrations.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class Address {

    @JsonProperty("city")
    private String city = null;

    @JsonProperty("contactNumber")
    private String contactNumber = null;

    @JsonProperty("firstName")
    private String firstName = null;

    @JsonProperty("lastName")
    private String lastName = null;

    @JsonProperty("pinCode")
    private String pinCode = null;

    @JsonProperty("state")
    private String state = null;

    @JsonProperty("addressLine2")
    private String addressLine2 = null;

    @JsonProperty("stateCode")
    private String stateCode = null;

    @JsonProperty("addressLine1")
    private String addressLine1 = null;

    @JsonProperty("landmark")
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(this.city, address.city) &&
                Objects.equals(this.contactNumber, address.contactNumber) &&
                Objects.equals(this.firstName, address.firstName) &&
                Objects.equals(this.lastName, address.lastName) &&
                Objects.equals(this.pinCode, address.pinCode) &&
                Objects.equals(this.state, address.state) &&
                Objects.equals(this.addressLine2, address.addressLine2) &&
                Objects.equals(this.stateCode, address.stateCode) &&
                Objects.equals(this.addressLine1, address.addressLine1) &&
                Objects.equals(this.landmark, address.landmark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, contactNumber, firstName, lastName, pinCode, state, addressLine2, stateCode, addressLine1, landmark);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Address {\n");

        sb.append("    city: ").append(toIndentedString(city)).append("\n");
        sb.append("    contactNumber: ").append(toIndentedString(contactNumber)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
        sb.append("    pinCode: ").append(toIndentedString(pinCode)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
        sb.append("    addressLine2: ").append(toIndentedString(addressLine2)).append("\n");
        sb.append("    stateCode: ").append(toIndentedString(stateCode)).append("\n");
        sb.append("    addressLine1: ").append(toIndentedString(addressLine1)).append("\n");
        sb.append("    landmark: ").append(toIndentedString(landmark)).append("\n");
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

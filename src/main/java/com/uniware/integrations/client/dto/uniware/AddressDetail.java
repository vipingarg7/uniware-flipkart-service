package com.uniware.integrations.client.dto.uniware;

import com.unifier.core.annotation.Encrypt;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class AddressDetail {

    @NotBlank
    private String id;

    @NotBlank
    @Length(max = 100)
    @Encrypt
    private String name;

    @NotBlank
    @Length(max = 500)
    @Encrypt
    private String addressLine1;

    @Length(max = 500)
    private String addressLine2;

    @NotBlank
    @Length(max = 100)
    private String city;

    @Length(max = 100)
    private String district;

    @NotBlank
    @Length(max = 45)
    private String state;

    private String country ;

    @Length(max = 45)
    private String pincode;

    @NotBlank
    @Length(max = 50)
    @Encrypt
    private String phone;

    @Length(max = 100)
    @Encrypt
    private String email;

    @Length(max = 10)
    private String type;

    public AddressDetail() {

    }

    /**
     * @param address
     */
    public AddressDetail(AddressDetail address) {
        this.setId(String.valueOf(address.getId()));
        this.name = address.getName();
        this.addressLine1 = address.getAddressLine1();
        this.addressLine2 = address.getAddressLine2();
        this.city = address.getCity();
        this.district = address.getDistrict();
        this.state = address.getState();
        this.country = address.getCountry();
        this.pincode = address.getPincode();
        this.phone = address.getPhone();
        this.email = address.getEmail();
        this.type = address.getType();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the addressLine1
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * @param addressLine1 the addressLine1 to set
     */
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    /**
     * @return the addressLine2
     */
    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * @param addressLine2 the addressLine2 to set
     */
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the pincode
     */
    public String getPincode() {
        return pincode;
    }

    /**
     * @param pincode the pincode to set
     */
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the addressType
     */
    public String getType() { return this.type; }

    /**
     * @param type the addressType to set
     */
    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "AddressDetail{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", addressLine1='" + addressLine1 + '\'' + ", addressLine2='" + addressLine2 + '\'' + ", city='"
                + city + '\'' + ", state='" + state + '\'' + ", country='" + country + '\'' + ", pincode='" + pincode + '\'' + ", phone='" + phone + '\'' + ", email='" + email
                + '\'' + '}';
    }
    
}

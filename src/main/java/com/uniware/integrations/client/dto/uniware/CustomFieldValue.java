package com.uniware.integrations.client.dto.uniware;

import org.hibernate.validator.constraints.NotBlank;

public class CustomFieldValue {

    @NotBlank
    private String name;

    private String value;

    public CustomFieldValue() {

    }

    /*
     * @param fieldName
     * @param fieldValue
     */
    public CustomFieldValue(String name, String value) {
        this.name = name;
        this.value = value;
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
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CustomFieldValue{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
    
}

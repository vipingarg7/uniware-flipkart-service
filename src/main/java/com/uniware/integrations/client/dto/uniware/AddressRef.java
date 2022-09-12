package com.uniware.integrations.client.dto.uniware;

import org.hibernate.validator.constraints.NotBlank;

public class AddressRef {

    @NotBlank
    private String referenceId;

    /**
     *
     */
    public AddressRef() {
    }

    public AddressRef(String referenceId) {
        this.referenceId = referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    @Override
    public String toString() {
        return "AddressRef{" +
                "referenceId='" + referenceId + '\'' +
                '}';
    }
    
}

package org.egov.search.domain.entities;

import org.egov.search.domain.Searchable;

@Searchable
public class Address {
    @Searchable
    private String street;
    @Searchable
    private String pincode;

    public Address(String street, String pincode) {
        this.street = street;
        this.pincode = pincode;
    }

    public String getStreet() {
        return street;
    }

    public String getPincode() {
        return pincode;
    }
}

package org.egov.search.service;

import org.egov.search.domain.Searchable;

public class Department {

    @Searchable
    private String name;

    @Searchable
    private String code;

    @Searchable(nested = true)
    private Address address;

    private long id;

    public Department(long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public long getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

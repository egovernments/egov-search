package org.egov.search.domain.entities;

import org.egov.search.domain.Searchable;
@Searchable
public class Bike implements Vehicle {
    @Searchable
    private String name;

    @Searchable
    private String manufacturer;

    public Bike(String name) {
        this.name = name;
        this.manufacturer = "enfield";
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }
}

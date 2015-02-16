package org.egov.search.domain.entities;

import org.egov.search.domain.Searchable;

@Searchable
public class Car implements Vehicle {
    @Searchable
    private String name;

    public Car(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

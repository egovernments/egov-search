package org.egov.search.domain;

import java.util.HashMap;
import java.util.Map;

public class Filters {
    private Map<String, String> andFilters;
    private Map<String, String> orFilters;
    private Map<String, String> notInFilters;

    public Filters(Map<String, String> andFilters, Map<String, String> orFilters, Map<String, String> notInFilters) {
        this.andFilters = andFilters;
        this.orFilters = orFilters;
        this.notInFilters = notInFilters;
    }

    public Map<String, String> getAndFilters() {
        return andFilters;
    }

    public Map<String, String> getOrFilters() {
        return orFilters;
    }

    public Map<String, String> getNotInFilters() {
        return notInFilters;
    }

    public static Filters withAndFilters(Map<String, String> andFilters) {
        return new Filters(andFilters, new HashMap<>(), new HashMap<>());
    }
}

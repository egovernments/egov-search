package org.egov.search.domain;

import java.util.HashMap;
import java.util.Map;

public class Filters {
    public static final Filters NULL = new Filters();

    private Map<String, String> andFilters;
    private Map<String, String> orFilters;
    private Map<String, String> notInFilters;

    private Filters() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

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
        return withAndPlusOrFilters(andFilters, new HashMap<>());
    }

    public static Filters withOrFilters(Map<String, String> orFilters) {
        return withAndPlusOrFilters(new HashMap<>(), orFilters);
    }

    public static Filters withAndPlusOrFilters(Map<String, String> andFilters, Map<String, String> orFilters) {
        return new Filters(andFilters, orFilters, new HashMap<>());
    }

    public static Filters withAndPlusNotFilters(Map<String, String> andFilters, Map<String, String> notFilters) {
        return new Filters(andFilters, new HashMap<>(), notFilters);
    }
    
    public static Filters withOrPlusNotFilters(Map<String, String> orFilters, Map<String, String> notFilters){
    	return new Filters(new HashMap<>(), orFilters, notFilters);
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return andFilters.isEmpty() && orFilters.isEmpty() && notInFilters.isEmpty();
    }
}

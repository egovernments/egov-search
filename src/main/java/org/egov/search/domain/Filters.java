package org.egov.search.domain;

import java.util.List;

import static java.util.Arrays.asList;

public class Filters {
    public static final Filters NULL = new Filters();

    private List<Filter> andFilters;
    private List<Filter> orFilters;
    private List<Filter> notInFilters;

    private Filters() {
        this(asList(), asList(), asList());
    }

    private Filters(List<Filter> andFilters, List<Filter> orFilters, List<Filter> notInFilters) {
        this.andFilters = andFilters;
        this.orFilters = orFilters;
        this.notInFilters = notInFilters;
    }

    public List<Filter> getAndFilters() {
        return andFilters;
    }

    public List<Filter> getOrFilters() {
        return orFilters;
    }

    public List<Filter> getNotInFilters() {
        return notInFilters;
    }

    public static Filters withAndFilters(List<Filter> andFilters) {
        return withAndPlusOrFilters(andFilters, asList());
    }

    public static Filters withOrFilters(List<Filter> orFilters) {
        return withAndPlusOrFilters(asList(), orFilters);
    }

    public static Filters withAndPlusOrFilters(List<Filter> andFilters, List<Filter> orFilters) {
        return new Filters(andFilters, orFilters, asList());
    }

    public static Filters withAndPlusNotFilters(List<Filter> andFilters, List<Filter> notFilters) {
        return new Filters(andFilters, asList(), notFilters);
    }

    public static Filters withOrPlusNotFilters(List<Filter> orFilters, List<Filter> notFilters) {
        return new Filters(asList(), orFilters, notFilters);
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return andFilters.isEmpty() && orFilters.isEmpty() && notInFilters.isEmpty();
    }
}

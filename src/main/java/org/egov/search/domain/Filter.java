package org.egov.search.domain;

public abstract class Filter {
    protected String fieldName;

    public Filter(String fieldName) {
        this.fieldName = fieldName;
    }

    public static Filter matchFilter(String fieldName, String value) {
        return new MatchFilter(fieldName, value);
    }

    public String field() {
        return fieldName;
    }

    public static Filter rangeFilter(String fieldName, String from, String to) {
        return new RangeFilter(fieldName, from, to);
    }
}

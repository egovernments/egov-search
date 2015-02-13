package org.egov.search.domain;

public class RangeFilter extends Filter {
    private String from;
    private String to;

    RangeFilter(String fieldName, String from, String to) {
        super(fieldName);
        this.from = from;
        this.to = to;
    }

    public String from() {
        return from;
    }

    public String to() {
        return to;
    }
}

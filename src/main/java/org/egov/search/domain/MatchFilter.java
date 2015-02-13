package org.egov.search.domain;

public class MatchFilter extends Filter {

    private String value;

    public MatchFilter(String fieldName, String value) {
        super(fieldName);
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}

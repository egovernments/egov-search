package org.egov.search.domain;

public class Range {

    private String name;
    private String from;

    private Range(String name) {
        this.name = name;
    }

    public static Range forField(String name) {
        return new Range(name);
    }

    public Range from(String from) {
        this.from = from;
        return this;
    }

    public String field() {
        return this.name ;
    }

    public String from() {
        return this.from;
    }
}

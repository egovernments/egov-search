package org.egov.search.domain;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;

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

    @Override
    public FilterBuilder filterBuilder() {
        return FilterBuilders.rangeFilter(this.field()).from(this.from()).to(this.to());
    }
}

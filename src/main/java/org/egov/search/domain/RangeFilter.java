package org.egov.search.domain;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

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
    public QueryBuilder queryBuilder() {
        return QueryBuilders.rangeQuery(this.field()).from(this.from()).to(this.to());

    }
}

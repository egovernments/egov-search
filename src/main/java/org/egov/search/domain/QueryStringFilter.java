package org.egov.search.domain;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;

public class QueryStringFilter extends Filter {

    private String value;

    QueryStringFilter(String fieldName, String value) {
        super(fieldName);
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    @Override
    public FilterBuilder filterBuilder() {
        return FilterBuilders.queryFilter(QueryBuilders.queryString(this.value()).field(this.field()));
    }
}

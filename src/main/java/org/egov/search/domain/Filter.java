package org.egov.search.domain;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public abstract class Filter {
    protected String fieldName;

    public Filter(String fieldName) {
        this.fieldName = fieldName;
    }

    public static Filter queryStringFilter(String fieldName, String value) {
        return new QueryStringFilter(fieldName, value);
    }

    public String field() {
        return fieldName;
    }

    public static Filter rangeFilter(String fieldName, String from, String to) {
        return new RangeFilter(fieldName, from, to);
    }

    public static Filter rangeFilterFrom(String fieldName, String from) {
        return rangeFilter(fieldName, from, null);
    }

    public static Filter rangeFilterTo(String fieldName, String to) {
        return rangeFilter(fieldName, null, to);
    }

    public QueryBuilder queryBuilder() {
        return QueryBuilders.matchAllQuery();
    }
}

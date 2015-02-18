package org.egov.search.domain;

import org.elasticsearch.index.query.QueryBuilder;

public class NoOpFilter extends Filter {

    NoOpFilter() {
        super(null);
    }

    @Override
    public QueryBuilder queryBuilder() {
        return null;
    }

}

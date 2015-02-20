package org.egov.search.domain;


import org.elasticsearch.index.query.FilterBuilder;

public class NoOpFilter extends Filter {

    NoOpFilter() {
        super(null);
    }

    @Override
    public FilterBuilder filterBuilder() {
        return null;
    }

}

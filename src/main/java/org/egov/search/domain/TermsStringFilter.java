package org.egov.search.domain;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class TermsStringFilter extends Filter {

    private String[] values;

    TermsStringFilter(String fieldName, String... values) {
        super(fieldName);
        this.values = values;
    }

    public String[] values() {
        return this.values;
    }

    @Override
    public QueryBuilder queryBuilder() {
        return QueryBuilders.termsQuery(this.field(), this.values());
    }
}

package org.egov.search.domain;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;

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
    public FilterBuilder filterBuilder() {
        return FilterBuilders.termsFilter(this.field(), this.values());
    }
}

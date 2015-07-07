package org.egov.search.domain;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;

public class TermsStringFilter extends Filter {

    private String value;

    TermsStringFilter(String fieldName, String value) {
        super(fieldName);
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    @Override
    public FilterBuilder filterBuilder() {
        return FilterBuilders.termsFilter(this.field(), this.value());
    }
}

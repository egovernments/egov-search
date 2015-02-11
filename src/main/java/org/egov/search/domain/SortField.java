package org.egov.search.domain;

import org.elasticsearch.search.sort.SortOrder;

public class SortField {
    private final String field;
    private final SortOrder order;

    public SortField(String field, SortOrder order) {
        this.field = field;
        this.order = order;
    }

    public String field() {
        return field;
    }

    public SortOrder order() {
        return order;
    }
}

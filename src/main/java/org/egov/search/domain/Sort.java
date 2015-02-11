package org.egov.search.domain;

import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Sort {

    public static final Sort NULL = new Sort();

    List<SortField> sortFields = new ArrayList<>();

    public static Sort by() {
        return new Sort();
    }

    public Sort field(String field, SortOrder order) {
        SortField sortField = new SortField(field, order);
        sortFields.add(sortField);
        return this;
    }

    public Stream<SortField> stream() {
        return Stream.of(sortFields.toArray(new SortField[sortFields.size()]));
    }



}

package org.egov.search.domain;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FiltersTest {

    @Test
    public void shouldCheckIfEmpty() {
        Filters empty = Filters.NULL;

        assertFalse(empty.isNotEmpty());
        assertTrue(empty.isEmpty());
    }

    @Test
    public void shouldCheckIfNotEmpty() {
        List<Filter> filters = asList(Filter.matchFilter("key", "value"));
        assertTrue(Filters.withAndFilters(filters).isNotEmpty());
        assertTrue(Filters.withOrFilters(filters).isNotEmpty());
    }

}
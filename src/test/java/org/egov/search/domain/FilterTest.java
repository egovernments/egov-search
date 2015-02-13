package org.egov.search.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FilterTest {

    @Test
    public void shouldConstructMatchFilter() {
        Filter filter = Filter.queryStringFilter("fieldName", "value");

        assertThat(filter, instanceOf(QueryStringFilter.class));
        assertThat(filter.field(), is("fieldName"));
        assertThat(((QueryStringFilter) filter).value(), is("value"));
    }

    @Test
    public void shouldConstructRangeFilter() {
        Filter filter = Filter.rangeFilter("fieldName", "from", "to");

        assertThat(filter, instanceOf(RangeFilter.class));
        assertThat(filter.field(), is("fieldName"));

        assertThat(((RangeFilter) filter).from(), is("from"));
        assertThat(((RangeFilter) filter).to(), is("to"));
    }
}
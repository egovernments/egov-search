package org.egov.search.domain;

import org.hamcrest.core.Is;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public class QueryStringFilterTest {

    @Test
    public void shouldConstructQueryStringFilter() {
        Filter filter = Filter.queryStringFilter("field", "value");

        assertThat(filter, instanceOf(QueryStringFilter.class));
        assertThat(filter.field(), Is.is("field"));
        assertThat(((QueryStringFilter) filter).value(), Is.is("value"));
    }

    @Test
    public void shouldConstructEmptyFilterIfValueIsNullOrEmpty() {
        Filter filter = Filter.queryStringFilter("field", "");
        assertThat(filter, instanceOf(NoOpFilter.class));

        filter = Filter.queryStringFilter("field", null);
        assertThat(filter, instanceOf(NoOpFilter.class));

        filter = Filter.queryStringFilter(null, "value");
        assertThat(filter, instanceOf(NoOpFilter.class));

        filter = Filter.queryStringFilter("", "value");
        assertThat(filter, instanceOf(NoOpFilter.class));
    }

}
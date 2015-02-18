package org.egov.search.domain;

import org.hamcrest.core.Is;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public class RangeFilterTest {

	@Test
	public void shouldConstructRangeFilter() {
		Filter filter = Filter.rangeFilter("field", "from", "to");
		assertThat(filter, instanceOf(RangeFilter.class));
		assertThat(filter.field(), Is.is("field"));
		assertThat(((RangeFilter) filter).from(), Is.is("from"));
		assertThat(((RangeFilter) filter).to(), Is.is("to"));
	}

	@Test
	public void shouldConstructRangeFilterFrom() {
		Filter filter = Filter.rangeFilterFrom("field", "from");
		assertThat(filter, instanceOf(RangeFilter.class));
		assertThat(filter.field(), Is.is("field"));
		assertThat(((RangeFilter) filter).from(), Is.is("from"));
	}

	@Test
	public void shouldConstructRangeFilterTo() {
		Filter filter = Filter.rangeFilterFrom("field", "to");
		assertThat(filter, instanceOf(RangeFilter.class));
		assertThat(filter.field(), Is.is("field"));
		assertThat(((RangeFilter) filter).from(), Is.is("to"));
	}

	@Test
	public void shouldConstructEmptyFilterIfValueIsNullOrEmpty() {
		Filter filter = Filter.rangeFilter("field", "", "");
		assertThat(filter, instanceOf(NoOpFilter.class));

		filter = Filter.rangeFilter("field", null, null);
		assertThat(filter, instanceOf(NoOpFilter.class));

		filter = Filter.rangeFilter(null, "from", "to");
		assertThat(filter, instanceOf(NoOpFilter.class));

		filter = Filter.rangeFilter("", "from", "to");
		assertThat(filter, instanceOf(NoOpFilter.class));
	}

	@Test
	public void shouldConstructEmptyFilterIfFieldOrFromNullOrEmpty() {
		Filter filter = Filter.rangeFilterFrom("field", "");
		assertThat(filter, instanceOf(NoOpFilter.class));

		filter = Filter.rangeFilterFrom("field", null);
		assertThat(filter, instanceOf(NoOpFilter.class));

		filter = Filter.rangeFilterFrom(null, "from");
		assertThat(filter, instanceOf(NoOpFilter.class));

	}

	@Test
	public void shouldConstructEmptyFilterIfFieldOrToNullOrEmpty() {
		Filter filter = Filter.rangeFilterTo("field", "");
		assertThat(filter, instanceOf(NoOpFilter.class));

		filter = Filter.rangeFilterTo("field", null);
		assertThat(filter, instanceOf(NoOpFilter.class));

		filter = Filter.rangeFilterTo(null, "from");
		assertThat(filter, instanceOf(NoOpFilter.class));

	}

	@Test
	public void shouldConstructFilterIfFromValueIsNullOrEmpty() {
		Filter filter = Filter.rangeFilter("field", "", "to");
		assertThat(((RangeFilter) filter).to(), Is.is("to"));

	}

	@Test
	public void shouldConstructFilterIfToValueIsNullOrEmpty() {
		Filter filter = Filter.rangeFilter("field", "from", "");
		assertThat(((RangeFilter) filter).from(), Is.is("from"));

	}

}
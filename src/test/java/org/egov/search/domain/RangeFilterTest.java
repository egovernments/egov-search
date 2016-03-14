/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

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
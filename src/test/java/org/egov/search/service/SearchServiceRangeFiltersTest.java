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

package org.egov.search.service;

import org.egov.search.domain.Filter;
import org.egov.search.domain.Filters;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.hamcrest.core.Is;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.egov.search.domain.Filter.queryStringFilter;
import static org.egov.search.domain.Filter.rangeFilter;
import static org.egov.search.domain.Filter.rangeFilterFrom;
import static org.egov.search.domain.Filter.rangeFilterTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class SearchServiceRangeFiltersTest extends SearchServiceTest {

    @Test
    public void shouldFilterByMatchAndDateRangeFrom() {
        List<Filter> andFilters = asList(
                queryStringFilter("clauses.status", "REGISTERED"),
                rangeFilterFrom("common.created_date", "28-Jan-15")
        );
        Filters filters = Filters.withAndFilters(andFilters);

        SearchResult searchResult = searchService.search(asList(indexName), asList(indexType), "", filters, Sort.NULL, Page.NULL);

        assertThat(searchResult.documentCount(), Is.is(6));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("810FBE", "820LGN", "225OEJ", "210BIM", "396LLE", "994JFK"));
    }

    @Test
    public void shouldFilterByMatchAndDateRangeTo() {
        List<Filter> andFilters = asList(
                queryStringFilter("clauses.status", "REGISTERED"),
                rangeFilterTo("common.created_date", "09-Jan-15")
        );
        Filters filters = Filters.withAndFilters(andFilters);

        SearchResult searchResult = searchService.search(asList(indexName), asList(indexType), "", filters, Sort.NULL, Page.NULL);

        assertThat(searchResult.documentCount(), Is.is(3));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("751HFP", "696IDN", "892JBP"));
    }

    @Test
    public void shouldFilterByMatchAndDateRangeWithFromAndTo() {
        List<Filter> andFilters = asList(
                queryStringFilter("clauses.status", "REGISTERED"),
                rangeFilter("common.created_date", "05-Jan-15", "09-Jan-15")
        );
        Filters filters = Filters.withAndFilters(andFilters);
        SearchResult searchResult = searchService.search(asList(indexName), asList(indexType), "", filters, Sort.NULL, Page.NULL);

        assertThat(searchResult.documentCount(), Is.is(3));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("751HFP", "696IDN", "892JBP"));
    }

    @Test
    public void shouldFilterByAmountRange() {
        indexWorksPackageData();

        List<Filter> andFilters = asList(
                rangeFilter("clauses.package_amount", "500000", "1100000")
        );

        Filters filters = Filters.withAndFilters(andFilters);
        SearchResult searchResult = searchService.search(asList(indexName), asList(indexType), "", filters, Sort.NULL, Page.NULL);

        assertThat(searchResult.documentCount(), Is.is(3));
        assertThat(packageNumbers(searchResult), containsInAnyOrder("B/WP/6013/2014-15", "NC/WP/5852/2014-15", "NC/WP/3651/2014-15"));
    }

    @Test
    public void shouldFilterByAmountGreaterThan() {
        indexWorksPackageData();

        List<Filter> andFilters = asList(
                rangeFilterFrom("clauses.package_amount", "1000000")
        );

        Filters filters = Filters.withAndFilters(andFilters);
        SearchResult searchResult = searchService.search(asList(indexName), asList(indexType), "", filters, Sort.NULL, Page.NULL);

        assertThat(searchResult.documentCount(), Is.is(2));
        assertThat(packageNumbers(searchResult), containsInAnyOrder("NC/WP/3651/2014-15", "RS/WP/2645/2014-15"));
    }

    @Test
    public void shouldFilterByAmountLesserThan() {
        indexWorksPackageData();

        List<Filter> andFilters = asList(
                rangeFilterTo("clauses.package_amount", "1000000")
        );

        Filters filters = Filters.withAndFilters(andFilters);
        SearchResult searchResult = searchService.search(asList(indexName), asList(indexType), "", filters, Sort.NULL, Page.NULL);

        assertThat(searchResult.documentCount(), Is.is(3));
        assertThat(packageNumbers(searchResult), containsInAnyOrder("NC/WP/5852/2014-15", "NC/WP/1955/2014-15", "B/WP/6013/2014-15"));
    }
}

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
import static org.egov.search.domain.Filter.*;
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
        assertThat(packageNumbers(searchResult), containsInAnyOrder("NC/WP/5852/2014-15", "NC/WP/1955/2014-15","B/WP/6013/2014-15"));
    }
}

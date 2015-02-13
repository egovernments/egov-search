package org.egov.search.service;

import org.egov.search.domain.Filter;
import org.egov.search.domain.Filters;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.egov.search.domain.Filter.queryStringFilter;
import static org.egov.search.domain.Filters.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SearchServiceMultipleFiltersTest extends SearchServiceTest {

    @Test
    public void shouldSearchWithEmptyFilters() {
        SearchResult searchResult = searchService.search(asList(indexName), asList(), Filters.NULL, Sort.NULL, Page.NULL);

        assertThat(searchResult.documentCount(), is(11));
    }

    @Test
    public void shouldSearchWithSingleFilter() {
        List<Filter> andFilters = asList(queryStringFilter("clauses.mode", "INTERNET"));

        SearchResult searchResult = searchService.search(asList(indexName), asList(), withAndFilters(andFilters), Sort.NULL, Page.NULL);

        assertThat(searchResult.documentCount(), is(3));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("299DIF", "751HFP", "696IDN"));
    }

    @Test
    public void shouldSearchWithMultipleFilters() {
        List<Filter> andFilters = asList(queryStringFilter("clauses.mode", "INTERNET"),
                queryStringFilter("clauses.status", "REGISTERED"));

        SearchResult searchResult = searchService.search(asList(indexName), asList(), withAndFilters(andFilters), Sort.NULL, Page.NULL);

        assertThat(searchResult.documentCount(), is(2));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("751HFP", "696IDN"));
    }

    @Test
    public void shouldSearchWithMultipleFiltersIncludingPartialMatch() {
        List<Filter> andFilters = asList(queryStringFilter("clauses.status", "REGISTERED"),
                queryStringFilter("common.citizen.address", "jakkasandra"));

        SearchResult searchResult = searchService.search(asList(indexName), asList(), withAndFilters(andFilters), Sort.NULL, Page.NULL);
        assertThat(searchResult.documentCount(), is(2));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("810FBE", "892JBP"));
    }

    @Test
    public void shouldSearchWithAndPlusOrFilters() {
        List<Filter> andFilters = asList(queryStringFilter("clauses.status", "REGISTERED"));
        List<Filter> orFilters = asList(queryStringFilter("searchable.title", "mosquito OR garbage"));

        SearchResult searchResult = searchService.search(asList(indexName), asList(), withAndPlusOrFilters(andFilters, orFilters), Sort.NULL, Page.NULL);
        assertThat(searchResult.documentCount(), is(3));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("810FBE", "820LGN", "751HFP"));
    }

    @Test
    public void shouldSearchWithOrFiltersOnDifferentFields() {
        List<Filter> orFilters = asList(queryStringFilter("clauses.status", "COMPLETED"),
                queryStringFilter("clauses.mode", "INTERNET"));

        SearchResult searchResult = searchService.search(asList(indexName), asList(), withOrFilters(orFilters), Sort.NULL, Page.NULL);
        assertThat(searchResult.documentCount(), is(4));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("299DIF", "751HFP", "696IDN", "873GBH"));
    }

    @Test
    public void shouldSearchWithOrFiltersOnSameField() {
        List<Filter> andFilters = asList(queryStringFilter("clauses.status", "FORWARDED OR COMPLETED"));

        SearchResult searchResult = searchService.search(asList(indexName), asList(), withAndFilters(andFilters), Sort.NULL, Page.NULL);
        assertThat(searchResult.documentCount(), is(2));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("299DIF", "873GBH"));
    }

    @Test
    public void shouldSearchWithAndPlusNotInFilter() {
        List<Filter> andFilters = asList(queryStringFilter("clauses.status", "REGISTERED"));
        List<Filter> notInFilters = asList(queryStringFilter("clauses.mode", "CITIZEN"));

        SearchResult searchResult = searchService.search(asList(indexName), asList(), withAndPlusNotFilters(andFilters, notInFilters), Sort.NULL, Page.NULL);
        assertThat(searchResult.documentCount(), is(2));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("751HFP", "696IDN"));
    }

    @Test
    public void shouldSearchWithMultipleOrAndNotInFilter() {
        List<Filter> orFilters = asList(queryStringFilter("clauses.status", "COMPLETED"),
                queryStringFilter("clauses.mode", "INTERNET"));
        List<Filter> notInFilters = asList(queryStringFilter("searchable.title", "mosquito"));

        SearchResult searchResult = searchService.search(asList(indexName), asList(indexType), Filters.withOrPlusNotFilters(orFilters, notInFilters), Sort.NULL, Page.NULL);
        assertThat(searchResult.documentCount(), is(2));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("873GBH", "696IDN"));
    }

    @Test
    public void shouldSearchWithMultipleOrFilter() {
        List<Filter> orFilters = asList(queryStringFilter("common.boundary.zone", "N09 or N12"),
                queryStringFilter("common.created_by.department", "H-HEALTH"));

        SearchResult searchResult = searchService.search(asList(indexName), asList(indexType), withOrFilters(orFilters), Sort.NULL, Page.NULL);
        assertThat(searchResult.documentCount(), is(5));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("299DIF", "810FBE", "210BIM", "696IDN", "892JBP"));
    }

}

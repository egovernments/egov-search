package org.egov.search.service;

import org.egov.search.domain.Filters;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.egov.search.domain.Filters.withAndFilters;
import static org.egov.search.domain.Filters.withAndPlusNotFilters;
import static org.egov.search.domain.Filters.withAndPlusOrFilters;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SearchServiceMultipleFiltersTest extends SearchServiceTest {

    private Sort sort = Sort.NULL;

    @Test
    public void shouldSearchWithEmptyFilters() {
        SearchResult searchResult = searchService.search(asList(indexName), asList(), Filters.NULL, Sort.NULL);

        assertThat(searchResult.documentCount(), is(11));
    }

    @Test
    public void shouldSearchWithSingleFilter() {
        Map<String, String> andFilters = new HashMap<>();
        andFilters.put("clauses.mode", "INTERNET");

        SearchResult searchResult = searchService.search(asList(indexName), asList(), Filters.withAndFilters(andFilters), sort);

        assertThat(searchResult.documentCount(), is(3));
        assertThat(complaintNumbers(searchResult), contains("299DIF", "751HFP", "696IDN"));
    }

    @Test
    public void shouldSearchWithMultipleFilters() {
        Map<String, String> andFilters = new HashMap<>();
        andFilters.put("clauses.mode", "INTERNET");
        andFilters.put("clauses.status", "REGISTERED");

        SearchResult searchResult = searchService.search(asList(indexName), asList(), Filters.withAndFilters(andFilters), sort);

        assertThat(searchResult.documentCount(), is(2));
        assertThat(complaintNumbers(searchResult), contains("751HFP", "696IDN"));
    }

    @Test
    public void shouldSearchWithMultipleFiltersIncludingPartialMatch() {
        Map<String, String> andFilters = new HashMap<>();
        andFilters.put("clauses.status", "REGISTERED");
        andFilters.put("common.citizen.address", "jakkasandra");

        SearchResult searchResult = searchService.search(asList(indexName), asList(), Filters.withAndFilters(andFilters), sort);
        assertThat(searchResult.documentCount(), is(2));
        assertThat(complaintNumbers(searchResult), contains("810FBE", "892JBP"));
    }

    @Test
    public void shouldSearchWithAndPlusOrFilters() {
        Map<String, String> andFilters = new HashMap<>();
        andFilters.put("clauses.status", "REGISTERED");

        Map<String, String> orFilters = new HashMap<>();
        orFilters.put("searchable.title", "mosquito OR garbage");

        SearchResult searchResult = searchService.search(asList(indexName), asList(), withAndPlusOrFilters(andFilters, orFilters), sort);
        assertThat(searchResult.documentCount(), is(3));
        assertThat(complaintNumbers(searchResult), contains("810FBE","820LGN", "751HFP"));
    }

    @Test
    public void shouldSearchWithOrFiltersOnSameField() {
        Map<String, String> andFilters = new HashMap<>();
        andFilters.put("clauses.status", "FORWARDED OR COMPLETED");

        SearchResult searchResult = searchService.search(asList(indexName), asList(), withAndFilters(andFilters), sort);
        assertThat(searchResult.documentCount(), is(2));
        assertThat(complaintNumbers(searchResult), contains("299DIF","873GBH"));
    }

    @Test
    public void shouldSearchWithAndPlusNotInFilter() {
        Map<String, String> andFilters = new HashMap<>();
        andFilters.put("clauses.status", "REGISTERED");

        Map<String, String> notInFilters = new HashMap<>();
        notInFilters.put("clauses.mode", "CITIZEN");

        SearchResult searchResult = searchService.search(asList(indexName), asList(), withAndPlusNotFilters(andFilters, notInFilters), sort);
        assertThat(searchResult.documentCount(), is(2));
        assertThat(complaintNumbers(searchResult), contains("751HFP","696IDN"));
    }

}

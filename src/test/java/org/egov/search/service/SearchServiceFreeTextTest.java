package org.egov.search.service;

import org.egov.search.domain.Filters;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.egov.search.domain.Filter.queryStringFilter;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SearchServiceFreeTextTest extends SearchServiceTest {

    @Test
    public void shouldSearchForFreeText() {
        SearchResult searchResult = searchService.search(asList(indexName), asList(), "mosquito", Filters.NULL, Sort.NULL, Page.NULL);

        assertThat(searchResult.documentCount(), is(3));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("299DIF", "820LGN", "751HFP"));
    }

    @Test
    public void shouldSearchForFreeTextWithFilters() {
        Filters filters = Filters.withAndFilters(asList(queryStringFilter("clauses.status", "FORWARDED")));
        SearchResult searchResult = searchService.search(asList(indexName), asList(), "mosquito", filters, Sort.NULL, Page.NULL);

        assertThat(searchResult.documentCount(), is(1));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("299DIF"));
    }
    
    

}

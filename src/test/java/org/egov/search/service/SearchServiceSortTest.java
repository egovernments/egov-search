package org.egov.search.service;

import org.egov.search.domain.Filters;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.elasticsearch.search.sort.SortOrder;
import org.hamcrest.core.Is;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;

public class SearchServiceSortTest extends SearchServiceTest {
    
    @Test
    public void shouldSortByComplaintNumber() {
        Sort sort = Sort.by().field("searchable.complaint_number", SortOrder.ASC);

        SearchResult searchResult = searchService.search(asList(indexName), asList(), Filters.NULL, sort);

        assertThat(searchResult.documentCount(), Is.is(11));
        assertThat(searchResult.getDocuments().get(0).getCorrelationId(), Is.is("203465"));
        assertThat(searchResult.getDocuments().get(1).getCorrelationId(), Is.is("203464"));
        assertThat(searchResult.getDocuments().get(10).getCorrelationId(), Is.is("203467"));
    }

    @Test
    public void shouldSortByMultipleFields() {
        Sort sort = Sort.by()
                .field("searchable.title", SortOrder.ASC)
                .field("searchable.complaint_number", SortOrder.DESC);

        SearchResult searchResult = searchService.search(asList(indexName), asList(), Filters.NULL, sort);

        assertThat(searchResult.documentCount(), Is.is(11));
        assertThat(searchResult.getDocuments().get(8).getCorrelationId(), Is.is("203463"));
        assertThat(searchResult.getDocuments().get(9).getCorrelationId(), Is.is("203468"));
        assertThat(searchResult.getDocuments().get(10).getCorrelationId(), Is.is("203461"));

    }
}

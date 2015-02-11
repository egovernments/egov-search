package org.egov.search.service;

import org.egov.search.domain.Filters;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SearchServicePaginationTest extends SearchServiceTest {

    @Test
    public void shouldSearchByNullPage() {
        SearchResult searchResult = searchService.search(asList(indexName), asList(), Filters.NULL, Sort.NULL, Page.NULL);

        assertThat(searchResult.documentCount(), is(11));
    }

    @Test
    public void shouldSearchByGivenPageSize() {
        Page page = Page.at(1).ofSize(3);
        SearchResult searchResult = searchService.search(asList(indexName), asList(), Filters.NULL, Sort.NULL, page);

        assertThat(searchResult.documentCount(), is(3));
        assertThat(complaintNumbers(searchResult), contains("299DIF", "810FBE", "820LGN"));

        searchResult = searchService.search(asList(indexName), asList(), Filters.NULL, Sort.NULL, page.nextPage());

        assertThat(searchResult.documentCount(), is(3));
        assertThat(complaintNumbers(searchResult), contains("225OEJ", "210BIM", "396LLE"));
    }
}

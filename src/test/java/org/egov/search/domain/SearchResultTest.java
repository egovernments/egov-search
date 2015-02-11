package org.egov.search.domain;

import org.hamcrest.core.Is;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;

public class SearchResultTest {

    private String response;

    @Test
    public void shouldIsEmpty() {
        SearchResult searchResult = new SearchResult(response, asList());
        assertThat(searchResult.isEmpty(), Is.is(true));

        searchResult = new SearchResult(response, asList(new Document()));
        assertThat(searchResult.isEmpty(), Is.is(false));
    }

    @Test
    public void shouldDocumentCount() {
        SearchResult searchResult = new SearchResult(response, asList());
        assertThat(searchResult.documentCount(), Is.is(0));

        searchResult = new SearchResult(response, asList(new Document()));
        assertThat(searchResult.documentCount(), Is.is(1));
    }

}
package org.egov.search.service;

import org.egov.search.domain.Document;
import org.egov.search.domain.SearchResultTransformer;
import org.egov.search.util.JsonMatcher;
import org.junit.Test;

import java.util.List;

import static org.egov.search.util.Classpath.readAsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SearchResultTransformerTest {

    @Test
    public void shouldTransformResponseToDocuments() {
        String response = readAsString("searchResponseTransformerTest-response.json");

        List<Document> result = new SearchResultTransformer().transform(response);

        assertThat(result.size(), is(3));

        Document firstDocument = result.get(0);
        assertTrue(firstDocument instanceof Document);
        assertThat(firstDocument.getCorrelationId(), is("203461"));
        assertThat(firstDocument.getResource().toJSONString(), JsonMatcher.matches(readAsString("searchResponseTransformerTest-doc1.json")));
    }

    @Test
    public void shouldTransformResponseWhenSearchResultIsEmpty() {
        String response = readAsString("searchResponseTransformerTest-responseWithEmptyHits.json");
        List<Document> documents = new SearchResultTransformer().transform(response);

        assertThat(documents.size(), is(0));
    }
}
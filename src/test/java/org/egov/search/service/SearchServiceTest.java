package org.egov.search.service;

import org.egov.search.AbstractNodeIntegrationTest;
import org.egov.search.config.SearchConfig;
import org.egov.search.domain.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.egov.search.util.Classpath.readAsString;
import static org.mockito.Mockito.when;

public class SearchServiceTest extends AbstractNodeIntegrationTest {

    private String indexName;
    private ElasticSearchClient elasticSearchClient;

    @Mock
    private SearchConfig searchConfig;
    private SearchService searchService;
    private String indexType;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        indexName = this.getClass().getSimpleName().toLowerCase();
        indexType = "complaint";

        when(searchConfig.searchShardsFor(indexName)).thenReturn(1);
        when(searchConfig.searchReplicasFor(indexName)).thenReturn(0);
        elasticSearchClient = new ElasticSearchClient(client(), searchConfig);

        searchService = new SearchService(elasticSearchClient);

        indexPGRdata();
        refreshIndices(indexName);
    }

    @Test
    public void shouldSearchWithFilters() {
        Map<String, String> filters = new HashMap<>();
        filters.put("clauses.mode", "INTERNET");

        List<Document> documents = searchService.search(asList(indexName), asList(), filters);

//        assertThat(documents.size(), is(3));
    }

    private void indexPGRdata() {
        for (int id = 203461; id <= 203471; id++) {
            elasticSearchClient.index(id + "", readAsString(format("data/pgr/pgr%s.json", id)), indexName, indexType);
        }
    }
}
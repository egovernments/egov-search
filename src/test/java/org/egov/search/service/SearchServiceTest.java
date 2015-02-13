package org.egov.search.service;

import org.egov.search.AbstractNodeIntegrationTest;
import org.egov.search.config.SearchConfig;
import org.egov.search.domain.SearchResult;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.jayway.jsonpath.JsonPath.read;
import static java.lang.String.format;
import static org.egov.search.util.Classpath.readAsString;
import static org.mockito.Mockito.when;

public abstract class SearchServiceTest extends AbstractNodeIntegrationTest {

    protected String indexName;
    protected String indexType;

    protected ElasticSearchClient elasticSearchClient;
    protected SearchService searchService;

    @Mock
    private SearchConfig searchConfig;

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
    }


    protected List<String> complaintNumbers(SearchResult searchResult) {
        return read(searchResult.rawResponse(), "$..complaint_number");
    }

    protected List<String> packageNumbers(SearchResult searchResult) {
        return read(searchResult.rawResponse(), "$..package_number");
    }

    private void indexPGRdata() {
        for (int id = 203461; id <= 203471; id++) {
            elasticSearchClient.index(id + "", readAsString(format("data/pgr/pgr%s.json", id)), indexName, indexType);
        }
        refreshIndices(indexName);
    }

    protected void indexWorksPackageData() {
        for (int id = 1; id <= 5; id++) {
            elasticSearchClient.index(id + "", readAsString(format("data/works/workspackage/workspackage%s.json", id)), indexName, indexType);
        }
        refreshIndices(indexName);
    }
}

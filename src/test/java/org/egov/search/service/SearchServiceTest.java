package org.egov.search.service;

import org.egov.search.AbstractNodeIntegrationTest;
import org.egov.search.config.SearchConfig;
import org.egov.search.domain.Filters;
import org.egov.search.domain.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.jsonpath.JsonPath.read;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.egov.search.domain.Filters.withAndFilters;
import static org.egov.search.domain.Filters.withAndPlusNotFilters;
import static org.egov.search.domain.Filters.withAndPlusOrFilters;
import static org.egov.search.util.Classpath.readAsString;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
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
    }

    @Test
    public void shouldSearchWithSingleFilter() {
        Map<String, String> andFilters = new HashMap<>();
        andFilters.put("clauses.mode", "INTERNET");

        SearchResult searchResult = searchService.search(asList(indexName), asList(), Filters.withAndFilters(andFilters));

        assertThat(searchResult.documentCount(), is(3));
        assertThat(complaintNumbers(searchResult), contains("299DIF", "751HFP", "696IDN"));
    }

    @Test
    public void shouldSearchWithMultipleFilters() {
        Map<String, String> andFilters = new HashMap<>();
        andFilters.put("clauses.mode", "INTERNET");
        andFilters.put("clauses.status", "REGISTERED");

        SearchResult searchResult = searchService.search(asList(indexName), asList(), Filters.withAndFilters(andFilters));

        assertThat(searchResult.documentCount(), is(2));
        assertThat(complaintNumbers(searchResult), contains("751HFP", "696IDN"));
    }

    @Test
    public void shouldSearchWithMultipleFiltersIncludingPartialMatch() {
        Map<String, String> andFilters = new HashMap<>();
        andFilters.put("clauses.status", "REGISTERED");
        andFilters.put("common.citizen.address", "jakkasandra");

        SearchResult searchResult = searchService.search(asList(indexName), asList(), Filters.withAndFilters(andFilters));
        assertThat(searchResult.documentCount(), is(2));
        assertThat(complaintNumbers(searchResult), contains("810FBE", "892JBP"));
    }

    @Test
    public void shouldSearchWithAndPlusOrFilters() {
        Map<String, String> andFilters = new HashMap<>();
        andFilters.put("clauses.status", "REGISTERED");

        Map<String, String> orFilters = new HashMap<>();
        orFilters.put("searchable.title", "mosquito OR garbage");

        SearchResult searchResult = searchService.search(asList(indexName), asList(), withAndPlusOrFilters(andFilters, orFilters));
        assertThat(searchResult.documentCount(), is(3));
        assertThat(complaintNumbers(searchResult), contains("810FBE","820LGN", "751HFP"));
    }

    @Test
    public void shouldSearchWithOrFiltersOnSameField() {
        Map<String, String> andFilters = new HashMap<>();
        andFilters.put("clauses.status", "FORWARDED OR COMPLETED");

        SearchResult searchResult = searchService.search(asList(indexName), asList(), withAndFilters(andFilters));
        assertThat(searchResult.documentCount(), is(2));
        assertThat(complaintNumbers(searchResult), contains("299DIF","873GBH"));
    }

    @Test
    public void shouldSearchWithAndPlusNotInFilter() {
        Map<String, String> andFilters = new HashMap<>();
        andFilters.put("clauses.status", "REGISTERED");

        Map<String, String> notInFilters = new HashMap<>();
        notInFilters.put("clauses.mode", "CITIZEN");

        SearchResult searchResult = searchService.search(asList(indexName), asList(), withAndPlusNotFilters(andFilters, notInFilters));
        assertThat(searchResult.documentCount(), is(2));
        assertThat(complaintNumbers(searchResult), contains("751HFP","696IDN"));
    }

    private void indexPGRdata() {
        for (int id = 203461; id <= 203471; id++) {
            elasticSearchClient.index(id + "", readAsString(format("data/pgr/pgr%s.json", id)), indexName, indexType);
        }
        refreshIndices(indexName);
    }
    
    private List<String> complaintNumbers(SearchResult searchResult) {
        return read(searchResult.rawResponse(), "$..complaint_number");
    }
}

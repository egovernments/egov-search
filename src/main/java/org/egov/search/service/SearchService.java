package org.egov.search.service;

import org.egov.search.domain.Filters;
import org.egov.search.domain.SearchResult;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.QueryBuilders.queryString;

@Service
public class SearchService {

    private ElasticSearchClient elasticSearchClient;

    @Autowired
    public SearchService(ElasticSearchClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    public SearchResult search(List<String> indices, List<String> types, Filters filters) {

        List<QueryStringQueryBuilder> mustQueryStringFilters = queryStringFilters(filters.getAndFilters());
        List<QueryStringQueryBuilder> shouldQueryStringFilters = queryStringFilters(filters.getOrFilters());
        List<QueryStringQueryBuilder> notQueryStringFilters = queryStringFilters(filters.getNotInFilters());


        BoolFilterBuilder boolFilterBuilder = FilterBuilders.boolFilter();

        mustQueryStringFilters
                .stream()
                .forEach(filter -> boolFilterBuilder.must(FilterBuilders.queryFilter(filter)));
        shouldQueryStringFilters
                .stream()
                .forEach(filter -> boolFilterBuilder.should(FilterBuilders.queryFilter(filter)));
        notQueryStringFilters
                .stream()
                .forEach(filter -> boolFilterBuilder.mustNot(FilterBuilders.queryFilter(filter)));

        FilteredQueryBuilder filteredQueryBuilder = QueryBuilders.filteredQuery(null, boolFilterBuilder);

        String response = elasticSearchClient.search(indices, types, filteredQueryBuilder);
        return SearchResult.from(response);
    }

    private List<QueryStringQueryBuilder> queryStringFilters(Map<String, String> filters) {
        return filters.entrySet()
                    .stream()
                    .map(entry -> queryString(entry.getValue()).field(entry.getKey()))
                    .collect(toList());
    }
}

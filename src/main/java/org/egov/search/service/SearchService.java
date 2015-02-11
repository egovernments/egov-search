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

        List<QueryStringQueryBuilder> queryStringFilters = filters.getAndFilters().entrySet()
                .stream()
                .map(entry -> queryString(entry.getValue()).field(entry.getKey()))
                .collect(toList());


        BoolFilterBuilder boolFilterBuilder = FilterBuilders.boolFilter();
        queryStringFilters
                .stream()
                .forEach(filter -> boolFilterBuilder.must(FilterBuilders.queryFilter(filter)));

        FilteredQueryBuilder filteredQueryBuilder = QueryBuilders.filteredQuery(null, boolFilterBuilder);

        String response = elasticSearchClient.search(indices, types, filteredQueryBuilder);
        return SearchResult.from(response);
    }
}

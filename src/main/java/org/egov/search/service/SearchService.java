package org.egov.search.service;

import org.egov.search.domain.Filters;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
public class SearchService {

    private ElasticSearchClient elasticSearchClient;

    @Autowired
    public SearchService(ElasticSearchClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    public SearchResult search(List<String> indices, List<String> types, Filters filters, Sort sort) {
        BoolFilterBuilder boolFilterBuilder = constructBoolFilter(filters);

        QueryBuilder rootQueryBuilder = null;
        if(filters.isNotEmpty()) {
            rootQueryBuilder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), boolFilterBuilder);
        } else {
            rootQueryBuilder = QueryBuilders.matchAllQuery();
        }


        String response = elasticSearchClient.search(indices, types, rootQueryBuilder, sort);
        return SearchResult.from(response);
    }

    private BoolFilterBuilder constructBoolFilter(Filters filters) {
        List<QueryStringQueryBuilder> mustQueryStringFilters = queryString(filters.getAndFilters());
        List<QueryStringQueryBuilder> shouldQueryStringFilters = queryString(filters.getOrFilters());
        List<QueryStringQueryBuilder> notQueryStringFilters = queryString(filters.getNotInFilters());

        BoolFilterBuilder boolFilterBuilder = FilterBuilders.boolFilter();
        mustQueryStringFilters.stream().forEach(filter -> boolFilterBuilder.must(FilterBuilders.queryFilter(filter)));
        shouldQueryStringFilters.stream().forEach(filter -> boolFilterBuilder.should(FilterBuilders.queryFilter(filter)));
        notQueryStringFilters.stream().forEach(filter -> boolFilterBuilder.mustNot(FilterBuilders.queryFilter(filter)));

        return boolFilterBuilder;
    }

    private List<QueryStringQueryBuilder> queryString(Map<String, String> filters) {
        return filters.entrySet().stream()
                    .map(entry -> QueryBuilders.queryString(entry.getValue()).field(entry.getKey()))
                    .collect(toList());
    }
}

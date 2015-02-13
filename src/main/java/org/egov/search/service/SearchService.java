package org.egov.search.service;

import org.egov.search.domain.Filter;
import org.egov.search.domain.Filters;
import org.egov.search.domain.MatchFilter;
import org.egov.search.domain.Page;
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

import static java.util.stream.Collectors.toList;

@Service
public class SearchService {

    private ElasticSearchClient elasticSearchClient;

    @Autowired
    public SearchService(ElasticSearchClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    public SearchResult search(List<String> indices, List<String> types, Filters filters, Sort sort, Page page) {
        BoolFilterBuilder boolFilterBuilder = constructBoolFilter(filters);

        QueryBuilder rootQueryBuilder;
        if (filters.isNotEmpty()) {
            rootQueryBuilder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), boolFilterBuilder);
        } else {
            rootQueryBuilder = QueryBuilders.matchAllQuery();
        }

        String response = elasticSearchClient.search(indices, types, rootQueryBuilder, sort, page);
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

    private List<QueryStringQueryBuilder> queryString(List<Filter> filters) {
        return filters.stream()
                .map(filter -> QueryBuilders.queryString(((MatchFilter) filter).value()).field(filter.field()))
                .collect(toList());
    }
}

package org.egov.search.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.search.domain.Filter;
import org.egov.search.domain.Filters;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    private ElasticSearchClient elasticSearchClient;

    @Autowired
    public SearchService(ElasticSearchClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    public SearchResult search(List<String> indices, List<String> types, String searchText, Filters filters, Sort sort, Page page) {
        QueryBuilder filterBuilder = constructBoolFilter(filters);

        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        if(StringUtils.isNotEmpty(searchText)) {
            queryBuilder = QueryBuilders.queryStringQuery(searchText)
                    .lenient(true)
                    .field("searchable.*")
                    .field("common.*")
            		.field("clauses.*");
        }

        QueryBuilder rootQueryBuilder;
        if (filters.isNotEmpty()) {
            rootQueryBuilder = QueryBuilders.boolQuery().must(queryBuilder).filter(filterBuilder);
        } else {
            rootQueryBuilder = queryBuilder;
        }

        String response = elasticSearchClient.search(indices, types, rootQueryBuilder, sort, page);
        return SearchResult.from(response);
    }

    private BoolQueryBuilder constructBoolFilter(Filters filters) {
        List<QueryBuilder> mustFilters = queryBuilders(filters.getAndFilters());
        List<QueryBuilder> shouldFilters = queryBuilders(filters.getOrFilters());
        List<QueryBuilder> notFilters = queryBuilders(filters.getNotInFilters());

        BoolQueryBuilder boolFilterBuilder = QueryBuilders.boolQuery();
        mustFilters.stream().forEach(boolFilterBuilder::must);
        shouldFilters.stream().forEach(boolFilterBuilder::should);
        notFilters.stream().forEach(boolFilterBuilder::mustNot);

        return boolFilterBuilder;
    }

    private List<QueryBuilder> queryBuilders(List<Filter> filters) {
        return filters.stream()
                .map(Filter::queryBuilder)
                .collect(toList());
    }

}

package org.egov.search.service;

import org.egov.search.domain.Document;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    public List<Document> search(List<String> indices, List<String> types, Map<String, String> filterMap) {
        List<QueryStringQueryBuilder> queryStringFilters = filterMap.entrySet()
                .stream()
                .map(entry -> queryString(entry.getValue()).field(entry.getKey()))
                .collect(toList());


        BoolFilterBuilder boolFilterBuilder = FilterBuilders.boolFilter();
        queryStringFilters
                .stream()
                .forEach(filter -> boolFilterBuilder.must(FilterBuilders.queryFilter(filter)));

        FilteredQueryBuilder filteredQueryBuilder = QueryBuilders.filteredQuery(null, boolFilterBuilder);

        String response = elasticSearchClient.search(indices, types, filteredQueryBuilder);
        System.out.println(response);
        return Arrays.asList(new Document("", null));
    }
}

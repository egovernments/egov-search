package org.egov.search.service;

import org.egov.search.config.SearchConfig;
import org.egov.search.domain.Page;
import org.egov.search.domain.Sort;
import org.egov.search.util.Classpath;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class ElasticSearchClient {

    private Client client;
    private SearchConfig searchConfig;

    @Autowired
    public ElasticSearchClient(@Qualifier("transportClient") Client client, SearchConfig searchConfig) {
        this.client = client;
        this.searchConfig = searchConfig;
    }

    public boolean index(String documentId, String document, String indexName, String type) {
        if (!indexExists(indexName)) {
            createIndex(indexName);
        }

        IndexRequestBuilder indexRequestBuilder = new IndexRequestBuilder(client)
                .setIndex(indexName)
                .setType(type)
                .setSource(document)
                .setId(documentId);

        IndexResponse indexResponse = client.index(indexRequestBuilder.request()).actionGet();
        return indexResponse.isCreated();
    }

    public String search(List<String> indices, List<String> types, QueryBuilder queryBuilder, Sort sort, Page page) {
        SearchRequestBuilder requestBuilder = new SearchRequestBuilder(client)
                .setIndices(toArray(indices))
                .setTypes(toArray(types))
                .setQuery(queryBuilder)
                .setFrom(page.offset())
                .setSize(page.size());

        sort.stream().forEach(sf -> requestBuilder.addSort(sf.field(), sf.order()));
        SearchResponse searchResponse = client.search(requestBuilder.request()).actionGet();
        return searchResponse.toString();
    }

    private String[] toArray(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

    private boolean indexExists(String name) {
        return client.admin().indices().exists(new IndicesExistsRequest(name)).actionGet().isExists();
    }

    private CreateIndexResponse createIndex(String indexName) {
        ImmutableSettings.Builder settingsBuilder = ImmutableSettings.settingsBuilder();
        Settings settings = settingsBuilder
                .put("index.mapper.dynamic", true)
                .put("index.number_of_shards", searchConfig.searchShardsFor(indexName))
                .put("index.number_of_replicas", searchConfig.searchReplicasFor(indexName))
                .build();

        String dynamicTemplates = Classpath.readAsString("config/search/dynamic-templates.json");
        CreateIndexRequest createIndexRequest = (new CreateIndexRequest(indexName)).settings(settings).mapping("_default_", dynamicTemplates);
        return client.admin().indices().create(createIndexRequest).actionGet();
    }
}

package org.egov.search.service;

import java.util.List;

import org.egov.search.config.SearchConfig;
import org.egov.search.domain.Page;
import org.egov.search.domain.Sort;
import org.egov.search.util.Classpath;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.index.IndexAction;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
class ElasticSearchClient {

    private Client client;
    private SearchConfig searchConfig;

    @Autowired
    public ElasticSearchClient(@Qualifier("transportClient") Client client, SearchConfig searchConfig) {
        this.client = client;
        this.searchConfig = searchConfig;
    }

    public boolean index(String index, String type, String id, String json) {
        if (!indexExists(index)) {
            createIndex(index);
        }

        IndexRequestBuilder indexRequestBuilder = new IndexRequestBuilder(client, IndexAction.INSTANCE)
                .setIndex(index)
                .setType(type)
                .setSource(json)
                .setId(id);

        IndexResponse indexResponse = client.index(indexRequestBuilder.request()).actionGet();
        return indexResponse.isCreated();
    }

	public String search(List<String> indices, List<String> types,QueryBuilder queryBuilder, Sort sort, Page page) {
		SearchRequestBuilder requestBuilder = new SearchRequestBuilder(client, SearchAction.INSTANCE)
				.setIndices(toArray(indices))
				.setTypes(toArray(types))
				.setQuery(queryBuilder)
				.setFrom(page.offset())
				.setSize(page.size());
		
		sort.stream().forEach(sf -> requestBuilder.addSort(sf.field(), sf.order()));
		boolean indexExits = indices.stream().allMatch(obj -> {
			if (!indexExists(obj)) {
				return false;
			}
			return true;
		});
		SearchResponse searchResponse = null;
		if (indexExits) {
			searchResponse = client.search(requestBuilder.request())
					.actionGet();
		}
		return searchResponse != null ? searchResponse.toString() : "";
         
    }

    private String[] toArray(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

    private boolean indexExists(String name) {
        return client.admin().indices().exists(new IndicesExistsRequest(name)).actionGet().isExists();
    }

    private CreateIndexResponse createIndex(String indexName) {
        Settings settings = Settings.settingsBuilder()
                .put("index.mapper.dynamic", true)
                .put("index.number_of_shards", searchConfig.searchShardsFor(indexName))
                .put("index.number_of_replicas", searchConfig.searchReplicasFor(indexName))
                .put("index.max_result_window", 999999999)
                .build();

        String dynamicTemplates = Classpath.readAsString("config/search/dynamic-templates.json");
        CreateIndexRequest createIndexRequest = (new CreateIndexRequest(indexName)).settings(settings).mapping("_default_", dynamicTemplates);
        return client.admin().indices().create(createIndexRequest).actionGet();
    }
}

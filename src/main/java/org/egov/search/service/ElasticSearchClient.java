/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
class ElasticSearchClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchClient.class);
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

    public String search(List<String> indices, List<String> types, QueryBuilder queryBuilder, Sort sort, Page page) {
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
        LOGGER.info("Creating Index {} ", indexName);
        Settings settings = Settings.settingsBuilder()
                .put("index.mapper.dynamic", true)
                .put("index.number_of_shards", searchConfig.searchShardsFor(indexName))
                .put("index.number_of_replicas", searchConfig.searchReplicasFor(indexName))
                .build();

        String dynamicTemplates = Classpath.readAsString("config/search/dynamic-templates.json");
        CreateIndexRequest createIndexRequest = (new CreateIndexRequest(indexName)).settings(settings).mapping("_default_", dynamicTemplates);
        return client.admin().indices().create(createIndexRequest).actionGet();
    }
}

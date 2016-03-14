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

package org.egov.search;

import com.jayway.restassured.RestAssured;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthAction;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequestBuilder;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexAction;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.Arrays;

public abstract class AbstractNodeIntegrationTest {
    private static final TimeValue TIMEOUT = TimeValue.timeValueSeconds(5L);
    private static final ESLogger ES_LOGGER = Loggers.getLogger(AbstractNodeIntegrationTest.class);
    protected static Node node;
    protected static int PORT = 9209;

    @BeforeClass
    public static void beforeAllTests() throws IOException, InterruptedException {
        Settings settings = Settings.settingsBuilder()
                .put("path.data", "target/es-data")
                .put("http.port", PORT)
                .put("path.home", "target/es-data")
                .put("cluster.name", "test-cluster").build();

        node = NodeBuilder.nodeBuilder().local(true).settings(settings).node();
        RestAssured.baseURI = "http://localhost/";
        RestAssured.port = PORT;
    }

    @AfterClass
    public static void afterAllTests() throws InterruptedException {
        Client client = node.client();
        String[] indices = client.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().concreteAllIndices();
        DeleteIndexResponse deleteIndexResponse = client.admin().indices().prepareDelete(indices).execute().actionGet();
        ES_LOGGER.info("Delete indices [{}] acknowledged [{}]", Arrays.toString(indices), deleteIndexResponse.isAcknowledged());
        node.close();
    }

    protected CreateIndexResponse createIndexOnNode(String indexName) {
        if (indexExists(indexName)) {
            ES_LOGGER.warn("Index [" + indexName + "] already exists. Attempting to delete it.");
            DeleteIndexResponse builder = this.deleteIndexOnNode(indexName);
            ES_LOGGER.info("Delete index [{}] acknowledged [{}]", indexName, builder.isAcknowledged());
        }

        Settings settings = Settings.settingsBuilder()
                .put("index.mapper.dynamic", true)
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
                .build();
        CreateIndexResponse createIndexResponse = node.client().admin().indices()
                .create((new CreateIndexRequest(indexName)).settings(settings)).actionGet(TIMEOUT);
        waitForGreenClusterState(indexName);
        return createIndexResponse;
    }

    protected DeleteIndexResponse deleteIndexOnNode(String indexName) {
        if (!this.indexExists(indexName)) {
            ES_LOGGER.warn("Index [" + indexName + "] does not exist. Cannot delete.");
            return DeleteIndexAction.INSTANCE.newResponse();
        } else {
            return node.client().admin().indices().delete(new DeleteIndexRequest(indexName)).actionGet(TIMEOUT);
        }
    }

    protected boolean indexExists(String name) {
        return node.client().admin().indices().exists(new IndicesExistsRequest(name)).actionGet().isExists();
    }

    protected boolean typeExists(String indexName, String typeName) {
        return node.client().admin().indices().prepareTypesExists(new String[]{indexName})
                .setTypes(new String[]{typeName}).execute().actionGet().isExists();
    }

    protected void refreshIndices(String... indices) {
        node.client().admin().indices().prepareRefresh(indices).execute().actionGet();
    }

    private ClusterHealthResponse waitForGreenClusterState(String index) {
        ClusterAdminClient clusterAdminClient = node.client().admin().cluster();
        ClusterHealthRequest request = (new ClusterHealthRequestBuilder(clusterAdminClient, ClusterHealthAction.INSTANCE))
                .setIndices(index).setWaitForGreenStatus().request();
        return clusterAdminClient.health(request).actionGet();
    }

    protected CreateIndexResponse createIndexOnNode(String indexName, Settings settings) {
        if (this.indexExists(indexName)) {
            ES_LOGGER.warn("Index [" + indexName + "] already exists. Attempting to delete it.");
            DeleteIndexResponse deleteIndexResponse = this.deleteIndexOnNode(indexName);
            ES_LOGGER.info("Delete index [{}] acknowledged [{}]", indexName, deleteIndexResponse.isAcknowledged());
        }

        CreateIndexResponse createIndexResponse = node.client().admin().indices()
                .create((new CreateIndexRequest(indexName)).settings(settings)).actionGet(TIMEOUT);
        this.waitForGreenClusterState(indexName);
        return createIndexResponse;
    }

    protected AdminClient adminClient() {
        return node.client().admin();
    }

    protected Client client() {
        return node.client();
    }
}
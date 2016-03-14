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

import com.jayway.restassured.RestAssured;
import org.egov.search.AbstractNodeIntegrationTest;
import org.egov.search.config.SearchConfig;
import org.egov.search.domain.Page;
import org.egov.search.domain.Sort;
import org.elasticsearch.index.query.QueryBuilders;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.jayway.jsonassert.JsonAssert.with;
import static com.jayway.restassured.RestAssured.get;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ElasticSearchClientTest extends AbstractNodeIntegrationTest {

    private String indexName;
    private ElasticSearchClient indexClient;

    @Mock
    private SearchConfig searchConfig;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        indexName = this.getClass().getSimpleName().toLowerCase();
        when(searchConfig.searchShardsFor(indexName)).thenReturn(1);
        when(searchConfig.searchReplicasFor(indexName)).thenReturn(0);
        indexClient = new ElasticSearchClient(client(), searchConfig);

        RestAssured.baseURI = "http://localhost/";
        RestAssured.port = PORT;
    }

    @Test
    public void shouldIndexDocument() {
        Map<String, String> attrs = new HashMap<>();
        attrs.put("name", "Srivatsa Katta");
        attrs.put("role", "Application Developer");
        attrs.put("location", "Bengaluru");

        String type = "employee";
        boolean documentIndexed = indexClient.index(indexName, type, "document_id", new JSONObject(attrs).toJSONString());
        assertTrue(documentIndexed);

        refreshIndices(indexName);

        String searchResult = indexClient.search(asList(indexName), asList(type), QueryBuilders.queryStringQuery("developer"), Sort.NULL, Page.NULL);

        with(searchResult).assertEquals("$.hits.total", 1);
        with(searchResult).assertEquals("$.hits.hits[0]._id", "document_id");
    }

    @Test
    public void shouldCreateMappingBasedOnDynamicTemplates() throws ExecutionException, InterruptedException {
        JSONObject searchable = new JSONObject();
        searchable.put("name", "Srivatsa Katta");
        searchable.put("role", "Application Developer");
        searchable.put("location", "Bengaluru");


        JSONObject clauses = new JSONObject();
        clauses.put("department", "PS");
        clauses.put("status", "Permanant");

        JSONObject resource = new JSONObject();
        resource.put("searchable", searchable);
        resource.put("clauses", clauses);

        boolean indexed = indexClient.index(indexName, "users", "123", resource.toJSONString());
        assertTrue(indexed);

        refreshIndices(indexName);

        String mapping = get(indexName + "/_mapping/users").asString();
        with(mapping).assertEquals("$..properties.clauses..department.index", asList("not_analyzed"));
        with(mapping).assertEquals("$..properties.clauses..status.index", asList("not_analyzed"));
        with(mapping).assertNotDefined("$..searchable..name.index");
        with(mapping).assertNotDefined("$..searchable..role.index");
    }
}
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

import org.egov.search.AbstractNodeIntegrationTest;
import org.egov.search.config.SearchConfig;
import org.egov.search.domain.SearchResult;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.jayway.jsonpath.JsonPath.read;
import static java.lang.String.format;
import static org.egov.search.util.Classpath.readAsString;
import static org.mockito.Mockito.when;

public abstract class SearchServiceTest extends AbstractNodeIntegrationTest {

    protected String indexName;
    protected String indexType;

    protected ElasticSearchClient elasticSearchClient;
    protected SearchService searchService;

    @Mock
    private SearchConfig searchConfig;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        indexName = this.getClass().getSimpleName().toLowerCase();
        indexType = "complaint";

        when(searchConfig.searchShardsFor(indexName)).thenReturn(1);
        when(searchConfig.searchReplicasFor(indexName)).thenReturn(0);
        elasticSearchClient = new ElasticSearchClient(client(), searchConfig);

        searchService = new SearchService(elasticSearchClient);

        indexPGRdata();
    }


    protected List<String> complaintNumbers(SearchResult searchResult) {
        return read(searchResult.rawResponse(), "$..complaint_number");
    }

    protected List<String> packageNumbers(SearchResult searchResult) {
        return read(searchResult.rawResponse(), "$..package_number");
    }

    private void indexPGRdata() {
        for (int id = 203461; id <= 203471; id++) {
            elasticSearchClient.index(indexName, indexType, id + "", readAsString(format("data/pgr/pgr%s.json", id)));
        }
        refreshIndices(indexName);
    }

    protected void indexWorksPackageData() {
        for (int id = 1; id <= 5; id++) {
            elasticSearchClient.index(indexName, indexType, id + "", readAsString(format("data/works/workspackage/workspackage%s.json", id)));
        }
        refreshIndices(indexName);
    }
}

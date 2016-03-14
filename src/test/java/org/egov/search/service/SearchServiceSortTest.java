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

import org.egov.search.domain.Filters;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.elasticsearch.search.sort.SortOrder;
import org.hamcrest.core.Is;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;

public class SearchServiceSortTest extends SearchServiceTest {

    @Test
    public void shouldSortByComplaintNumber() {
        Sort sort = Sort.by().field("searchable.complaint_number", SortOrder.ASC);

        SearchResult searchResult = searchService.search(asList(indexName), asList(), "", Filters.NULL, sort, Page.NULL);

        assertThat(searchResult.documentCount(), Is.is(11));
        assertThat(searchResult.getDocuments().get(0).getCorrelationId(), Is.is("203465"));
        assertThat(searchResult.getDocuments().get(1).getCorrelationId(), Is.is("203464"));
        assertThat(searchResult.getDocuments().get(10).getCorrelationId(), Is.is("203467"));
    }

    @Test
    public void shouldSortByMultipleFields() {
        Sort sort = Sort.by()
                .field("searchable.title", SortOrder.ASC)
                .field("searchable.complaint_number", SortOrder.DESC);

        SearchResult searchResult = searchService.search(asList(indexName), asList(), "", Filters.NULL, sort, Page.NULL);

        assertThat(searchResult.documentCount(), Is.is(11));
        assertThat(searchResult.getDocuments().get(8).getCorrelationId(), Is.is("203463"));
        assertThat(searchResult.getDocuments().get(9).getCorrelationId(), Is.is("203468"));
        assertThat(searchResult.getDocuments().get(10).getCorrelationId(), Is.is("203461"));

    }
}

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
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.egov.search.domain.Filter.queryStringFilter;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SearchServiceFreeTextTest extends SearchServiceTest {

    @Test
    public void shouldSearchForFreeText() {
        SearchResult searchResult = searchService.search(asList(indexName), asList(), "mosquito", Filters.NULL, Sort.NULL, Page.NULL);

        assertThat(searchResult.documentCount(), is(3));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("299DIF", "820LGN", "751HFP"));
    }

    @Test
    public void shouldSearchForFreeTextWithFilters() {
        Filters filters = Filters.withAndFilters(asList(queryStringFilter("clauses.status", "FORWARDED")));
        SearchResult searchResult = searchService.search(asList(indexName), asList(), "mosquito", filters, Sort.NULL, Page.NULL);

        assertThat(searchResult.documentCount(), is(1));
        assertThat(complaintNumbers(searchResult), containsInAnyOrder("299DIF"));
    }


}

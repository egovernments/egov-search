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

import org.apache.commons.lang.StringUtils;
import org.egov.search.domain.Filter;
import org.egov.search.domain.Filters;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.egov.search.util.SearchUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

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
        if (StringUtils.isNotEmpty(searchText)) {
            queryBuilder = QueryBuilders.queryStringQuery(SearchUtil.escape(searchText))
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

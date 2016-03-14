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

package org.egov.search.domain;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.ArrayList;
import java.util.List;

public abstract class Filter {
    protected String fieldName;

    protected Filter(String fieldName) {
        this.fieldName = fieldName;
    }

    public static Filter queryStringFilter(String fieldName, String value) {
        if (StringUtils.isEmpty(value) || StringUtils.isEmpty(fieldName)) {
            return new NoOpFilter();
        }

        return new QueryStringFilter(fieldName, value);
    }

    public static Filter rangeFilter(String fieldName, String from, String to) {
        if ((StringUtils.isEmpty(from) && StringUtils.isEmpty(to))
                || StringUtils.isEmpty(fieldName)) {
            return new NoOpFilter();
        }
        return new RangeFilter(fieldName, from, to);
    }

    public static Filter rangeFilterFrom(String fieldName, String from) {
        if (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(from)) {
            return new NoOpFilter();
        }
        return rangeFilter(fieldName, from, null);
    }

    public static Filter rangeFilterTo(String fieldName, String to) {
        if (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(to)) {
            return new NoOpFilter();
        }
        return rangeFilter(fieldName, null, to);
    }

    public static Filter termsStringFilter(String fieldName, String... values) {
        List<String> valuesList = new ArrayList<String>(0);

        for (String value : values) {
            if (value != null) {
                valuesList.add(value);
            }
        }

        if (StringUtils.isEmpty(fieldName) || valuesList.size() == 0) {
            return new NoOpFilter();
        }

        return new TermsStringFilter(fieldName, valuesList.stream().toArray(String[]::new));
    }

    public String field() {
        return fieldName;
    }

    public QueryBuilder queryBuilder() {
        return QueryBuilders.matchAllQuery();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}

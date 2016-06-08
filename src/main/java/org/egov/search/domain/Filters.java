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

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class Filters {
    public static final Filters NULL = new Filters();

    private List<Filter> andFilters;
    private List<Filter> orFilters;
    private List<Filter> notInFilters;

    private Filters() {
        this(asList(), asList(), asList());
    }

    private Filters(List<Filter> andFilters, List<Filter> orFilters, List<Filter> notInFilters) {
        this.andFilters = removeNoOpFilters(andFilters);
        this.orFilters = removeNoOpFilters(orFilters);
        this.notInFilters = removeNoOpFilters(notInFilters);
    }

    public static Filters withAndFilters(List<Filter> andFilters) {
        return withAndPlusOrFilters(andFilters, asList());
    }

    public static Filters withOrFilters(List<Filter> orFilters) {
        return withAndPlusOrFilters(asList(), orFilters);
    }

    public static Filters withAndPlusOrFilters(List<Filter> andFilters, List<Filter> orFilters) {
        return new Filters(andFilters, orFilters, asList());
    }

    public static Filters withAndPlusNotFilters(List<Filter> andFilters, List<Filter> notFilters) {
        return new Filters(andFilters, asList(), notFilters);
    }

    public static Filters withOrPlusNotFilters(List<Filter> orFilters, List<Filter> notFilters) {
        return new Filters(asList(), orFilters, notFilters);
    }

    private List<Filter> removeNoOpFilters(List<Filter> filters) {
        return filters.stream().filter(f -> !(f instanceof NoOpFilter)).collect(toList());
    }

    public List<Filter> getAndFilters() {
        return andFilters;
    }

    public List<Filter> getOrFilters() {
        return orFilters;
    }

    public List<Filter> getNotInFilters() {
        return notInFilters;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return andFilters.isEmpty() && orFilters.isEmpty() && notInFilters.isEmpty();
    }
}

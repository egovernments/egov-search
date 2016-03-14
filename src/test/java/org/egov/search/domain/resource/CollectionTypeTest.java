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

package org.egov.search.domain.resource;

import org.egov.search.domain.entities.Person;
import org.hamcrest.Matchers;
import org.json.simple.JSONArray;
import org.junit.Test;

import java.lang.reflect.Field;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class CollectionTypeTest {
    @Test
    public void shouldRetrieveValueForCollectionWithSimpleTypes() throws NoSuchFieldException {
        Person person = Person.newInstance();
        Field jobsField = Person.class.getDeclaredField("jobs");

        Object collectionValue = new CollectionType(jobsField).jsonValue(person);

        assertThat(collectionValue, Matchers.instanceOf(JSONArray.class));

        String json = ((JSONArray) collectionValue).toJSONString();
        with(json).assertThat("$", containsInAnyOrder("Job One", "Job Two"));
    }

    @Test
    public void shouldRetrieveValueForCollectionWithNestedTypes() throws NoSuchFieldException {
        Person person = Person.newInstance();
        Field addressesField = Person.class.getDeclaredField("addresses");

        Object collectionValue = new CollectionType(addressesField).jsonValue(person);

        assertThat(collectionValue, Matchers.instanceOf(JSONArray.class));
        String json = ((JSONArray) collectionValue).toJSONString();

        with(json).assertThat("$.*.street", containsInAnyOrder("str1", "str2"));

    }
}
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

import org.egov.search.domain.entities.Address;
import org.egov.search.domain.entities.Person;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.jayway.jsonassert.JsonAssert.with;

public class SimpleFieldsResourceGeneratorTest {

    private String json;

    @Before
    public void before() {
        Person person = Person.newInstance();
        ResourceGenerator<Person> resourceGenerator = new ResourceGenerator<>(Person.class, person);

        JSONObject resource = resourceGenerator.generate();

        json = resource.toJSONString();
    }

    @Test
    public void withMultipleFields() {
        with(json).assertEquals("$.searchable.citizen", true);
        with(json).assertEquals("$.searchable.age", 37);
        with(json).assertEquals("$.clauses.status", "ACTIVE");

        with(json).assertNotDefined("$.searchable.role");
    }

    @Test
    public void withMultipleFields_WithGivenName() {
        with(json).assertEquals("$.searchable.citizen_name", "Elzan");
    }

    @Test
    public void withAListField() {
        with(json).assertEquals("$.searchable.jobs", Arrays.asList("Job One", "Job Two"));
    }

    @Test
    public void withAMapField() {
        with(json).assertEquals("$.searchable.serviceHistory.company1", "Job One");
        with(json).assertEquals("$.searchable.serviceHistory.company2", "Job Two");
    }

    @Test
    public void shouldGroupFields() {
        Address address = new Address("street1", "560001");
        ResourceGenerator<Address> resourceGenerator = new ResourceGenerator<>(Address.class, address);

        JSONObject resource = resourceGenerator.generate();

        String json = resource.toJSONString();
        with(json).assertEquals("$.searchable.street", "street1");
        with(json).assertEquals("$.searchable.pincode", "560001");
        with(json).assertNotDefined("$.clauses");
        with(json).assertNotDefined("$.common");
    }
}



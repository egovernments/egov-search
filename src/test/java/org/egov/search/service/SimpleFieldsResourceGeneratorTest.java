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



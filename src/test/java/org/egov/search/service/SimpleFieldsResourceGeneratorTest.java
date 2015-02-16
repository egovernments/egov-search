package org.egov.search.service;

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
        with(json).assertEquals("$.citizen", true);
        with(json).assertEquals("$.age", 37);
        with(json).assertNotDefined("$.role");
    }

    @Test
    public void withMultipleFields_WithGivenName() {
        with(json).assertEquals("$.citizen_name", "Elzan");
    }
    
    @Test
    public void withAListField() {
        with(json).assertEquals("$.jobs", Arrays.asList("Job One", "Job Two"));
    }

    @Test
    public void withAMapField() {
        with(json).assertEquals("$.serviceHistory.company1", "Job One");
        with(json).assertEquals("$.serviceHistory.company2", "Job Two");
    }
}



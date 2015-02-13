package org.egov.search.service;

import org.egov.search.service.resources.Person;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static com.jayway.jsonassert.JsonAssert.with;

public class NestedFieldsResourceGeneratorTest {

    private String json;

    @Before
    public void before() {
        Person person = Person.newInstance();

        ResourceGenerator<Person> resourceGenerator = new ResourceGenerator<>(Person.class, person);
        JSONObject resource = resourceGenerator.generate();

        json = resource.toJSONString();
    }

    @Test
    public void withNestedObject() {
        with(json).assertEquals("$.currentDepartment.name", "deptName");
        with(json).assertEquals("$.currentDepartment.code", "deptCode");
        with(json).assertNotDefined("$.currentDepartment.id");
    }

    @Test
    public void withMultipleNestedObjects() {
        with(json).assertEquals("$.currentDepartment.name", "deptName");
        with(json).assertEquals("$.currentDepartment.code", "deptCode");
        with(json).assertNotDefined("$.currentDepartment.id");

        with(json).assertEquals("$.previous_department.name", "oldDeptName");
        with(json).assertEquals("$.previous_department.code", "oldDeptCode");
        with(json).assertNotDefined("$.previous_department.id");
    }

    @Test
    public void shouldMultiLevelNestedObject() {
        System.out.println(json);

        with(json).assertEquals("$.currentDepartment.address.street", "mg road");
        with(json).assertEquals("$.currentDepartment.address.pincode", "560001");
    }
}
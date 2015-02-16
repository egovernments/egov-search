package org.egov.search.service;

import org.egov.search.domain.entities.Person;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.containsInAnyOrder;

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
        with(json).assertEquals("$.common.currentDepartment.name", "deptName");
        with(json).assertEquals("$.common.currentDepartment.code", "deptCode");
        with(json).assertNotDefined("$.common.currentDepartment.id");
    }

    @Test
    public void withMultipleNestedObjects() {
        with(json).assertEquals("$.common.currentDepartment.name", "deptName");
        with(json).assertEquals("$.common.currentDepartment.code", "deptCode");
        with(json).assertNotDefined("$.common.currentDepartment.id");

        with(json).assertEquals("$.searchable.previous_department.name", "oldDeptName");
        with(json).assertEquals("$.searchable.previous_department.code", "oldDeptCode");
        with(json).assertNotDefined("$.searchable.previous_department.id");
    }

    @Test
    public void withMultiLevelNestedObject() {
        with(json).assertEquals("$.common.currentDepartment.address.street", "mg road");
        with(json).assertEquals("$.common.currentDepartment.address.pincode", "560001");

        with(json).assertNotDefined("$.searchable.previous_department.address");
    }

    @Test
    public void withNestedObjectInACollection() {
        with(json).assertThat("$.searchable.addresses[*].street", containsInAnyOrder("str1", "str2"));
    }

    @Test
    public void withNestedObjectInAMap() {
        with(json).assertEquals("$.searchable.communicationAddresses.primary.street", "str1");
        with(json).assertEquals("$.searchable.communicationAddresses.secondary.street", "str2");
    }

    @Test
    public void shouldNotGroupNestedObjectsBeyondFirstLevelInRootJson() {
        with(json).assertNotDefined("$.common.currentDepartment.clauses");
    }
}
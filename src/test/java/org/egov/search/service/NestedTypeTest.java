package org.egov.search.service;

import org.egov.search.service.resources.Person;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.lang.reflect.Field;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.junit.Assert.assertThat;

public class NestedTypeTest {

    @Test
    public void shouldRetrieveValueOfNestedType() throws NoSuchFieldException {
        Person person = Person.newInstance();
        Field departmentField = Person.class.getDeclaredField("currentDepartment");

        Object nestedValue = new NestedType(departmentField).propertyValue(person);

        assertThat(nestedValue, Matchers.instanceOf(JSONObject.class));

        String json = ((JSONObject) nestedValue).toJSONString();
        with(json).assertEquals("$.name", "deptName");
    }

}
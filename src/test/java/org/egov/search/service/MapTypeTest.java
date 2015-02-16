package org.egov.search.service;

import org.egov.search.service.resources.Person;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.lang.reflect.Field;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.junit.Assert.assertThat;

public class MapTypeTest {

    @Test
    public void shouldRetrieveValueForMapWithSimpleObjects() throws NoSuchFieldException {
        Person person = Person.newInstance();
        Field serviceHistoryField = Person.class.getDeclaredField("serviceHistory");

        Object mapValue = new MapType(serviceHistoryField).propertyValue(person);

        assertThat(mapValue, Matchers.instanceOf(JSONObject.class));

        String json = ((JSONObject) mapValue).toJSONString();
        with(json).assertEquals("$.company1", "Job One");
        with(json).assertEquals("$.company2", "Job Two");
    }

    @Test
    public void shouldRetrieveValueForMapWithNestedObjects() throws NoSuchFieldException {
        Person person = Person.newInstance();
        Field communicationAddressesField = Person.class.getDeclaredField("communicationAddresses");

        Object mapValue = new MapType(communicationAddressesField).propertyValue(person);

        assertThat(mapValue, Matchers.instanceOf(JSONObject.class));

        String json = ((JSONObject) mapValue).toJSONString();
        with(json).assertEquals("$.primary.street", "str1");
        with(json).assertEquals("$.secondary.street", "str2");
        with(json).assertNotDefined("$.secondary.pincode");
    }

}
package org.egov.search.domain.resource;

import net.minidev.json.JSONArray;
import org.egov.search.domain.entities.Person;
import org.hamcrest.Matchers;
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
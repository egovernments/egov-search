package org.egov.search.service;

import org.egov.search.service.resources.Person;
import org.hamcrest.core.Is;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertThat;

public class SimpleTypeTest {

    @Test
    public void shouldRetrieveValueForSimpleType() throws NoSuchFieldException {
        Person person = Person.newInstance();
        Field nameField = Person.class.getDeclaredField("name");

        Object name = new SimpleType(nameField).propertyValue(person);

        assertThat(name, Is.is("Elzan"));
    }

}
package org.egov.search.service;

import org.egov.search.service.resources.Person;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public class TypeTest {

    @Test
    public void shouldIdentifySimpleType() throws NoSuchFieldException {
        Field nameField = Person.class.getDeclaredField("name");

        assertThat(Type.newInstanceFor(nameField), instanceOf(SimpleType.class));
    }

    @Test
    public void shouldIdentifyNestedType() throws NoSuchFieldException {
        Field deptField = Person.class.getDeclaredField("currentDepartment");

        assertThat(Type.newInstanceFor(deptField), instanceOf(NestedType.class));
    }

    @Test
    public void shouldIdentifyCollectionType() throws NoSuchFieldException {
        Field addressesField = Person.class.getDeclaredField("addresses");

        assertThat(Type.newInstanceFor(addressesField), instanceOf(CollectionType.class));
    }
    
    @Test
    public void shouldIdentifyMapType() throws NoSuchFieldException {
        Field addressesField = Person.class.getDeclaredField("communicationAddresses");

        assertThat(Type.newInstanceFor(addressesField), instanceOf(MapType.class));
    }

}
package org.egov.search.service;

import java.lang.reflect.Field;

public class NestedType extends Type {

    public NestedType(Field field) {
        super(field);
    }

    @Override
    public Object retrievePropertyValue(Object fieldValue) {
        return new ResourceGenerator<>(field.getType(), fieldValue).generate();
    }
}

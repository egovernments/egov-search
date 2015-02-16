package org.egov.search.service;

import org.egov.search.util.Beans;

import java.lang.reflect.Field;

public class NestedType extends Type {

    public NestedType(Field field) {
        super(field);
    }

    @Override
    public Object propertyValue(Object object) {
        Object fieldValue = Beans.readPropertyValue(object, field);
        if (fieldValue == null) {
            return null;
        }

        return new ResourceGenerator<>(field.getType(), fieldValue).generate();
    }
}

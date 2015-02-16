package org.egov.search.domain.resource;

import org.egov.search.service.ResourceGenerator;

import java.lang.reflect.Field;

public class NestedType extends Type {

    public NestedType(Field field) {
        super(field);
    }

    @Override
    public Object retrievePropertyValue(Object fieldValue) {
        return new ResourceGenerator<>(fieldValue.getClass(), fieldValue, false).generate();
    }
}

package org.egov.search.service;

import java.lang.reflect.Field;

public class SimpleType extends Type {

    public SimpleType(Field field) {
        super(field);
    }

    @Override
    public Object retrievePropertyValue(Object fieldValue) {
        return fieldValue;
    }

}

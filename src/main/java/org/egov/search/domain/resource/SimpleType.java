package org.egov.search.domain.resource;

import java.lang.reflect.Field;

public class SimpleType extends Type {

    public SimpleType(Field field) {
        super(field);
    }

    @Override
    public Object retrieveJsonValue(Object fieldValue) {
        return fieldValue;
    }

}

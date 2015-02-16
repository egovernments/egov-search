package org.egov.search.service;

import org.egov.search.util.Beans;

import java.lang.reflect.Field;

public class SimpleType extends Type {

    public SimpleType(Field field) {
        super(field);
    }

    @Override
    public Object propertyValue(Object object) {
        return Beans.readPropertyValue(object, field);
    }

}

package org.egov.search.service;

import org.egov.search.domain.Searchable;
import org.egov.search.util.Beans;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public abstract class Type {

    protected Field field;

    public Type(Field field) {
        this.field = field;
    }

    public static Type newInstanceFor(Field field) {
        Class<?> fieldClazz = field.getType();

        if (Collection.class.isAssignableFrom(fieldClazz)) {
            return new CollectionType(field);
        }

        if (Map.class.isAssignableFrom(fieldClazz)) {
            return new MapType(field);
        }

        Searchable searchable = fieldClazz.getAnnotation(Searchable.class);
        if (searchable != null) {
            return new NestedType(field);
        }

        return new SimpleType(field);
    }

    public Object propertyValue(Object object) {
        Object fieldValue = Beans.readPropertyValue(object, field);
        if (fieldValue == null) {
            return null;
        }
        return retrievePropertyValue(fieldValue);

    }

    public abstract Object retrievePropertyValue(Object fieldValue);

    protected boolean isSearchable(Object object) {
        return object != null && object.getClass().getAnnotation(Searchable.class) != null;
    }

}

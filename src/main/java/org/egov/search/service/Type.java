package org.egov.search.service;

import org.egov.search.domain.Searchable;

import java.lang.reflect.Field;
import java.util.Collection;

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

        Searchable searchable = fieldClazz.getAnnotation(Searchable.class);
        if (searchable != null) {
            return new NestedType(field);
        }

        return new SimpleType(field);
    }

    public abstract Object propertyValue(Object object);

    protected boolean isSearchable(Object object) {
        return object != null && object.getClass().getAnnotation(Searchable.class) != null;
    }

}

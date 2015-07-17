package org.egov.search.domain.resource;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.egov.search.domain.Searchable;
import org.egov.search.util.Beans;

public abstract class Type {

    protected Field field;

    public Type(Field field) {
        this.field = field;
    }

    public static Type newInstanceFor(Field field, Class<?> fieldClazz) {

        if (Collection.class.isAssignableFrom(fieldClazz)) {
            return new CollectionType(field);
        }

        if (Map.class.isAssignableFrom(fieldClazz)) {
            return new MapType(field);
        }
        if(Date.class.isAssignableFrom(fieldClazz)) {
        	return new DateType(field);
        }
        
        Searchable searchable = fieldClazz.getAnnotation(Searchable.class);
        if (searchable != null) {
            return new NestedType(field);
        }

        return new SimpleType(field);
    }

    public Object jsonValue(Object object) {
        Object fieldValue = Beans.readPropertyValue(object, field);
        return retrieveJsonValue(fieldValue);
    }

    protected abstract Object retrieveJsonValue(Object fieldValue);

    protected boolean isSearchable(Object object) {
        return object != null && object.getClass().getAnnotation(Searchable.class) != null;
    }

}

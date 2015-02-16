package org.egov.search.service;

import net.minidev.json.JSONArray;
import org.egov.search.util.Beans;

import java.lang.reflect.Field;
import java.util.Collection;

public class CollectionType extends Type {
    public CollectionType(Field field) {
        super(field);
    }

    @Override
    public Object propertyValue(Object object) {
        Object fieldValue = Beans.readPropertyValue(object, field);
        if (fieldValue == null) {
            return null;
        }

        JSONArray jsonArray = new JSONArray();
        Collection collection = (Collection) fieldValue;

        collection.stream().forEach(entry -> {
            if(isSearchable(entry))
                jsonArray.add(new ResourceGenerator<>(entry.getClass(), entry).generate());
            else
                jsonArray.add(entry);
        });
        return jsonArray;
    }
}

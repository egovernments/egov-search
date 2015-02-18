package org.egov.search.domain.resource;

import org.egov.search.service.ResourceGenerator;
import org.json.simple.JSONArray;

import java.lang.reflect.Field;
import java.util.Collection;

public class CollectionType extends Type {
    public CollectionType(Field field) {
        super(field);
    }

    @Override
    public Object retrieveJsonValue(Object fieldValue) {
        JSONArray jsonArray = new JSONArray();
        Collection collection = (Collection) fieldValue;

        collection.stream().forEach(item -> {
            if(isSearchable(item))
                jsonArray.add(new ResourceGenerator<>(item.getClass(), item, false).generate());
            else
                jsonArray.add(item);
        });
        return jsonArray;
    }
}

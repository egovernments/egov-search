package org.egov.search.domain.resource;

import net.minidev.json.JSONArray;
import org.egov.search.service.ResourceGenerator;

import java.lang.reflect.Field;
import java.util.Collection;

public class CollectionType extends Type {
    public CollectionType(Field field) {
        super(field);
    }

    @Override
    public Object retrievePropertyValue(Object fieldValue) {
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

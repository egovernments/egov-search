package org.egov.search.service;

import org.apache.commons.lang.StringUtils;
import org.egov.search.domain.Searchable;
import org.egov.search.domain.resource.Type;
import org.egov.search.util.Beans;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

public class ResourceGenerator<T> {
    private final Class<T> clazz;
    private final Object object;
    private boolean rootLevel;

    public ResourceGenerator(Class<T> clazz, Object object) {
        this(clazz, object, true);
    }

    public ResourceGenerator(Class<T> clazz, Object object, boolean rootLevel) {
        this.clazz = clazz;
        this.object = object;
        this.rootLevel = rootLevel;
    }

    public JSONObject generate() {
        JSONObject jsonObject = new JSONObject();
        List<Field> fields = Beans.findAllDeclaredFields(clazz);
        fields.stream()
                .filter(this::isSearchableField)
                .forEach(field -> addFieldToJson(jsonObject, field));

        return jsonObject;
    }

    private void addFieldToJson(JSONObject jsonObject, Field field) {
        Searchable searchable = searchableAnnotation(field);
        String fieldName = fieldName(field, searchable);

        Object fieldValue = Type.newInstanceFor(field).propertyValue(object);

        if (fieldValue == null) {
            return;
        }

        if(rootLevel) {
            searchable.group().addFieldToJson(jsonObject, fieldName, fieldValue);
        } else {
            jsonObject.put(fieldName, fieldValue);
        }
    }

    private String fieldName(Field field, Searchable searchable) {
        String fieldName = field.getName();

        if (StringUtils.isNotEmpty(searchable.name())) {
            fieldName = searchable.name();
        }
        return fieldName;
    }

    private boolean isSearchableField(Field field) {
        return searchableAnnotation(field) != null;
    }

    private Searchable searchableAnnotation(Field field) {
        return field.getAnnotation(Searchable.class);
    }
}

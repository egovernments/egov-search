package org.egov.search.service;

import org.apache.commons.lang.StringUtils;
import org.egov.search.domain.Searchable;
import org.egov.search.util.Beans;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

import static org.egov.search.util.Beans.readPropertyValue;

public class ResourceGenerator<T> {
    private final Class<T> clazz;
    private final Object object;

    public ResourceGenerator(Class<T> clazz, Object object) {
        this.clazz = clazz;
        this.object = object;
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

        Object fieldValue = readPropertyValue(object, field);
        if(fieldValue == null) return;

        if (searchable.nested()) {
            fieldValue = nestedPropertyValue(fieldValue, field);
        }

        jsonObject.put(fieldName, fieldValue);
    }

    private String fieldName(Field field, Searchable searchable) {
        String fieldName = field.getName();

        if (StringUtils.isNotEmpty(searchable.name())) {
            fieldName = searchable.name();
        }
        return fieldName;
    }

    private Object nestedPropertyValue(Object object, Field field) {
        return new ResourceGenerator<>(field.getType(), object).generate();
    }

    private boolean isSearchableField(Field field) {
        return searchableAnnotation(field) != null;
    }

    private Searchable searchableAnnotation(Field field) {
        return field.getAnnotation(Searchable.class);
    }
}

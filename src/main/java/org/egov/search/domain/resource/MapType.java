package org.egov.search.domain.resource;

import org.egov.search.service.ResourceGenerator;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.Map;

public class MapType extends Type {
    public MapType(Field field) {
        super(field);
    }

    @Override
    public Object retrieveJsonValue(Object fieldValue) {
        JSONObject json = new JSONObject();
        Map map = (Map) fieldValue;

        map.entrySet().stream().forEach(o -> {
            Map.Entry entry = (Map.Entry) o;
            Object value = entry.getValue();
            if (!isSearchable(value)) {
                json.put(entry.getKey(), value);
            } else {
                json.put(entry.getKey(), new ResourceGenerator<>(value.getClass(), value, false).generate());
            }

        });
        return json;
    }
}

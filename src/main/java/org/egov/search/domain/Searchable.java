package org.egov.search.domain;

import org.json.simple.JSONObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Searchable {

    String name() default "";

    Group group() default Group.SEARCHABLE;

    enum Group {
        SEARCHABLE, CLAUSES, COMMON;

        public void addFieldToJson(JSONObject rootJson, String fieldName, Object fieldValue) {
            if (rootJson.containsKey(groupName())) {
                JSONObject group = (JSONObject) rootJson.get(groupName());
                group.put(fieldName, fieldValue);
            }
            else {
                JSONObject group = new JSONObject();
                group.put(fieldName, fieldValue);
                rootJson.put(groupName(), group);
            }
        }

        private String groupName() {
            return this.name().toLowerCase();
        }
    }
}

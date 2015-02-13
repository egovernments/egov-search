package org.egov.search.util;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Beans {

    public static List<Field> findAllDeclaredFields(Class aClass) {
        List<Field> fields = new ArrayList<Field>();
        return collectDeclaredFields(aClass, fields);
    }

    public static Object readPropertyValue(Object object, Field field) {
        try {
            return PropertyUtils.getProperty(object, field.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Field> collectDeclaredFields(Class aClass, List<Field> fields) {
        if (aClass == null) return fields;

        fields.addAll(Arrays.asList(aClass.getDeclaredFields()));
        return collectDeclaredFields(aClass.getSuperclass(), fields);
    }

}
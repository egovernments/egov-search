package org.egov.search.domain.resource;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

public class DateType extends Type {

    public DateType(Field field) {
        super(field);
    }

    @Override
    public Object retrieveJsonValue(Object fieldValue) {
    	Calendar oldcl = Calendar.getInstance();
		oldcl.setTime((Date)fieldValue);
		
		Date now = new Date();
		Calendar newcl = Calendar.getInstance();
		
		newcl.setTime(now);
		newcl.set(Calendar.DATE,oldcl.get(Calendar.DATE));
		newcl.set(Calendar.MONTH, oldcl.get(Calendar.MONTH));
		newcl.set(Calendar.YEAR, oldcl.get(Calendar.YEAR));
		
		return newcl.getTime();
    }

}

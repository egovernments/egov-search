package org.egov.search.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class JsonMapper extends ObjectMapper {
	
	private static final long serialVersionUID = 1L;
	
    public JsonMapper() {
        this.setSerializationInclusion(Include.NON_NULL);
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        this.registerModule(new JodaModule());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm\'Z\'");
        this.setDateFormat(dateFormat);
        this.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
    }
}

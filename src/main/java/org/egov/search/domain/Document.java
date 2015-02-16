package org.egov.search.domain;

import org.egov.search.util.Serializer;
import org.json.simple.JSONObject;

public class Document {
    private String index;
    private String type;
    private String correlationId;
    private JSONObject resource;

    Document() {
    }

    public Document(String index, String type, String correlationId, JSONObject resource) {
        this.index = index;
        this.type = type;
        this.correlationId = correlationId;
        this.resource = resource;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public JSONObject getResource() {
        return resource;
    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public static Document fromJson(String json) {
        return Serializer.fromJson(json, Document.class);
    }

    public String toJSONString() {
        return Serializer.toJson(this);
    }
}

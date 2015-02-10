package org.egov.search.domain;

import org.egov.search.util.Serializer;
import org.json.simple.JSONObject;

public class Document {
    private String correlationId;
    private JSONObject resource;

    Document() {
    }

    public Document(String correlationId, JSONObject resource) {
        this.correlationId = correlationId;
        this.resource = resource;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public JSONObject getResource() {
        return resource;
    }

    public static Document fromJson(String json) {
        return Serializer.fromJson(json, Document.class);
    }

    public String toJson() {
        return Serializer.toJson(this);
    }
}

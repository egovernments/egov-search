package org.egov.search.service;

import com.jayway.jsonpath.JsonPath;
import org.egov.search.domain.Document;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchResultTransformer {

    public List<Document> transform(String response) {
        List<Document> documents = new ArrayList<>();

        List<Map> hits = JsonPath.read(response, "$.hits.hits[*]");

        for (Map hit : hits) {
            String id = (String) hit.get("_id");
            String index = (String) hit.get("_index");
            String type = (String) hit.get("_type");
            Map source = (Map) hit.get("_source");

            documents.add(new Document(index, type, id, new JSONObject(source)));
        }

        return documents;
    }
}

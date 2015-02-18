package org.egov.search.service;

import org.egov.search.domain.Document;
import org.egov.search.util.Serializer;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchResultTransformer {

    public List<Document> transform(String response) {
        List<Document> documents = new ArrayList<>();
        JSONObject responseJson = Serializer.fromJson(response, JSONObject.class);

        List hits = (List) ((Map) responseJson.get("hits")).get("hits");
        for (Object hit : hits) {
            Map hitMap = (Map) hit;

            String id = (String) hitMap.get("_id");
            String index = (String) hitMap.get("_index");
            String type = (String) hitMap.get("_type");
            Map source = (Map) hitMap.get("_source");

            documents.add(new Document(index, type, id, new JSONObject(source)));
        }
        return documents;
    }
}

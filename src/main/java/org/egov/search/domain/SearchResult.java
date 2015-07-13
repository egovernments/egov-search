package org.egov.search.domain;

import org.egov.search.service.SearchResultTransformer;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private String response;
    private List<Document> documents;

    SearchResult(String response, List<Document> documents) {
        this.response = response;
        this.documents = documents;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public String rawResponse() {
        return response;
    }

    public boolean isEmpty() {
        return documents == null || documents.size() == 0;
    }

    public int documentCount() {
        if (isEmpty()) return 0;
        return getDocuments().size();
    }

    public static SearchResult from(String response) {
        List<Document> documentList = new ArrayList<Document>(0);
        if(response!=null && !"".equals(response)){
        	documentList =	new SearchResultTransformer().transform(response);
        }
        return new SearchResult(response, documentList);
    }
}

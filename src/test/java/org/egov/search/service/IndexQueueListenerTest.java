package org.egov.search.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.egov.search.domain.Document;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class IndexQueueListenerTest {
    @Mock
    private ElasticSearchClient esIndexClient;
    @Mock
    private TextMessage message;
    @Mock
    private Session session;

    private IndexQueueListener indexQueueListener;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        indexQueueListener = new IndexQueueListener(esIndexClient);
    }

    @Test
    public void shouldDeserializeInToDocument() throws JMSException {
        HashMap<String, String> resource = new HashMap<String, String>() {{
            put("name", "john");
        }};
        String indexName = "index-name";
        String indexType = "complaint";

        Document document = new Document(indexName, indexType, "COR123", new JSONObject(resource));

        when(message.getText()).thenReturn(document.toJSONString());
        when(message.getStringProperty("index")).thenReturn(indexName);
        when(message.getStringProperty("type")).thenReturn(indexType);

        indexQueueListener.onMessage(message,session);

        verify(esIndexClient).index(indexName, indexType, "COR123", document.getResource().toJSONString());
    }

}
package org.egov.search.service;

import org.egov.search.service.ElasticSearchClient;
import org.egov.search.ResourceType;
import org.egov.search.domain.Document;
import org.egov.search.service.IndexQueueListener;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.HashMap;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IndexQueueListenerTest {
    @Mock
    private ElasticSearchClient esIndexClient;
    @Mock
    private TextMessage message;

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
        Document document = new Document("COR123", new JSONObject(resource));
        String indexName = "index-name";
        String indexType = ResourceType.COMPLAINT_TYPE.indexType();

        when(message.getText()).thenReturn(document.toJson());
        when(message.getStringProperty("index")).thenReturn(indexName);
        when(message.getStringProperty("type")).thenReturn(indexType);

        indexQueueListener.onMessage(message);

        verify(esIndexClient).index("COR123", document.toJson(), indexName, indexType);
    }

}
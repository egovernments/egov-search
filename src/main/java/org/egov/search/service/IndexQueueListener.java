package org.egov.search.service;

import org.egov.search.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
@Transactional
public class IndexQueueListener implements MessageListener {

    private ElasticSearchClient esIndexClient;

    @Autowired
    public IndexQueueListener(ElasticSearchClient esIndexClient) {
        this.esIndexClient = esIndexClient;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String documentMessage = ((TextMessage) message).getText();
            Document doc = Document.fromJson(documentMessage);
            esIndexClient.index(doc.getIndex(), doc.getType(), doc.getCorrelationId(), doc.getResource().toJSONString());
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}

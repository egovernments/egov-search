package org.egov.search.service;

import org.egov.search.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.TextMessage;

@Service
public class IndexService {

    private JmsTemplate jmsTemplate;
    private Destination indexQueue;

    @Autowired
    public IndexService(JmsTemplate jmsTemplate, Destination indexQueue) {
        this.jmsTemplate = jmsTemplate;
        this.indexQueue = indexQueue;
    }

    public void index(String indexName, String indexType, Document document) {
        jmsTemplate.send(indexQueue, session -> {
            TextMessage textMessage = session.createTextMessage(document.toJson());
            textMessage.setStringProperty("index", indexName);
            textMessage.setStringProperty("type", indexType);
            return textMessage;
        });
    }
}

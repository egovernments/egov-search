package org.egov.search;

import org.egov.search.config.SearchConfig;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Arrays.asList;

@Configuration
public class SearchBeans {

    @Bean
    @Autowired
    public Client transportClient(SearchConfig searchConfig) {
        ImmutableSettings.Builder settingsBuilder = ImmutableSettings.builder();
        settingsBuilder.put("cluster.name", searchConfig.searchClusterName());

        TransportClient client = new TransportClient(settingsBuilder);
        asList(searchConfig.searchHosts()).stream().forEach(host -> addTransportClient(client, host, searchConfig.searchPort()));

        return client;
    }

    private TransportClient addTransportClient(Client client, String host, int port) {
        return ((TransportClient) client).addTransportAddress(
                new InetSocketTransportAddress(host, port));
    }

}

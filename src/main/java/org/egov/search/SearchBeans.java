package org.egov.search;

import java.net.InetSocketAddress;

import org.egov.search.config.SearchConfig;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchBeans {

    @Bean
    @Autowired
    public Client transportClient(SearchConfig searchConfig) {
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name",searchConfig.searchClusterName()).build();
        Client client = TransportClient.builder().settings(settings).build();
        searchConfig.searchHosts().stream().forEach(host -> addTransportClient(client, host, searchConfig.searchPort()));
        return client;
    }

    private TransportClient addTransportClient(Client client, String host, int port) {
            return ((TransportClient) client).addTransportAddress(
                    new InetSocketTransportAddress(new InetSocketAddress(host, port)));
    }

}

package org.egov.search.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class SearchConfig {

    private Properties properties;

    @Autowired
    public SearchConfig(@Qualifier("egovSearchProperties") Properties properties) {
        this.properties = properties;
    }

    public String[] searchHosts() {
        return properties.getProperty("search.hosts").split(",");
    }

    public int searchPort() {
        return Integer.parseInt(properties.getProperty("search.port"));
    }

    public String searchClusterName() {
        return properties.getProperty("search.clusterName");
    }

    public int searchShardsFor(String indexName) {
        return Integer.parseInt(properties.getProperty(String.format("search.%s.shards", indexName)));
    }

    public int searchReplicasFor(String indexName) {
        return Integer.parseInt(properties.getProperty(String.format("search.%s.replicas", indexName)));
    }
}

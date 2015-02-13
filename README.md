EGovernments Search Module
----

Provides free text search with filters, sorting and pagination support. Uses elasticsearch as a search engine and queues to provide asynchronous support while indexing.

## Features

* Index API to index data in to elasticsearch
* Search API to search with:
    - Query String
    - Filters
    - Sorting & Pagination
    - Facets (Coming soon)

## Building

Run the following command from your checkout directory

```bash
mvn clean install  ## Will generate a egov-search-1.0-SNAPHOST.jar in your build (target) directory
```

## Prerequisites

* [Elasticsearch >= 1.4.2](https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-1.4.2.zip)
* Uses spring to manage objects lifecycle

## Usage

#### Indexing

```java
indexService.index("pgr", "complaint", new Document("CMP123", complaintJson));

// "pgr" - Index Name
// "complaint" - Index Type Name
// "complaintJson" - Complaint Entity in Json string format
```

#### Searching	

```java
SearchResult result = searchService.search(asList("pgr"), asList("complaint"), Filters.andFilters(filterMap), Sort.NULL, Page.NULL);
List<Document> documents = result.getDocuments();
String complaintJson = documents.first().getResource();
```

###### Refer the following tests to understand more search scenarios:

* [Searching with Filters](http://git.egovernments.org/projects/EES/repos/egov-search/browse/src/test/java/org/egov/search/service/SearchServiceMultipleFiltersTest.java)
* [Searching with Sort](http://git.egovernments.org/projects/EES/repos/egov-search/browse/src/test/java/org/egov/search/service/SearchServiceSortTest.java)
* [Searching with Pagination](http://git.egovernments.org/projects/EES/repos/egov-search/browse/src/test/java/org/egov/search/service/SearchServicePaginationTest.java)

#### Integrating with your application

* Add egov-search-[version].jar to your application dependencies

```xml
<dependency>
    <groupId>org.egov.search</groupId>
    <artifactId>egov-search</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

* Import [applcationContext-search-all.xml](http://git.egovernments.org/projects/EES/repos/egov-search/browse/src/main/resources/config/spring/applicationContext-search-all.xml) in to your application context

```xml
<import resource="classpath*:config/spring/applicationContext-search-all.xml" />
```

* Your JBoss server should have JMS module enabled and your comsumer application should have configured with spring beans:
    - __JMSConnectionFactory__ (bean-name: __cacheConnectionFactory__)
    - __TransactionManager__ (bean-name: __transactionManager__)  
    - __JMSDestinationReslver__ (bean-name: __jmsDestinationResolver__)

* Your JBoss server should have __Queue__ configured for indexing with JNDI __/jms/queue/searchindex__

#### Configuration

* Have a look at [Configuration](http://git.egovernments.org/projects/EES/repos/egov-search/browse/src/main/resources/config/egov-search.properties) to understand the defaults of the module.
* To override any of these config, have a file named __egov-search-override.properties' in your JBoss egov-settings module system.
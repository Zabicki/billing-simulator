package pl.zabicki.billing.elasticsearch.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.zabicki.billing.elasticsearch.model.Event;

import java.io.IOException;
import java.util.List;

@Repository
public class EventRepo {

    @Autowired
    ElasticsearchClient client;

    public List<Event> searchEvents(String clientId, String accountId) throws IOException {
        /*SearchResponse<Event> search = client.search(s -> s
                        .index("eventstore")
                        .query(q -> q
                                .term(t -> t
                                        .field("clientId")
                                        .value(v -> v.stringValue(clientId))
                                        .field("accountId")
                                        .value(v -> v.stringValue(accountId))
                                )).size(10_000),
                Event.class);*/

        SearchResponse<Event> search = client.search(s -> s
                .index("eventstore")
                .query(q -> q
                        .bool(b -> b
                                .filter(f -> f
                                        .match(m -> m
                                                //.field("clientId")
                                                //.query(clientId)
                                                .field("accountId")
                                                .query(accountId)))
                        )
                ).size(10_000),
                Event.class);

       /* new SearchRequest.Builder()
                .index("eventstore")
                .query(new BoolQuery.Builder().must()
                );*/

        return search.hits().hits().stream().map(Hit::source).toList();
    }

    public void saveEvents(List<Event> events) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (Event event : events) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index("eventstore")
                            .id(event.getId())
                            .document(event)));
        }
        BulkResponse response = client.bulk(br.build());
        System.out.println("test");
    }

    /*public void createIndex() {
        CreateIndexRequest request = new CreateIndexRequest.Builder()
                .index("eventstore")
                .settings(new IndexSettings.Builder()
                        .numberOfReplicas("0")
                        .numberOfShards("1")
                        .build())
                .mappings(TypeMapping.of(new TypeMapping.Builder()))
                .build();

        request.mapping(getMapping(), XContentType.JSON);
        CreateIndexResponse createIndexResponse = client.indices().create(request);
    }

    private void createMapping() {
        RootObjectMapper
    }

    private String getMapping() {
        return """
                {
                  "properties": {
                    "id": {
                      "type": "keyword"
                    },
                    "clientId": {
                      "type": "keyword"
                    },
                    "accountId": {
                      "type": "keyword"
                    },
                    "apInstanceId": {
                      "type": "text"
                    },
                    "callingNumber": {
                      "type": "text"
                    },
                    "calledNumber": {
                      "type": "text"
                    },
                    "callingPrefix": {
                      "type": "text"
                    },
                    "calledPrefix": {
                      "type": "text"
                    },
                    "eventBeginDate": {
                      "type": "text"
                    },
                    "eventEndDate": {
                      "type": "text"
                    },
                    "productId": {
                      "type": "text"
                    },
                    "rootProductId": {
                      "type": "text"
                    },
                    "intProperty1": {
                      "type": "long"
                    },
                    "intProperty2": {
                      "type": "long"
                    },
                    "intProperty3": {
                      "type": "long"
                    },
                    "intProperty4": {
                      "type": "long"
                    },
                    "intProperty5": {
                      "type": "long"
                    },
                    "stringProperty1": {
                      "type": "text"
                    },
                    "stringProperty2": {
                      "type": "text"
                    },
                    "stringProperty3": {
                      "type": "text"
                    },
                    "stringProperty4": {
                      "type": "text"
                    },
                    "stringProperty5": {
                      "type": "text"
                    },
                    "booleanProperty1": {
                      "type": "boolean"
                    },
                    "booleanProperty2": {
                      "type": "boolean"
                    },
                    "booleanProperty3": {
                      "type": "boolean"
                    },
                    "booleanProperty4": {
                      "type": "boolean"
                    },
                    "booleanProperty5": {
                      "type": "boolean"
                    },
                    "quantity": {
                      "type": "long"
                    },
                    "billingCycleDefId": {
                      "type": "keyword"
                    },
                    "billingCycleInstanceId": {
                      "type": "keyword"
                    },
                    "unit": {
                      "type": "text"
                    },
                    "billingProviderId": {
                      "type": "keyword"
                    }
                  }
                }
                """;
    }*/
}

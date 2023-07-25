package pl.zabicki.billing.elasticsearch.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.zabicki.billing.elasticsearch.model.Event;

import java.io.IOException;
import java.util.List;

@Repository
@Slf4j
public class EventRepo {

    @Autowired
    ElasticsearchClient client;

    public List<Event> searchEvents(String clientId, String accountId) throws IOException {
        SearchResponse<Event> search = client.search(s -> s
                .index("eventstore")
                .query(q -> q
                        .bool(b -> b
                                .filter(f -> f
                                        .match(m -> m
                                                .field("clientId")
                                                .query(clientId)
                                                .field("accountId")
                                                .query(accountId)))
                        )
                ).size(10_000),
                Event.class);

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
    }

    public long countEvents() {
        try {
            return client.count(new CountRequest.Builder()
                            .index("eventstore")
                            .build())
                    .count();
        } catch (IOException e) {
            log.error(e.getMessage());
            return 0;
        }
    }
}

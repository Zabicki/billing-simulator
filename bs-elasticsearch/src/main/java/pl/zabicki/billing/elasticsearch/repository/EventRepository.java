package pl.zabicki.billing.elasticsearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.annotations.Routing;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import pl.zabicki.billing.elasticsearch.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends ElasticsearchRepository<Event, String> {

    //@Query("{\"bool\": {\"must\": [{\"term\": {\"clientId\": \"?0\"}}, {\"term\": {\"accountId\": \"?1\"}}]}}")
    Page<Event> findByClientIdAndAccountId(String clientId, String accountId, Pageable pageable);

    @Query("{\"match_all\": {}}")
    List<Event> findAllEvents();

    void deleteAll();

    /*private final ElasticsearchClient client;

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
                ).size(10_000), //TODO add pagination instead of limiting the size
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

    public void truncate() {

    }*/
}

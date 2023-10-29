package pl.zabicki.billing.elasticsearch.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;
import pl.zabicki.billing.elasticsearch.model.Event;

import java.util.HashMap;
import java.util.Map;

/*@Component
@Slf4j
@RequiredArgsConstructor
public class ESInitializer {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @PostConstruct
    public void createIndexWithSettings() {
        log.info("Creating index");
        Map<String, Object> settings = new HashMap<>();
        settings.put("number_of_shards", 3);
        settings.put("number_of_replicas", 2);

        IndexCoordinates indexCoordinates = IndexCoordinates.of("event");
        IndexOperations indexOperations = elasticsearchOperations.indexOps(indexCoordinates);

        if (!indexOperations.exists()) {
            indexOperations.create(settings);  // Create index with settings
            indexOperations.putMapping(indexOperations.createMapping(Event.class));  // Put mapping
        }
    }
}*/

package pl.zabicki.billing.cassandra.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zabicki.billing.cassandra.EventConverter;
import pl.zabicki.billing.cassandra.model.Event;
import pl.zabicki.billing.cassandra.repository.EventRepository;
import pl.zabicki.billing.core.data.model.CsvEvent;
import pl.zabicki.billing.core.data.reader.DataReader;
import pl.zabicki.billing.core.data.reader.EventReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class SynchronizationService {

    @Autowired
    EventRepository repo;
    DataReader<CsvEvent> reader = new EventReader();

    public long synchronize() throws IOException {
        List<CsvEvent> records = reader.readData("data/test.csv");
        List<Event> events = EventConverter.convertEvents(records);
        int batchSize = 1000;
        List<Event> batch = new ArrayList<>( batchSize);

        long totalStart = System.currentTimeMillis();
        int iterations = events.size() / batchSize;
        for (int i = 0; i < iterations; i++) {
            batch.addAll(events.subList(batchSize * i, batchSize * i + batchSize));
            repo.saveAll(batch);
            batch = new ArrayList<>( batchSize);
        }
        /*for (int i = 0; i < events.size(); i++) {
            batch.add(events.get(i));
            if (i % 1000 == 0) {
                repo.saveAll(batch);
                batch = new LinkedList<>();
            }
        }*/
        repo.saveAll(batch);
        long totalStop = System.currentTimeMillis();
        return totalStop - totalStart;
    }
}

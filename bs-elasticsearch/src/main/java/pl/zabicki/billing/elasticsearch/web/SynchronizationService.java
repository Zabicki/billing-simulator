package pl.zabicki.billing.elasticsearch.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zabicki.billing.core.data.model.CsvEvent;
import pl.zabicki.billing.core.data.reader.DataReader;
import pl.zabicki.billing.core.data.reader.EventReader;
import pl.zabicki.billing.elasticsearch.EventConverter;
import pl.zabicki.billing.elasticsearch.repository.EventRepo;
import pl.zabicki.billing.elasticsearch.model.Event;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class SynchronizationService {

    @Autowired
    EventRepo repo;
    DataReader<CsvEvent> reader = new EventReader();

    public void synchronize() throws IOException {
        List<CsvEvent> records = reader.readData("data/test.csv");
        List<Event> events = EventConverter.convertEvents(records);
        List<Event> batch = new LinkedList<>();

        for (int i = 0; i < events.size(); i++) {
            batch.add(events.get(i));
            if (i % 1000 == 0) {
                repo.saveEvents(batch);
                batch = new LinkedList<>();
            }
        }
        repo.saveEvents(batch);
    }
}

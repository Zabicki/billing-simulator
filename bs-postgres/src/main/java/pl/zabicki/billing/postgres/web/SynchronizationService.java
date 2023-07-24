package pl.zabicki.billing.postgres.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zabicki.billing.core.data.model.CsvEvent;
import pl.zabicki.billing.core.data.reader.DataReader;
import pl.zabicki.billing.core.data.reader.EventReader;
import pl.zabicki.billing.postgres.EventConverter;
import pl.zabicki.billing.postgres.model.Event;
import pl.zabicki.billing.postgres.repository.EventRepository;

import java.io.IOException;
import java.util.List;

@Service
public class SynchronizationService {

    @Autowired
    EventRepository repo;
    DataReader<CsvEvent> reader = new EventReader();

    public long synchronize() throws IOException {
        List<CsvEvent> records = reader.readData("data/test.csv");
        List<Event> events = EventConverter.convertEvents(records);
        long totalStart = System.currentTimeMillis();
        repo.saveAll(events);
        long totalStop = System.currentTimeMillis();
        return totalStop - totalStart;
    }
}

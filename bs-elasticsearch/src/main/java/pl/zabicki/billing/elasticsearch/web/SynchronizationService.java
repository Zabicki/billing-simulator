package pl.zabicki.billing.elasticsearch.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zabicki.billing.core.data.model.CsvEvent;
import pl.zabicki.billing.core.data.reader.DataReader;
import pl.zabicki.billing.core.data.reader.EventReader;
import pl.zabicki.billing.core.service.BaseService;
import pl.zabicki.billing.elasticsearch.EventConverter;
import pl.zabicki.billing.elasticsearch.repository.EventRepository;
import pl.zabicki.billing.elasticsearch.model.Event;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SynchronizationService extends BaseService {

    private final EventRepository repo;
    DataReader<CsvEvent> reader = new EventReader();

    /*public long synchronize() throws IOException {
        List<CsvEvent> records = reader.readData("data/test.csv");
        List<Event> events = EventConverter.convertEvents(records);
        List<Event> batch = new LinkedList<>();

        long start = System.currentTimeMillis();
        for (int i = 0; i < events.size(); i++) {
            batch.add(events.get(i));
            if (i % 1000 == 0) {
                repo.saveAll(batch);
                batch = new LinkedList<>();
            }
        }
        repo.saveAll(batch);
        long stop = System.currentTimeMillis();
        return stop - start;
    }*/

    public void truncateEvents() {
        repo.deleteAll();
    }

    public long synchronize(String dataSet) throws IOException {
        List<Path> eventFiles = getEventFiles(dataSet);
        int batchSize = 1000;
        int threads = 8;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        long totalStart = System.currentTimeMillis();

        for (Path eventFile : eventFiles) {
            List<CsvEvent> records = reader.readData(eventFile.toString());
            List<Event> events = EventConverter.convertEvents(records);
            List<Future<?>> futures = new ArrayList<>();
            int iterations = events.size() / batchSize;

            for (int i = 0; i < iterations; i++) {
                final List<Event> batch = events.subList(batchSize * i, batchSize * i + batchSize);
                Future<?> future = executor.submit(() -> {
                    repo.saveAll(batch);
                });
                futures.add(future);
            }

            if (events.size() % batchSize != 0) {
                final List<Event> lastBatch = events.subList(batchSize * iterations, events.size());
                Future<?> future = executor.submit(() -> {
                    repo.saveAll(lastBatch);
                });
                futures.add(future);
            }

            // Wait for all tasks to complete before moving on
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    // Handle exception
                    e.printStackTrace();
                }
            }
        }
        long totalStop = System.currentTimeMillis();

        // Shutdown the executor
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        return totalStop - totalStart;
    }
}

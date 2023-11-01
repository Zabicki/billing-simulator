package pl.zabicki.billing.cassandra.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zabicki.billing.cassandra.EventConverter;
import pl.zabicki.billing.cassandra.model.Account;
import pl.zabicki.billing.cassandra.model.Event;
import pl.zabicki.billing.cassandra.repository.AccountRepository;
import pl.zabicki.billing.cassandra.repository.EventRepository;
import pl.zabicki.billing.core.data.model.CsvEvent;
import pl.zabicki.billing.core.data.reader.DataReader;
import pl.zabicki.billing.core.data.reader.EventReader;
import pl.zabicki.billing.core.generator.BaseEvent;
import pl.zabicki.billing.core.generator.ClientRequest;
import pl.zabicki.billing.core.generator.EventGenerator;
import pl.zabicki.billing.core.service.BaseService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public class SynchronizationService extends BaseService {

    @Autowired
    EventRepository eventRepo;
    @Autowired
    AccountRepository accountRepo;
    DataReader<CsvEvent> reader = new EventReader();

    public long synchronize(List<ClientRequest> clientRequests) {
        EventGenerator generator = new EventGenerator();

        List<EventGenerator.AccountInfo> accountInfos = generator.init(clientRequests);
        List<Account> accounts = accountInfos.stream()
                .map(Account::fromAccountInfo)
                .toList();

        accountRepo.saveAll(accounts);

        long totalSaveTime = 0;

        List<BaseEvent> events = generator.generate(10_000);
        while (!events.isEmpty()) {
            long start = System.currentTimeMillis();
            for (BaseEvent event : events) {
                eventRepo.save(EventConverter.convertEvent(event));
            }
            totalSaveTime += System.currentTimeMillis() - start;
            events = generator.generate(10_000);
        }

        return totalSaveTime;
    }


/*
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
                    eventRepo.saveAll(batch);
                });
                futures.add(future);
            }

            if (events.size() % batchSize != 0) {
                final List<Event> lastBatch = events.subList(batchSize * iterations, events.size());
                Future<?> future = executor.submit(() -> {
                    eventRepo.saveAll(lastBatch);
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
*/

    public void truncateEvents() {
        eventRepo.truncate();
    }
}

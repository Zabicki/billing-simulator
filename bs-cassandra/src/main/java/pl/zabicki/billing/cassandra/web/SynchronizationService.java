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
import java.util.LinkedList;
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

    public SyncStatistics synchronize(List<ClientRequest> clientRequests) {
        EventGenerator generator = new EventGenerator();
        List<Long> batchSyncTimes = new LinkedList<>();

        List<EventGenerator.AccountInfo> accountInfos = generator.init(clientRequests);
        List<Account> accounts = accountInfos.stream()
                .map(Account::fromAccountInfo)
                .toList();

        accountRepo.saveAll(accounts);

        ExecutorService executorService = Executors.newFixedThreadPool(8);
        List<Future<?>> futures = new ArrayList<>();

        long totalSaveTime = 0;

        List<BaseEvent> events = generator.generate(10_000);
        while (!events.isEmpty()) {
            long start = System.currentTimeMillis();
            int chunkSize = 1250;

            for (int i = 0; i < 8; i++) {
                final int fromIndex = i * chunkSize;
                final int toIndex = fromIndex + chunkSize;

                // Pass the sublist directly to the thread
                List<BaseEvent> finalEvents = events;
                Future<?> future = executorService.submit(() -> {
                    List<BaseEvent> eventsChunk = finalEvents.subList(fromIndex, toIndex);
                    for (BaseEvent event : eventsChunk) {
                        eventRepo.save(EventConverter.convertEvent(event));
                    }
                });
                futures.add(future);
            }

            // Wait for all tasks to complete
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    // Handle exceptions appropriately
                }
            }
            long batchSyncTime = System.currentTimeMillis() - start;
            futures.clear(); // Clear the futures list for the next batch

            totalSaveTime += batchSyncTime;
            batchSyncTimes.add(batchSyncTime);
            events = generator.generate(10_000);
        }

        // Shut down the executor service
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        return new SyncStatistics(totalSaveTime, batchSyncTimes);
    }

    public void truncateEvents() {
        eventRepo.truncate();
    }
}

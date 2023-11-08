package pl.zabicki.billing.elasticsearch.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.zabicki.billing.core.result.store.SimulationResult;
import pl.zabicki.billing.core.service.BaseService;
import pl.zabicki.billing.elasticsearch.model.Account;
import pl.zabicki.billing.elasticsearch.model.Event;
import pl.zabicki.billing.elasticsearch.repository.AccountRepository;
import pl.zabicki.billing.elasticsearch.repository.EventRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class InvoicingService extends BaseService {

    @Autowired
    EventRepository eventRepo;
    @Autowired
    AccountRepository accountRepo;

    public long startInvoicing() throws ExecutionException, InterruptedException {
        int threads = 8;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        long totalEvents = 0;

        long totalStart = System.currentTimeMillis();
        List<Account> accounts = StreamSupport.stream(accountRepo.findAll().spliterator(), false).toList();

        List<Event> result = Collections.synchronizedList(new LinkedList<>());

        List<Future<Void>> futures = new ArrayList<>();

        // Calculate the size of each sub-list
        int batchSize = (accounts.size() + threads - 1) / threads;


        for (int i = 0; i < threads; i++) {
            int start = i * batchSize;
            int end = Math.min(start + batchSize, accounts.size());
            if (start < end) {
                final List<Account> batch = accounts.subList(i * batchSize,
                        Math.min(accounts.size(), (i + 1) * batchSize));

                Future<Void> future = executor.submit(() -> {
                    for (Account acc : batch) {
                        Page<Event> eventPage;
                        do {
                            Pageable pageable = PageRequest.of(0, 10_000);
                            eventPage = eventRepo.findByClientIdAndAccountId(acc.getClientId(), acc.getAccountId(), pageable);
                            result.addAll(eventPage.getContent());
                        } while(!eventPage.isLast());
                    }
                    return null;
                });

                futures.add(future);
            }
        }

        for (Future<Void> future : futures) {
            future.get();
        }
        totalEvents += result.size();

        long totalStop = System.currentTimeMillis();

        // Shutdown the executor
        executor.shutdown();

        // Optionally print or return the time taken
        log.info("Number of accounts: " + accounts.size() +
                " Number of events: " + totalEvents +
                " Invoicing time: " + (totalStop - totalStart));

        return totalStop - totalStart;
    }

    public long countEvents() {
        return eventRepo.count();
    }
}

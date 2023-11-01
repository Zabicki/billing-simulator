package pl.zabicki.billing.postgres.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zabicki.billing.core.service.BaseService;
import pl.zabicki.billing.postgres.model.Account;
import pl.zabicki.billing.postgres.model.Event;
import pl.zabicki.billing.postgres.repository.AccountRepository;
import pl.zabicki.billing.postgres.repository.EventRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        List<Account> accounts = accountRepo.findAll();

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
                        result.addAll(eventRepo.findByClientIdAndAccountId(acc.getClientId(), acc.getAccountId()));
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

package pl.zabicki.billing.postgres.web;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    InvoicingService self;

    public long startInvoicing() throws ExecutionException, InterruptedException {
        int threads = 8;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        long totalStart = System.currentTimeMillis();
        List<Account> accounts = accountRepo.findAll();

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
                        self.findEvents(acc);
                    }
                    return null;
                });

                futures.add(future);
            }
        }

        for (Future<Void> future : futures) {
            future.get();
        }

        long totalStop = System.currentTimeMillis();

        // Shutdown the executor
        executor.shutdown();

        return totalStop - totalStart;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void findEvents(Account acc) {
        eventRepo.findByClientIdAndAccountId(acc.getClientId(), acc.getAccountId());
        entityManager.flush();
        entityManager.clear();
    }

    public long countEvents() {
        return eventRepo.count();
    }
}

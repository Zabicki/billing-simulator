package pl.zabicki.billing.postgres.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zabicki.billing.core.data.model.CsvAccount;
import pl.zabicki.billing.core.data.reader.AccountReader;
import pl.zabicki.billing.core.data.reader.DataReader;
import pl.zabicki.billing.core.service.BaseService;
import pl.zabicki.billing.postgres.model.Event;
import pl.zabicki.billing.postgres.repository.EventRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class InvoicingService extends BaseService {

    @Autowired
    EventRepository repo;
    DataReader<CsvAccount> reader = new AccountReader();

    public long startInvoicing(String dataSet) throws IOException, ExecutionException, InterruptedException {
        List<Path> accountFiles = getAccountFiles(dataSet);
        int threads = 8;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        long totalStart = System.currentTimeMillis();
        long totalAccounts = 0;
        long totalEvents = 0;

        for (Path file : accountFiles) {
            List<CsvAccount> accounts = reader.readData(file.toString());
            totalAccounts += accounts.size();

            // Use a thread-safe list to hold results
            List<Event> result = Collections.synchronizedList(new LinkedList<>());

            List<Future<Void>> futures = new ArrayList<>();

            // Calculate the size of each sub-list
            int batchSize = (accounts.size() + threads - 1) / threads;


            for (int i = 0; i < threads; i++) {
                final List<CsvAccount> batch = accounts.subList(i * batchSize,
                        Math.min(accounts.size(), (i + 1) * batchSize));

                Future<Void> future = executor.submit(() -> {
                    for (CsvAccount acc : batch) {
                        result.addAll(repo.findByClientIdAndAccountId(acc.clientId(), acc.accountId()));
                    }
                    return null;
                });

                futures.add(future);
            }

            for (Future<Void> future : futures) {
                future.get();
            }
            totalEvents += result.size();
        }

        long totalStop = System.currentTimeMillis();

        // Shutdown the executor
        executor.shutdown();

        // Optionally print or return the time taken
        log.info("Number of accounts: " + totalAccounts +
                " Number of events: " + totalEvents +
                " Invoicing time: " + (totalStop - totalStart));

        return totalStop - totalStart;
    }

    public long countEvents() {
        return repo.count();
    }
}

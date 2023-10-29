package pl.zabicki.billing.elasticsearch.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.zabicki.billing.core.data.model.CsvAccount;
import pl.zabicki.billing.core.data.reader.AccountReader;
import pl.zabicki.billing.core.data.reader.DataReader;
import pl.zabicki.billing.core.service.BaseService;
import pl.zabicki.billing.elasticsearch.model.Event;
import pl.zabicki.billing.elasticsearch.repository.EventRepository;

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

@Service
@Slf4j
public class InvoicingService extends BaseService {

    @Autowired
    EventRepository repo;
    DataReader<CsvAccount> reader = new AccountReader();

    /*public long startInvoicing() throws IOException {
        List<CsvAccount> accounts = reader.readData("data/accounts.csv");
        List<Event> result;
        long totalStart = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            result = new LinkedList<>();
            long start = System.currentTimeMillis();
            for (CsvAccount acc : accounts) {
                List<Event> res = repo.searchEvents(acc.clientId(), acc.accountId());
                result.addAll(res);
            }
            long stop = System.currentTimeMillis();
            System.out.println("Run " + i + " invoicing time: " + (stop - start));
        }
        long totalStop = System.currentTimeMillis();
        System.out.println("Total invoicing time: " + (totalStop - totalStart));
        return totalStop - totalStart;
    }*/

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
                        Page<Event> eventPage;
                        do {
                            Pageable pageable = PageRequest.of(0, 10_000);
                            eventPage = repo.findByClientIdAndAccountId(acc.clientId(), acc.accountId(), pageable);
                            result.addAll(eventPage.getContent());
                        } while(!eventPage.isLast());

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
        System.out.println("Total time: " + (totalStop - totalStart) + "ms");
        log.info("Number of accounts: " + totalAccounts +
                "\nNumber of events: " + totalEvents +
                "Invoicing time: " + (totalStop - totalStart));

        return totalStop - totalStart;
    }

    public long countEvents() {
        return repo.count();
    }
}

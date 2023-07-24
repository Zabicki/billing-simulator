package pl.zabicki.billing.elasticsearch.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zabicki.billing.core.data.model.CsvAccount;
import pl.zabicki.billing.core.data.reader.AccountReader;
import pl.zabicki.billing.core.data.reader.DataReader;
import pl.zabicki.billing.elasticsearch.model.Event;
import pl.zabicki.billing.elasticsearch.repository.EventRepo;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class InvoicingService {

    @Autowired
    EventRepo repo;
    //EventRepository repo;
    DataReader<CsvAccount> reader = new AccountReader();

    public void startInvoicing() throws IOException {
        List<CsvAccount> accounts = reader.readData("data/accounts.csv");
        List<Event> result;
        long totalStart = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            result = new LinkedList<>();
            long start = System.currentTimeMillis();
            for (CsvAccount acc : accounts) {
                result.addAll(repo.searchEvents(acc.clientId(), acc.accountId()));
            }
            long stop = System.currentTimeMillis();
            System.out.println("Run " + i + " invoicing time: " + (stop - start));
        }
        long totalStop = System.currentTimeMillis();

        System.out.println("Total invoicing time: " + (totalStop - totalStart));
    }
}

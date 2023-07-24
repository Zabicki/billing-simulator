package pl.zabicki.billing.cassandra.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zabicki.billing.cassandra.model.Event;
import pl.zabicki.billing.cassandra.model.EventPrimaryKey;
import pl.zabicki.billing.cassandra.repository.EventRepository;
import pl.zabicki.billing.core.data.model.CsvAccount;
import pl.zabicki.billing.core.data.reader.AccountReader;
import pl.zabicki.billing.core.data.reader.DataReader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class InvoicingService {

    @Autowired
    EventRepository repo;

    DataReader<CsvAccount> reader = new AccountReader();

    public void startInvoicing() throws IOException {
        List<CsvAccount> accounts = reader.readData("data/accounts.csv");
        List<Event> result;
        long totalStart = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            result = new LinkedList<>();
            long start = System.currentTimeMillis();
            for (CsvAccount acc : accounts) {
                result.addAll(repo.findByClientIdAndAccountId(acc.clientId(), acc.accountId()));
            }
            long stop = System.currentTimeMillis();
            System.out.println("Run " + i + " invoicing time: " + (stop - start));
        }
        long totalStop = System.currentTimeMillis();

        System.out.println("Total invoicing time: " + (totalStop - totalStart));
    }
}

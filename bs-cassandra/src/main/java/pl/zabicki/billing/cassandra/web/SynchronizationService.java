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

    public void truncateEvents() {
        eventRepo.truncate();
    }
}

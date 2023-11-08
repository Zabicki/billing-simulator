package pl.zabicki.billing.postgres.web;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.zabicki.billing.core.generator.BaseEvent;
import pl.zabicki.billing.core.generator.ClientRequest;
import pl.zabicki.billing.core.generator.EventGenerator;
import pl.zabicki.billing.core.service.BaseService;
import pl.zabicki.billing.postgres.EventConverter;
import pl.zabicki.billing.postgres.model.Account;
import pl.zabicki.billing.postgres.model.Event;
import pl.zabicki.billing.postgres.repository.AccountRepository;
import pl.zabicki.billing.postgres.repository.EventRepository;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class SynchronizationService extends BaseService {

    @Autowired
    EventRepository eventRepo;
    @Autowired
    AccountRepository accountRepo;

    @Autowired
    private SynchronizationService self;

    @PersistenceContext
    private EntityManager entityManager;

    public SyncStatistics synchronize(List<ClientRequest> clientRequests) {
        log.info("Max memory: " + Runtime.getRuntime().maxMemory());
        log.info("Total memory: " + Runtime.getRuntime().totalMemory());
        EventGenerator generator = new EventGenerator();
        List<Long> batchSyncTime = new LinkedList<>();

        List<EventGenerator.AccountInfo> accountInfos = generator.init(clientRequests);
        List<Account> accounts = accountInfos.stream()
                .map(Account::fromAccountInfo)
                .toList();
        self.saveAccounts(accounts);

        long totalSyncTime = 0;

        List<BaseEvent> events = generator.generate(10_000);
        while (!events.isEmpty()) {
            long transactionSyncTime = self.saveEventsBatch(EventConverter.convertEvents(events));

            totalSyncTime += transactionSyncTime;
            batchSyncTime.add(transactionSyncTime);

            events = generator.generate(10_000);
        }

        return new SyncStatistics(totalSyncTime, batchSyncTime);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAccounts(List<Account> accounts) {
        accountRepo.saveAll(accounts);
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long saveEventsBatch(List<Event> events) {
        long start = System.currentTimeMillis();

        eventRepo.saveAll(events);
        entityManager.flush();
        entityManager.clear();

        long stop = System.currentTimeMillis();
        return stop - start;
    }

    public void printSessionStatistics() {
        Session session = entityManager.unwrap(Session.class);
        Statistics stats = session.getSessionFactory().getStatistics();
        System.out.println("Insert count: " + stats.getEntityInsertCount());
        System.out.println("Load count: " + stats.getEntityLoadCount());
        System.out.println("Delete count: " + stats.getEntityDeleteCount());
        System.out.println("Fetch count: " + stats.getEntityFetchCount());
        System.out.println("Update count: " + stats.getEntityUpdateCount());
    }

    public void truncateTables() {
        eventRepo.truncate();
        accountRepo.truncate();
    }
}

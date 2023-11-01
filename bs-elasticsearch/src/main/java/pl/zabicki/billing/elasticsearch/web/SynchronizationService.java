package pl.zabicki.billing.elasticsearch.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zabicki.billing.core.generator.BaseEvent;
import pl.zabicki.billing.core.generator.ClientRequest;
import pl.zabicki.billing.core.generator.EventGenerator;
import pl.zabicki.billing.core.service.BaseService;
import pl.zabicki.billing.elasticsearch.EventConverter;
import pl.zabicki.billing.elasticsearch.model.Account;
import pl.zabicki.billing.elasticsearch.repository.AccountRepository;
import pl.zabicki.billing.elasticsearch.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SynchronizationService extends BaseService {

    private final EventRepository eventRepo;
    @Autowired
    AccountRepository accountRepo;

    public void truncateEvents() {
        eventRepo.deleteAll();
    }

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
}

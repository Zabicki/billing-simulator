package pl.zabicki.billing.core.generator;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EventGenerator {

    private final Random random = new Random();
    private String billCycleDefId = UUID.randomUUID().toString();
    private String billCycleInstanceId = UUID.randomUUID().toString();
    private String bProviderId = UUID.randomUUID().toString();

    private List<EventsForAccount> eventsForAccounts = new ArrayList<>();

    //500kk events
    /*public static void main(String[] args) throws IOException {
        new EventGenerator().generate(List.of(
                new Request(10_000_000, 1, 200), //200kk 2kkk
                new Request(100_000, 50, 200), //100kk 1kkk
                new Request(1000, 1_000, 2000) //200kk 2kkk
                ));
    }*/

    public List<AccountInfo> init(List<ClientRequest> requests) {
        List<AccountInfo> accountInfos = new ArrayList<>((int) requests.stream().mapToLong(r -> r.numOfClients() * r.numOfAccounts()).sum());
        for (ClientRequest request : requests) {
            for (int i = 0; i < request.numOfClients(); i++) {
                String clientId = UUID.randomUUID().toString();
                for (int j = 0; j < request.numOfAccounts(); j++) {
                    String accountId = UUID.randomUUID().toString();
                    accountInfos.add(new AccountInfo(clientId, accountId));
                    EventsForAccount eventsForAccount = new EventsForAccount(clientId, accountId, request.numOfEventsPerAccount(), 0);
                    eventsForAccounts.add(eventsForAccount);
                }
            }
        }

        return accountInfos;
    }

    public record AccountInfo(String clientId, String accountId) {

    }

    public List<BaseEvent> generate(int numOfEvents) {
        List<BaseEvent> result = new ArrayList<>(numOfEvents);

        for (int i = 0; i < numOfEvents; i++) {
            if (eventsForAccounts.isEmpty()) {
                break;
            }
            int randomIndex = random.nextInt(eventsForAccounts.size());

            EventsForAccount eventsForAccount = eventsForAccounts.get(randomIndex);
            eventsForAccount.current++;

            if (eventsForAccount.expected == eventsForAccount.current) {
                int lastIndex = eventsForAccounts.size() - 1;
                eventsForAccounts.set(randomIndex, eventsForAccounts.get(lastIndex));
                eventsForAccounts.remove(lastIndex);
            }

            result.add(BaseEvent.builder()
                    .clientId(eventsForAccount.getClientId())
                    .accountId(eventsForAccount.getAccountId())
                    .apInstanceId(UUID.randomUUID().toString())
                    .billingCycleDefId(billCycleDefId)
                    .billingCycleInstanceId(billCycleInstanceId)
                    .billingProviderId(bProviderId)
                    .calledNumber(randomNumber(100_000_000, 999_999_999))
                    .callingNumber(randomNumber(100_000_000, 999_999_999))
                    .calledPrefix(randomNumber(10, 99))
                    .callingPrefix(randomNumber(10, 99))
                    .eventBeginDate(randomDate())
                    .eventEndDate(randomDate())
                    .booleanProperty1(randomBool())
                    .booleanProperty2(randomBool())
                    .booleanProperty3(randomBool())
                    .booleanProperty4(randomBool())
                    .booleanProperty5(randomBool())
                    .intProperty1(randomLong())
                    .intProperty2(randomLong())
                    .intProperty3(randomLong())
                    .intProperty4(randomLong())
                    .intProperty5(randomLong())
                    .stringProperty1(randomString(5, 100))
                    .stringProperty2(randomString(5, 100))
                    .stringProperty3(randomString(5, 100))
                    .stringProperty4(randomString(5, 100))
                    .stringProperty5(randomString(5, 100))
                    .unit(randomString(2, 16))
                    .productId(UUID.randomUUID().toString())
                    .rootProductId(UUID.randomUUID().toString())
                    .quantity(Long.parseLong(randomNumber(1, 1000)))
                    .build());
        }

        return result;
    }

    private String randomNumber(int min, int max) {
        return String.valueOf(random.nextInt(min, max));
    }

    private String randomDate() {
        return Instant.ofEpochSecond(ThreadLocalRandom.current().nextInt()).toString();
    }

    private boolean randomBool() {
        return random.nextBoolean();
    }

    private long randomLong() {
        return random.nextLong();
    }

    private String randomString(int min, int max) {
        int length = random.nextInt(min, max);
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Data
    @AllArgsConstructor
    private static class EventsForAccount {
        private String clientId;
        private String accountId;
        private long expected;
        private long current;
    }
}

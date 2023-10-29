package pl.zabicki.billing.core.generator;

import pl.zabicki.billing.core.data.model.CsvEvent;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EventGenerator {

    Random random = new Random();
    //500kk events
    public static void main(String[] args) throws IOException {
        new EventGenerator().generate(List.of(
                new Request(10_000_000, 1, 200), //200kk 2kkk
                new Request(100_000, 50, 200), //100kk 1kkk
                new Request(1000, 1_000, 2000) //200kk 2kkk
                ));
    }

    /**
     * NOTE can generate up to 2 billion events (max int).
     * Generate events for clients and accounts specified by requests.
     * Saves events in csv file
     * Events will be inserted into tested database.
     * Also creates file with clients and accounts. Format for each row: clientId,accountId1,accountId2,...,accountIdn
     * @param requests generation requests specifying number of clients, accounts per clients and events per accounts
     *                 for 1 billing cycle
     */
    public void generate(List<Request> requests) throws IOException {
        Map<String, List<String>> clientIdToAccountIds = new HashMap<>();
        EventWriter eventWriter = new EventWriter();

        List<CsvEvent> resultEvents = new ArrayList<>(requests.stream()
                .mapToInt(r -> r.numOfAccounts() * r.numOfClients() * r.numOfEventsPerAccount())
                .sum());

        String billCycleDefId = UUID.randomUUID().toString();
        String billCycleInstanceId = UUID.randomUUID().toString();

        for (Request request : requests) {
            String bProviderId = UUID.randomUUID().toString();
            for (int i = 0; i < request.numOfClients(); i++) {
                String clientId = UUID.randomUUID().toString();
                for (int j = 0; j < request.numOfAccounts(); j++) {
                    String accountId = UUID.randomUUID().toString();
                    if (clientIdToAccountIds.containsKey(clientId)) {
                        List<String> accountIds = clientIdToAccountIds.get(clientId);
                        accountIds.add(accountId);
                        clientIdToAccountIds.put(clientId, accountIds);
                    } else {
                        List<String> accountIds = new LinkedList<>();
                        accountIds.add(accountId);
                        clientIdToAccountIds.put(clientId, accountIds);
                    }

                    for (int k = 0; k < request.numOfEventsPerAccount(); k++) {
                        String apInstanceId = UUID.randomUUID().toString();
                        String productId = UUID.randomUUID().toString();
                        String rootProductId = UUID.randomUUID().toString();
                        CsvEvent event = CsvEvent.builder()
                                .clientId(clientId)
                                .accountId(accountId)
                                .apInstanceId(apInstanceId)
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
                                .productId(productId)
                                .rootProductId(rootProductId)
                                .quantity(Long.parseLong(randomNumber(1, 1000)))
                                .build();

                        //printRecord(printer, event);
                        resultEvents.add(event);
                    }
                }
            }
        }

        // randomize the order of the events
        Collections.shuffle(resultEvents);
        eventWriter.writeEvents(resultEvents);
        eventWriter.writeAccounts(clientIdToAccountIds);
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
}

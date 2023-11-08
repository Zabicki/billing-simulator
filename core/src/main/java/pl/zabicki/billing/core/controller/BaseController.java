package pl.zabicki.billing.core.controller;

import pl.zabicki.billing.core.generator.ClientRequest;
import pl.zabicki.billing.core.result.store.ResultStore;

import java.util.List;

public class BaseController {

    protected final String PROCESSING_TIME = "processingTime";
    protected final String COUNT = "count";

    protected final String CURRENT_DATA = "202308262043";

    protected final ResultStore resultStore = new ResultStore("store.json");

    protected String wrapJson(String name, long val) {
        return String.format("""
                {
                    "%s": %s
                }
                """, name, val);
    }

    protected long toSeconds(long processingTimeMillis) {
        return processingTimeMillis / 1000;
    }

    protected record RequestStatistics(long clients, long accounts, long events) {

    }
    protected record SimulationRequest(String description, List<ClientRequest> clientRequests) {
        public RequestStatistics getStatistics() {
            long clients = 0;
            long accounts = 0;
            long events = 0;

            for (ClientRequest request : clientRequests) {
                clients += request.numOfClients();
                accounts += request.numOfClients() * request.numOfAccounts();
                events += request.numOfClients() * request.numOfAccounts() * request.numOfEventsPerAccount();
            }

            return new RequestStatistics(clients, accounts, events);
        }
    }
}

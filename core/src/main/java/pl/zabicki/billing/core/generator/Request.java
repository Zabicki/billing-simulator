package pl.zabicki.billing.core.generator;

/**
 * Request generation of n clients, each client has x accounts and each account has z events
 */
public record Request(int numOfClients, int numOfAccounts, int numOfEventsPerAccount) {
}

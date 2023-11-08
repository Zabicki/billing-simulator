package pl.zabicki.billing.core.service;


import java.util.List;

public class BaseService {

    public record SyncStatistics(long totalSyncTime, List<Long> batchSyncTime) {

    }
}

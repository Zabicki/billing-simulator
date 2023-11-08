package pl.zabicki.billing.core.result.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationResult {
    private long synchronizationTime;
    private long invoicingTime;
    private String description;
    private long clients;
    private long accounts;
    private long events;
    private List<Long> batchSyncTime;
}
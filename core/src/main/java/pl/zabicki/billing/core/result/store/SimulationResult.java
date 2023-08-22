package pl.zabicki.billing.core.result.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationResult {
    private long synchronizationTime;
    private long invoicingTime;
    private String description;
}
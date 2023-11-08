package pl.zabicki.billing.postgres.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.zabicki.billing.core.controller.BaseController;
import pl.zabicki.billing.core.generator.ClientRequest;
import pl.zabicki.billing.core.result.store.SimulationResult;
import pl.zabicki.billing.core.service.BaseService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/postgres/")
@Slf4j
public class BSController extends BaseController {

    @Autowired
    SynchronizationService synchronizationService;

    @Autowired
    InvoicingService invoicingService;

    @GetMapping(value = "invoicing")
    public String startInvoicing() throws IOException, ExecutionException, InterruptedException {
        log.info("Invoicing started");
        long processingTime = invoicingService.startInvoicing();
        log.info("Invoicing finished");
        return wrapJson(PROCESSING_TIME, toSeconds(processingTime));
    }

    @PostMapping(value = "synchronization")
    public String startEventSynchronization(@RequestBody List<ClientRequest> clientRequests) {
        log.info("Synchronization started");
        BaseService.SyncStatistics statistics = synchronizationService.synchronize(clientRequests);
        log.info("Synchronization finished");
        return wrapJson(PROCESSING_TIME, toSeconds(statistics.totalSyncTime()));
    }

    @GetMapping(value = "count")
    public String countEvents() {
        log.info("Calculate number of events");
        return wrapJson(COUNT, invoicingService.countEvents());
    }

    @PostMapping("simulation")
    public void startSimulation(@RequestBody SimulationRequest request) throws ExecutionException, InterruptedException {
        log.info("Starting simulation. Description: " + request.description());

        log.info("Truncating table");
        synchronizationService.truncateTables();

        log.info("Running synchronization");
        BaseService.SyncStatistics syncStatistics = synchronizationService.synchronize(request.clientRequests());

        log.info("Running invoicing");
        long invoicingTime = invoicingService.startInvoicing();

        log.info("Storing results in result store");
        RequestStatistics statistics = request.getStatistics();
        resultStore.saveResult(SimulationResult.builder()
                .clients(statistics.clients())
                .accounts(statistics.accounts())
                .events(statistics.events())
                .synchronizationTime(syncStatistics.totalSyncTime())
                .invoicingTime(invoicingTime)
                .batchSyncTime(syncStatistics.batchSyncTime())
                .description(request.description())
                .build());

        log.info("Simulation finished");
    }

    @GetMapping("results")
    public List<SimulationResult> getResults() {
        return resultStore.getResults();
    }

    @GetMapping("result")
    public SimulationResult getLastResult() {
        return resultStore.getLastResult();
    }
}

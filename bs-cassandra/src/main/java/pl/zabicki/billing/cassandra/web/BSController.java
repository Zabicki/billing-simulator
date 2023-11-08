package pl.zabicki.billing.cassandra.web;

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
@RequestMapping(value = "/cassandra/")
@Slf4j
public class BSController extends BaseController {

    @Autowired
    SynchronizationService synchronizationService;

    @Autowired
    InvoicingService invoicingService;

    @GetMapping(value = "invoicing")
    public String startInvoicing() throws ExecutionException, InterruptedException {
        log.info("Invoicing started");
        long processingTime = invoicingService.startInvoicing();
        log.info("Invoicing finished");
        return wrapJson(PROCESSING_TIME, toSeconds(processingTime));
    }

    @PostMapping(value = "synchronization")
    public String startEventSynchronization(@RequestBody List<ClientRequest> clientRequests) {
        log.info("Synchronization started");
        BaseService.SyncStatistics processingTime = synchronizationService.synchronize(clientRequests);
        log.info("Synchronization finished");
        return wrapJson(PROCESSING_TIME, toSeconds(processingTime.totalSyncTime()));
    }

    @GetMapping(value = "count")
    public String countEvents() {
        log.info("Calculate number of events");
        return wrapJson(COUNT, invoicingService.countEvents());
    }

    @GetMapping(value = "truncate")
    public void truncateEvents() {
        log.info("Truncate events");
        synchronizationService.truncateEvents();
        log.info("Events truncated");
    }

    @PostMapping("simulation")
    public void startSimulation(@RequestBody SimulationRequest request) throws ExecutionException, InterruptedException {
        log.info("Starting simulation. Description: " + request.description());

        log.info("Truncating table");
        synchronizationService.truncateEvents();

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

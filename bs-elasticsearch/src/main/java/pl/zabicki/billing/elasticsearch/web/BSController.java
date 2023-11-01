package pl.zabicki.billing.elasticsearch.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pl.zabicki.billing.core.controller.BaseController;
import pl.zabicki.billing.core.generator.ClientRequest;
import pl.zabicki.billing.core.result.store.SimulationResult;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/elasticsearch/")
@RequiredArgsConstructor
@Slf4j
public class BSController extends BaseController {

    private final SynchronizationService synchronizationService;

    private final InvoicingService invoicingService;

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
        long processingTime = synchronizationService.synchronize(clientRequests);
        log.info("Synchronization finished");
        return wrapJson(PROCESSING_TIME, toSeconds(processingTime));
    }

    @GetMapping(value = "count")
    public String countEvents() {
        long numOfEvents = invoicingService.countEvents();
        log.info("Calculated number of events = " + numOfEvents);
        return wrapJson(COUNT, numOfEvents);
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
        long synchronizationTime = synchronizationService.synchronize(request.clientRequests());

        // wait for all the documents to be indexed and searchable by ES
        Thread.sleep(5000);

        log.info("Running invoicing");
        long invoicingTime = invoicingService.startInvoicing();

        log.info("Storing results in result store");
        resultStore.saveResult(SimulationResult.builder()
                .synchronizationTime(synchronizationTime)
                .invoicingTime(invoicingTime)
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

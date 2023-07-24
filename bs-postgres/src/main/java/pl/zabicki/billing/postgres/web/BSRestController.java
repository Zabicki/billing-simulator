package pl.zabicki.billing.postgres.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
@RestController
@RequestMapping(value = "/postgres/")
@Slf4j
public class BSRestController {

    @Autowired
    SynchronizationService synchronizationService;

    @Autowired
    InvoicingService invoicingService;

    @GetMapping(value = "invoicing")
    public String startInvoicing() throws IOException {
        log.info("Start invoicing");
        invoicingService.startInvoicing();
        log.info("End invoicing");
        return "Invoicing simulation started";
    }

    @GetMapping(value = "synchronization")
    public String startEventSynchronization() throws IOException {
        log.info("Synchronization started");
        synchronizationService.synchronize();
        log.info("Synchronization finished");
        return "Event synchronization simulation finished";
    }

    @GetMapping(value = "count")
    public String countEvents() {
        log.info("Calculate number of events");
        return "{ \"count\": " + invoicingService.countEvents() + "}";
    }
}

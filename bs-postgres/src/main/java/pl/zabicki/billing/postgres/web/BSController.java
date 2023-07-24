package pl.zabicki.billing.postgres.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.zabicki.billing.core.controller.BaseController;

import java.io.IOException;
@RestController
@RequestMapping(value = "/postgres/")
@Slf4j
public class BSController extends BaseController {

    @Autowired
    SynchronizationService synchronizationService;

    @Autowired
    InvoicingService invoicingService;

    @GetMapping(value = "invoicing")
    public String startInvoicing() throws IOException {
        log.info("Invoicing started");
        long processingTime = invoicingService.startInvoicing();
        log.info("Invoicing finished");
        return wrapJson(PROCESSING_TIME, toSeconds(processingTime));
    }

    @GetMapping(value = "synchronization")
    public String startEventSynchronization() throws IOException {
        log.info("Synchronization started");
        long processingTime = synchronizationService.synchronize();
        log.info("Synchronization finished");
        return wrapJson(PROCESSING_TIME, toSeconds(processingTime));
    }

    @GetMapping(value = "count")
    public String countEvents() {
        log.info("Calculate number of events");
        return wrapJson(COUNT, invoicingService.countEvents());
    }
}

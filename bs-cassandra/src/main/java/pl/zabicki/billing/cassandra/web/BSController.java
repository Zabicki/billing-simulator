package pl.zabicki.billing.cassandra.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
@RestController
@RequestMapping(value = "/cassandra/")
public class BSController {

    @Autowired
    SynchronizationService synchronizationService;

    @Autowired
    InvoicingService invoicingService;

    @GetMapping(value = "invoicing")
    public String startInvoicing() throws IOException {
        invoicingService.startInvoicing();
        return "Invoicing simulation started";
    }

    @GetMapping(value = "synchronization")
    public String startEventSynchronization() throws IOException {
        synchronizationService.synchronize();
        return "Event synchronization simulation started";
    }
}

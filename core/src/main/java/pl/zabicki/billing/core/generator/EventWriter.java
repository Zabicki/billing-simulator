package pl.zabicki.billing.core.generator;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;
import pl.zabicki.billing.core.data.model.CsvEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class EventWriter {

    private final String eventDir;
    private final String accountDir;
    private static final int EVENTS_PER_FILE = 100000;

    public EventWriter() {
        LocalDateTime dateTime = LocalDateTime.now();
        String directoryName = String.format("%04d%02d%02d%02d%02d",
                dateTime.getYear(),
                dateTime.getMonthValue(),
                dateTime.getDayOfMonth(),
                dateTime.getHour(),
                dateTime.getMinute());

        this.eventDir = "data/" + directoryName + "/events";
        this.accountDir = "data/" + directoryName + "/accounts";

        try {
            Files.createDirectory(Paths.get("data/" + directoryName));
            Files.createDirectory(Paths.get(eventDir));
            Files.createDirectory(Paths.get(accountDir));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeEvents(List<CsvEvent> resultEvents) {
        int fileNumber = 1;
        int eventsWritten = 0;
        BufferedWriter writer;
        CSVPrinter printer = null;
        CSVFormat format = createFormat(); // Assume some default format; modify as needed.

        try {
            for (CsvEvent event : resultEvents) {
                if (eventsWritten % EVENTS_PER_FILE == 0) {
                    if (printer != null) {
                        printer.close();
                    }
                    writer = Files.newBufferedWriter(Paths.get(eventDir + "/events" + fileNumber + ".csv"), StandardOpenOption.CREATE_NEW);
                    printer = new CSVPrinter(writer, format);
                    fileNumber++;
                }

                printRecord(printer, event);
                eventsWritten++;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (printer != null) {
                try {
                    printer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void writeAccounts(Map<String, List<String>> clientIdToAccountIds) throws IOException {
        int fileNumber = 1; // Starting file number
        int writeCount = 0; // Track how many objects have been written to the current file
        int maxObjectsPerFile = 100000;

        FileWriter fw = null;

        try {
            for (Map.Entry<String, List<String>> entry : clientIdToAccountIds.entrySet()) {
                for (String accountId : entry.getValue()) {
                    // Check if we've reached our limit for the current file
                    if (writeCount == 0 || writeCount >= maxObjectsPerFile) {
                        // If we already had a FileWriter open, close it
                        if (fw != null) {
                            fw.close();
                        }

                        // Open a new FileWriter for the next file
                        fw = new FileWriter(accountDir + "/accounts" + fileNumber + ".csv");
                        fw.write("clientId,accountId\n"); // write header for each new file
                        fileNumber++;
                        writeCount = 0;
                    }

                    // Write the current object
                    fw.write(entry.getKey() + "," + accountId + "\n");
                    writeCount++;
                }
            }
        } finally {
            // Ensure that we always close the FileWriter when done
            if (fw != null) {
                fw.close();
            }
        }
    }

    private CSVFormat createFormat() {
        return CSVFormat.DEFAULT.builder()
                .setHeader("clientId",
                        "accountId",
                        "apInstanceId",
                        "callingNumber",
                        "calledNumber",
                        "callingPrefix",
                        "calledPrefix",
                        "eventBeginDate",
                        "eventEndDate",
                        "productId",
                        "rootProductId",
                        "intProperty1",
                        "intProperty2",
                        "intProperty3",
                        "intProperty4",
                        "intProperty5",
                        "stringProperty1",
                        "stringProperty2",
                        "stringProperty3",
                        "stringProperty4",
                        "stringProperty5",
                        "booleanProperty1",
                        "booleanProperty2",
                        "booleanProperty3",
                        "booleanProperty4",
                        "booleanProperty5",
                        "quantity",
                        "billingCycleDefId",
                        "billingCycleInstanceId",
                        "unit",
                        "billingProviderId")
                .build();
    }

    private void printRecord(CSVPrinter printer, CsvEvent event) throws IOException {
        printer.printRecord(event.getClientId(),
                event.getAccountId(),
                event.getApInstanceId(),
                event.getCallingNumber(),
                event.getCalledNumber(),
                event.getCallingPrefix(),
                event.getCalledPrefix(),
                event.getEventBeginDate(),
                event.getEventEndDate(),
                event.getProductId(),
                event.getRootProductId(),
                event.getIntProperty1(),
                event.getIntProperty2(),
                event.getIntProperty3(),
                event.getIntProperty4(),
                event.getIntProperty5(),
                event.getStringProperty1(),
                event.getStringProperty2(),
                event.getStringProperty3(),
                event.getStringProperty4(),
                event.getStringProperty5(),
                event.isBooleanProperty1(),
                event.isBooleanProperty2(),
                event.isBooleanProperty3(),
                event.isBooleanProperty4(),
                event.isBooleanProperty5(),
                event.getQuantity(),
                event.getBillingCycleDefId(),
                event.getBillingCycleInstanceId(),
                event.getUnit(),
                event.getBillingProviderId());
    }

}

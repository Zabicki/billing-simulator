package pl.zabicki.billing.core.data.reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import pl.zabicki.billing.core.data.model.CsvEvent;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.stream.StreamSupport;

public class EventReader implements DataReader<CsvEvent> {

    @Override
    public List<CsvEvent> readData(String path) throws IOException {
        Reader in = new FileReader(path);
        CSVFormat format = createEventFormat();
        Iterable<CSVRecord> records = format.parse(in);

        return StreamSupport.stream(records.spliterator(), false)
                .map(this::createEvent)
                .toList();
    }

    private CsvEvent createEvent(CSVRecord r) {
        return CsvEvent.builder()
                .clientId(r.get("clientId"))
                .accountId(r.get("accountId"))
                .apInstanceId(r.get("apInstanceId"))
                .callingNumber(r.get("callingNumber"))
                .calledNumber(r.get("calledNumber"))
                .callingPrefix(r.get("callingPrefix"))
                .calledPrefix(r.get("calledPrefix"))
                .eventBeginDate(r.get("eventBeginDate"))
                .eventEndDate(r.get("eventEndDate"))
                .productId(r.get("productId"))
                .rootProductId(r.get("rootProductId"))
                .intProperty1(Long.parseLong(r.get("intProperty1")))
                .intProperty2(Long.parseLong(r.get("intProperty2")))
                .intProperty3(Long.parseLong(r.get("intProperty3")))
                .intProperty4(Long.parseLong(r.get("intProperty4")))
                .intProperty5(Long.parseLong(r.get("intProperty5")))
                .stringProperty1(r.get("stringProperty1"))
                .stringProperty2(r.get("stringProperty2"))
                .stringProperty3(r.get("stringProperty3"))
                .stringProperty4(r.get("stringProperty4"))
                .stringProperty5(r.get("stringProperty5"))
                .booleanProperty1(Boolean.parseBoolean(r.get("booleanProperty1")))
                .booleanProperty2(Boolean.parseBoolean(r.get("booleanProperty2")))
                .booleanProperty3(Boolean.parseBoolean(r.get("booleanProperty3")))
                .booleanProperty4(Boolean.parseBoolean(r.get("booleanProperty4")))
                .booleanProperty5(Boolean.parseBoolean(r.get("booleanProperty5")))
                .quantity(Long.parseLong(r.get("quantity")))
                .billingCycleDefId(r.get("billingCycleDefId"))
                .billingCycleInstanceId(r.get("billingCycleInstanceId"))
                .unit(r.get("unit"))
                .billingProviderId(r.get("billingProviderId"))
                .build();
    }

    private CSVFormat createEventFormat() {
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
                .setSkipHeaderRecord(true)
                .build();
    }
}

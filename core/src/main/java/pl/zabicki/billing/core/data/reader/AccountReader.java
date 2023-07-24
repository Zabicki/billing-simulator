package pl.zabicki.billing.core.data.reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import pl.zabicki.billing.core.data.model.CsvAccount;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.stream.StreamSupport;

public class AccountReader implements DataReader<CsvAccount> {

    @Override
    public List<CsvAccount> readData(String path) throws IOException {
        Reader in = new FileReader(path);
        CSVFormat format = createAccountFormat();
        Iterable<CSVRecord> records =  format.parse(in);
        return StreamSupport.stream(records.spliterator(), false)
                .map(r -> new CsvAccount(r.get("clientId"), r.get("accountId")))
                .toList();
    }

    private CSVFormat createAccountFormat() {
        return CSVFormat.DEFAULT.builder()
                .setHeader("clientId", "accountId")
                .setSkipHeaderRecord(true)
                .build();
    }
}

package pl.zabicki.billing.core.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BaseService {

    protected List<Path> getEventFiles(String dataSet) {
        try {
            return Files.list(Paths.get("data/" + dataSet + "/events"))
                    .filter(f -> Files.isRegularFile(f) && f.toString().contains("events"))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<Path> getAccountFiles(String dataSet) {
        try {
            return Files.list(Paths.get("data/" + dataSet + "/accounts"))
                    .filter(f -> Files.isRegularFile(f) && f.toString().contains("accounts"))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

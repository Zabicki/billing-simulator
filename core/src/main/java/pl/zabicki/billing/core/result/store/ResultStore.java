package pl.zabicki.billing.core.result.store;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResultStore {
    private final String filePath;
    private final ObjectMapper objectMapper;

    public ResultStore(String filePath) {
        this.filePath = filePath;
        this.objectMapper = new ObjectMapper();

        // Check and create the file if it doesn't exist
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Unable to create the file.", e);
            }
        }
    }

    public List<SimulationResult> getResults() {
        try {
            File file = new File(filePath);
            if (file.length() == 0) {
                return new ArrayList<>();
            }
            List<SimulationResult> results = objectMapper.readValue(file, new TypeReference<List<SimulationResult>>() {});
            Collections.reverse(results);
            return results;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read data from the file.", e);
        }
    }

    public SimulationResult getLastResult() {
        List<SimulationResult> results = getResults();
        return results.get(results.size() - 1);
    }

    public void saveResult(SimulationResult data) {
        try {
            log.info("Persisting simulation result:\n" + objectMapper.writeValueAsString(data));
            List<SimulationResult> currentData = getResults();
            currentData.add(data);
            Files.write(Paths.get(filePath), objectMapper.writeValueAsBytes(currentData),
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write data to the file.", e);
        }
    }
}

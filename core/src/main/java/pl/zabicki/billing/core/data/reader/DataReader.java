package pl.zabicki.billing.core.data.reader;

import java.io.IOException;
import java.util.List;

@FunctionalInterface
public interface DataReader <T> {
    List<T> readData(String path) throws IOException;
}

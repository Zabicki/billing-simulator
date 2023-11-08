package pl.zabicki.billing.postgres;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BSPostgres {

    public static void main(String[] args) {
        SpringApplication.run(BSPostgres.class, args);
    }
}

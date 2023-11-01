package pl.zabicki.billing.cassandra.model;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import pl.zabicki.billing.core.generator.EventGenerator;

import java.util.UUID;

@Data
@Table("account")
public class Account {

    @PrimaryKey
    private String id;
    private String clientId;
    private String accountId;

    public static Account fromAccountInfo(EventGenerator.AccountInfo accountInfo) {
        Account account = new Account();
        account.id = UUID.randomUUID().toString();
        account.setClientId(accountInfo.clientId());
        account.setAccountId(accountInfo.accountId());
        return account;
    }
}

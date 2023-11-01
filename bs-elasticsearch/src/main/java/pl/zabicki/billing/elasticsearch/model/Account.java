package pl.zabicki.billing.elasticsearch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import pl.zabicki.billing.core.generator.EventGenerator;

@Data
@Document(indexName = "account", dynamic = Dynamic.STRICT)
public class Account {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Text, index = false)
    private String clientId;
    @Field(type = FieldType.Text, index = false)
    private String accountId;

    public static Account fromAccountInfo(EventGenerator.AccountInfo accountInfo) {
        Account account = new Account();
        account.setClientId(accountInfo.clientId());
        account.setAccountId(accountInfo.accountId());
        return account;
    }
}

package pl.zabicki.billing.cassandra.model;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("event")
@Data
public class Event {

    @PrimaryKey
    private EventPrimaryKey id;
    private String apInstanceId;
    private String callingNumber;
    private String calledNumber;
    private String callingPrefix;
    private String calledPrefix;
    private String eventBeginDate;
    private String eventEndDate;
    private String productId;
    private String rootProductId;
    private int intProperty1;
    private int intProperty2;
    private int intProperty3;
    private int intProperty4;
    private int intProperty5;
    private String stringProperty1;
    private String stringProperty2;
    private String stringProperty3;
    private String stringProperty4;
    private String stringProperty5;
    private boolean booleanProperty1;
    private boolean booleanProperty2;
    private boolean booleanProperty3;
    private boolean booleanProperty4;
    private boolean booleanProperty5;
    private int quantity;
    private String billingCycleDefId;
    private String billingCycleInstanceId;
    private String unit;
    private String billingProviderId;
}

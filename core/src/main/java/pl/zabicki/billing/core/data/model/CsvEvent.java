package pl.zabicki.billing.core.data.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CsvEvent {
    private long id;
    private String clientId;
    private String accountId;
    private String apInstanceId;
    private String callingNumber;
    private String calledNumber;
    private String callingPrefix;
    private String calledPrefix;
    private String eventBeginDate;
    private String eventEndDate;
    private String productId;
    private String rootProductId;
    private long intProperty1;
    private long intProperty2;
    private long intProperty3;
    private long intProperty4;
    private long intProperty5;
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
    private long quantity;
    private String billingCycleDefId;
    private String billingCycleInstanceId;
    private String unit;
    private String billingProviderId;

    @Override
    public String toString() {
        return clientId + "," +
                accountId + "," +
                apInstanceId + "," +
                callingNumber + "," +
                calledNumber + "," +
                callingPrefix + "," +
                calledPrefix + "," +
                eventBeginDate + "," +
                eventEndDate + "," +
                productId + "," +
                rootProductId + "," +
                intProperty1 + "," +
                intProperty2 + "," +
                intProperty3 + "," +
                intProperty4 + "," +
                intProperty5 + "," +
                stringProperty1 + "," +
                stringProperty2 + "," +
                stringProperty3 + "," +
                stringProperty4 + "," +
                stringProperty5 + "," +
                booleanProperty1 + "," +
                booleanProperty2 + "," +
                booleanProperty3 + "," +
                booleanProperty4 + "," +
                booleanProperty5 + "," +
                quantity + "," +
                billingCycleDefId + "," +
                billingCycleInstanceId + "," +
                unit + "," +
                billingProviderId;
    }
}

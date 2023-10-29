package pl.zabicki.billing.elasticsearch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Data
@Document(indexName = "event", dynamic = Dynamic.STRICT)
@Setting(shards = 1,
        replicas = 1)
@Routing("clientId")
public class Event {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Keyword)
    private String clientId;
    @Field(type = FieldType.Keyword)
    private String accountId;
    @Field(type = FieldType.Text, index = false)
    private String apInstanceId;
    @Field(type = FieldType.Text, index = false)
    private String callingNumber;
    @Field(type = FieldType.Text, index = false)
    private String calledNumber;
    @Field(type = FieldType.Text, index = false)
    private String callingPrefix;
    @Field(type = FieldType.Text, index = false)
    private String calledPrefix;
    @Field(type = FieldType.Text, index = false)
    private String eventBeginDate;
    @Field(type = FieldType.Text, index = false)
    private String eventEndDate;
    @Field(type = FieldType.Text, index = false)
    private String productId;
    @Field(type = FieldType.Text, index = false)
    private String rootProductId;
    @Field(type = FieldType.Text, index = false)
    private long intProperty1;
    @Field(type = FieldType.Text, index = false)
    private long intProperty2;
    @Field(type = FieldType.Text, index = false)
    private long intProperty3;
    @Field(type = FieldType.Text, index = false)
    private long intProperty4;
    @Field(type = FieldType.Text, index = false)
    private long intProperty5;
    @Field(type = FieldType.Text, index = false)
    private String stringProperty1;
    @Field(type = FieldType.Text, index = false)
    private String stringProperty2;
    @Field(type = FieldType.Text, index = false)
    private String stringProperty3;
    @Field(type = FieldType.Text, index = false)
    private String stringProperty4;
    @Field(type = FieldType.Text, index = false)
    private String stringProperty5;
    @Field(type = FieldType.Text, index = false)
    private boolean booleanProperty1;
    @Field(type = FieldType.Text, index = false)
    private boolean booleanProperty2;
    @Field(type = FieldType.Text, index = false)
    private boolean booleanProperty3;
    @Field(type = FieldType.Text, index = false)
    private boolean booleanProperty4;
    @Field(type = FieldType.Text, index = false)
    private boolean booleanProperty5;
    @Field(type = FieldType.Text, index = false)
    private long quantity;
    @Field(type = FieldType.Text, index = false)
    private String billingCycleDefId;
    @Field(type = FieldType.Text, index = false)
    private String billingCycleInstanceId;
    @Field(type = FieldType.Text, index = false)
    private String unit;
    @Field(type = FieldType.Text, index = false)
    private String billingProviderId;
}
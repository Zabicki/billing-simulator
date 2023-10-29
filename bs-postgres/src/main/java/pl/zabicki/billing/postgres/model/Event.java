package pl.zabicki.billing.postgres.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Event event = (Event) o;
        return id != null && Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
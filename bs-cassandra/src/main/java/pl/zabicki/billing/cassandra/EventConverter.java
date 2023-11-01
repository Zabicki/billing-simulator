package pl.zabicki.billing.cassandra;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.zabicki.billing.cassandra.model.Event;
import pl.zabicki.billing.cassandra.model.EventPrimaryKey;
import pl.zabicki.billing.core.generator.BaseEvent;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventConverter {

    public static Event convertEvent(BaseEvent baseEvent) {
        Event event = new Event();
        event.setId(new EventPrimaryKey(baseEvent.getClientId(), baseEvent.getAccountId(), UUID.randomUUID().toString()));
        event.setApInstanceId(baseEvent.getApInstanceId());
        event.setCallingNumber(baseEvent.getCallingNumber());
        event.setCalledNumber(baseEvent.getCalledNumber());
        event.setCallingPrefix(baseEvent.getCallingPrefix());
        event.setCalledPrefix(baseEvent.getCalledPrefix());
        event.setEventBeginDate(baseEvent.getEventBeginDate());
        event.setEventEndDate(baseEvent.getEventEndDate());
        event.setProductId(baseEvent.getProductId());
        event.setRootProductId(baseEvent.getRootProductId());
        event.setIntProperty1((int)baseEvent.getIntProperty1());
        event.setIntProperty2((int)baseEvent.getIntProperty2());
        event.setIntProperty3((int)baseEvent.getIntProperty3());
        event.setIntProperty4((int)baseEvent.getIntProperty4());
        event.setIntProperty5((int)baseEvent.getIntProperty5());
        event.setStringProperty1(baseEvent.getStringProperty1());
        event.setStringProperty2(baseEvent.getStringProperty2());
        event.setStringProperty3(baseEvent.getStringProperty3());
        event.setStringProperty4(baseEvent.getStringProperty4());
        event.setStringProperty5(baseEvent.getStringProperty5());
        event.setBooleanProperty1(baseEvent.isBooleanProperty1());
        event.setBooleanProperty2(baseEvent.isBooleanProperty2());
        event.setBooleanProperty3(baseEvent.isBooleanProperty3());
        event.setBooleanProperty4(baseEvent.isBooleanProperty4());
        event.setBooleanProperty5(baseEvent.isBooleanProperty5());
        event.setQuantity((int)baseEvent.getQuantity());
        event.setBillingCycleDefId(baseEvent.getBillingCycleDefId());
        event.setBillingCycleInstanceId(baseEvent.getBillingCycleInstanceId());
        event.setUnit(baseEvent.getUnit());
        event.setBillingProviderId(baseEvent.getBillingProviderId());
        return event;
    }

    public static List<Event> convertEvents(List<BaseEvent> baseEvent) {
        return baseEvent.stream()
                .map(EventConverter::convertEvent)
                .toList();
    }
}
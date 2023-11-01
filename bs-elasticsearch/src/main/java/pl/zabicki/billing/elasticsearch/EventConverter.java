package pl.zabicki.billing.elasticsearch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.zabicki.billing.core.generator.BaseEvent;
import pl.zabicki.billing.elasticsearch.model.Event;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventConverter {

    public static Event convertEvent(BaseEvent baseEvent) {
        Event event = new Event();
        event.setClientId(baseEvent.getClientId());
        event.setAccountId(baseEvent.getAccountId());
        event.setApInstanceId(baseEvent.getApInstanceId());
        event.setCallingNumber(baseEvent.getCallingNumber());
        event.setCalledNumber(baseEvent.getCalledNumber());
        event.setCallingPrefix(baseEvent.getCallingPrefix());
        event.setCalledPrefix(baseEvent.getCalledPrefix());
        event.setEventBeginDate(baseEvent.getEventBeginDate());
        event.setEventEndDate(baseEvent.getEventEndDate());
        event.setProductId(baseEvent.getProductId());
        event.setRootProductId(baseEvent.getRootProductId());
        event.setIntProperty1(baseEvent.getIntProperty1());
        event.setIntProperty2(baseEvent.getIntProperty2());
        event.setIntProperty3(baseEvent.getIntProperty3());
        event.setIntProperty4(baseEvent.getIntProperty4());
        event.setIntProperty5(baseEvent.getIntProperty5());
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
        event.setQuantity(baseEvent.getQuantity());
        event.setBillingCycleDefId(baseEvent.getBillingCycleDefId());
        event.setBillingCycleInstanceId(baseEvent.getBillingCycleInstanceId());
        event.setUnit(baseEvent.getUnit());
        event.setBillingProviderId(baseEvent.getBillingProviderId());
        event.setId(UUID.randomUUID().toString());
        return event;
    }

    public static List<Event> convertEvents(List<BaseEvent> baseEvents) {
        return baseEvents.stream()
                .map(EventConverter::convertEvent)
                .toList();
    }
}
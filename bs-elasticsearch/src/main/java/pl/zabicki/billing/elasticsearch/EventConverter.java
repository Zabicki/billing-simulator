package pl.zabicki.billing.elasticsearch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.zabicki.billing.core.data.model.CsvEvent;
import pl.zabicki.billing.elasticsearch.model.Event;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventConverter {

    public static Event convertEvent(CsvEvent csvEvent) {
        Event event = new Event();
        event.setClientId(csvEvent.getClientId());
        event.setAccountId(csvEvent.getAccountId());
        event.setApInstanceId(csvEvent.getApInstanceId());
        event.setCallingNumber(csvEvent.getCallingNumber());
        event.setCalledNumber(csvEvent.getCalledNumber());
        event.setCallingPrefix(csvEvent.getCallingPrefix());
        event.setCalledPrefix(csvEvent.getCalledPrefix());
        event.setEventBeginDate(csvEvent.getEventBeginDate());
        event.setEventEndDate(csvEvent.getEventEndDate());
        event.setProductId(csvEvent.getProductId());
        event.setRootProductId(csvEvent.getRootProductId());
        event.setIntProperty1(csvEvent.getIntProperty1());
        event.setIntProperty2(csvEvent.getIntProperty2());
        event.setIntProperty3(csvEvent.getIntProperty3());
        event.setIntProperty4(csvEvent.getIntProperty4());
        event.setIntProperty5(csvEvent.getIntProperty5());
        event.setStringProperty1(csvEvent.getStringProperty1());
        event.setStringProperty2(csvEvent.getStringProperty2());
        event.setStringProperty3(csvEvent.getStringProperty3());
        event.setStringProperty4(csvEvent.getStringProperty4());
        event.setStringProperty5(csvEvent.getStringProperty5());
        event.setBooleanProperty1(csvEvent.isBooleanProperty1());
        event.setBooleanProperty2(csvEvent.isBooleanProperty2());
        event.setBooleanProperty3(csvEvent.isBooleanProperty3());
        event.setBooleanProperty4(csvEvent.isBooleanProperty4());
        event.setBooleanProperty5(csvEvent.isBooleanProperty5());
        event.setQuantity(csvEvent.getQuantity());
        event.setBillingCycleDefId(csvEvent.getBillingCycleDefId());
        event.setBillingCycleInstanceId(csvEvent.getBillingCycleInstanceId());
        event.setUnit(csvEvent.getUnit());
        event.setBillingProviderId(csvEvent.getBillingProviderId());
        event.setId(UUID.randomUUID().toString());
        return event;
    }

    public static List<Event> convertEvents(List<CsvEvent> csvEvents) {
        return csvEvents.stream()
                .map(EventConverter::convertEvent)
                .toList();
    }
}
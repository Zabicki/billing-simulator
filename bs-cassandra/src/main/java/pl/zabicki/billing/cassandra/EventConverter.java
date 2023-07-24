package pl.zabicki.billing.cassandra;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.zabicki.billing.cassandra.model.Event;
import pl.zabicki.billing.cassandra.model.EventPrimaryKey;
import pl.zabicki.billing.core.data.model.CsvEvent;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventConverter {

    public static Event convertEvent(CsvEvent csvEvent) {
        Event event = new Event();
        //event.setKey(new EventPrimaryKey(csvEvent.getClientId(), csvEvent.getAccountId()));
        event.setId(UUID.randomUUID().toString());
        event.setApInstanceId(csvEvent.getApInstanceId());
        event.setCallingNumber(csvEvent.getCallingNumber());
        event.setCalledNumber(csvEvent.getCalledNumber());
        event.setCallingPrefix(csvEvent.getCallingPrefix());
        event.setCalledPrefix(csvEvent.getCalledPrefix());
        event.setEventBeginDate(csvEvent.getEventBeginDate());
        event.setEventEndDate(csvEvent.getEventEndDate());
        event.setProductId(csvEvent.getProductId());
        event.setRootProductId(csvEvent.getRootProductId());
        event.setIntProperty1((int)csvEvent.getIntProperty1());
        event.setIntProperty2((int)csvEvent.getIntProperty2());
        event.setIntProperty3((int)csvEvent.getIntProperty3());
        event.setIntProperty4((int)csvEvent.getIntProperty4());
        event.setIntProperty5((int)csvEvent.getIntProperty5());
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
        event.setQuantity((int)csvEvent.getQuantity());
        event.setBillingCycleDefId(csvEvent.getBillingCycleDefId());
        event.setBillingCycleInstanceId(csvEvent.getBillingCycleInstanceId());
        event.setUnit(csvEvent.getUnit());
        event.setBillingProviderId(csvEvent.getBillingProviderId());
        return event;
    }

    public static List<Event> convertEvents(List<CsvEvent> csvEvents) {
        return csvEvents.stream()
                .map(EventConverter::convertEvent)
                .toList();
    }
}
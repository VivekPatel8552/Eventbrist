package com.test.epam.Eventbrite.POJO;

import java.util.HashMap;

public class EventPOJO {
	
	private EventAttributePOJO event;
	
	public EventAttributePOJO getEvent() {
		return event;
	}

	public void setEvent(EventAttributePOJO event) {
		this.event = event;
	}

	public EventPOJO createEventPOJO(HashMap<String, String> testData) throws Exception {
		EventAttributePOJO eventAttribute = new EventAttributePOJO();
		EventPOJO eventPOJO = new EventPOJO();
		
		// Get Inner Payload of Event Attributes
		eventAttribute = (new EventAttributePOJO()).createEventAttributePOJO(testData);
		eventPOJO.setEvent(eventAttribute);
		return eventPOJO;
	}
}

package com.test.epam.Eventbrite.POJO;

import java.util.HashMap;

public class EventAttributePOJO {
	
	private boolean hide_start_date;
	private boolean hide_end_date;
	private String currency;
	private boolean online_event;
	private boolean listed;
	private String capacity;
	private name name;
	private start start;
	private end end;

	public name getName() {
		return name;
	}

	public void setName(name name) {
		this.name = name;
	}

	public start getStart() {
		return start;
	}

	public void setStart(start start) {
		this.start = start;
	}

	public end getEnd() {
		return end;
	}

	public void setEnd(end end) {
		this.end = end;
	}

	public boolean getHide_start_date() {
		return hide_start_date;
	}

	public void setHide_start_date(boolean hide_start_date) {
		this.hide_start_date = hide_start_date;
	}

	public boolean getHide_end_date() {
		return hide_end_date;
	}

	public void setHide_end_date(boolean hide_end_date) {
		this.hide_end_date = hide_end_date;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean getOnline_event() {
		return online_event;
	}

	public void setOnline_event(boolean online_event) {
		this.online_event = online_event;
	}

	public boolean getListed() {
		return listed;
	}

	public void setListed(boolean listed) {
		this.listed = listed;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}	

	class event{
		
	}
	
	// POJO Class for name attribute of Payload
	class name {
		private String html;

		public String getHtml() {
			return html;
		}
		public void setHtml(String html) {
			this.html = html;
		}
	}
	
	// POJO class for start attribute of Payload
	class start{
		private String timezone;
		private String utc;
		
		public String getTimezone() {
			return timezone;
		}
		public void setTimezone(String timezone) {
			this.timezone = timezone;
		}
		public String getUtc() {
			return utc;
		}
		public void setUtc(String utc) {
			this.utc = utc;
		}
	}
	
	// POJO class for end attribute of Payload
	class end{
		private String timezone;
		private String utc;
		
		public String getTimezone() {
			return timezone;
		}
		public void setTimezone(String timezone) {
			this.timezone = timezone;
		}
		public String getUtc() {
			return utc;
		}
		public void setUtc(String utc) {
			this.utc = utc;
		}
	}
		
	public EventAttributePOJO createEventAttributePOJO(HashMap<String, String> testData) {
		EventAttributePOJO eventAttributePOJO = new EventAttributePOJO();
						
		// set EventName in Payload
		EventAttributePOJO.name namePOJO = eventAttributePOJO.new name();
		namePOJO.setHtml(testData.get("eventName"));
		eventAttributePOJO.setName(namePOJO);
		
		// Set startDate in Payload
		EventAttributePOJO.start startDatePOJO = eventAttributePOJO.new start();
		startDatePOJO.setTimezone(testData.get("timeZone"));
		startDatePOJO.setUtc(testData.get("startDateTime"));
		eventAttributePOJO.setStart(startDatePOJO);
		
		// Set endDate in Payload
		EventAttributePOJO.end endDatePOJO = eventAttributePOJO.new end();
		endDatePOJO.setTimezone(testData.get("timeZone"));
		endDatePOJO.setUtc(testData.get("endDateTime"));
		eventAttributePOJO.setEnd(endDatePOJO);
		
		eventAttributePOJO.setCapacity(testData.get("capacity"));
		eventAttributePOJO.setCurrency(testData.get("currency"));
		eventAttributePOJO.setHide_start_date(Boolean.parseBoolean(testData.get("hideStartDate")));
		eventAttributePOJO.setHide_end_date(Boolean.parseBoolean(testData.get("hideEndDate")));
		eventAttributePOJO.setListed(Boolean.parseBoolean(testData.get("listed")));
		eventAttributePOJO.setOnline_event(Boolean.parseBoolean(testData.get("onlineEvent")));
		
		return eventAttributePOJO;
	}
}

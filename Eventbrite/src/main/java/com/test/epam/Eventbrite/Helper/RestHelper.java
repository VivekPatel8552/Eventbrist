package com.test.epam.Eventbrite.Helper;

import java.util.HashMap;
import com.google.gson.Gson;
import com.jayway.restassured.response.Response;
import com.test.epam.Eventbrite.POJO.EventPOJO;
import com.test.epam.Eventbrite.Utils.RestUtils;
import com.test.epam.Eventbrite.Utils.URLGenerator;

public class RestHelper {

	URLGenerator urlGenerator;
	RestUtils restUtils;
	
	Gson gson = new Gson();
	public RestHelper(String instanceUrl, RestUtils restUtils) throws Exception {
		this.urlGenerator = new URLGenerator(instanceUrl);
		this.restUtils = restUtils;
	}

	/**
	 * Common method to create Event via API
	 * 
	 * @param organizationId - String organization id under which new event should be created
	 * 
	 * @param createEventData
	 * 				- Map containing payload
	 * @return
	 * @throws Exception
	 */
	public Response createEvent(String organizationId, HashMap<String, String> createEventData) throws Exception {
		try {
			String payload = this.gson.toJson(new EventPOJO().createEventPOJO(createEventData));
			Response response = this.restUtils.postWithPayLoad(
					urlGenerator.organizationBasedEventURL.replace("{organization_id}", organizationId), payload);
			if (response.getStatusCode() != 200) {
				throw new Exception(
						"Failure while creating new Event using the API :" + this.urlGenerator.organizationBasedEventURL
								+ ". The response code was:" + response.getStatusCode()
								+ "and the response body received is: " + response.getBody().asString());

			} else {
				return response;
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Common method to get Event Info via event id
	 * 
	 * @param event_Id - String event id
	 * @return
	 * @throws Exception
	 */
	public Response getEventInfo(String event_Id) throws Exception {
		try {
			Response response = this.restUtils.getData(urlGenerator.eventURL.replace("{event_id}", event_Id));
			if (response.getStatusCode() != 200) {
				throw new Exception(
						"Failure while getting Event info using the API :" + this.urlGenerator.eventURL
								+ ". The response code was:" + response.getStatusCode()
								+ "and the response body received is: " + response.getBody().asString());
			} else {
				return response;
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Common method to update existing event
	 * 
	 * @param eventId - String event id
	 * 
	 * @param createEventData
	 * 		- Map for updating event
	 * @return
	 * @throws Exception
	 */
	public Response updateEvent(String eventId, HashMap<String, String> createEventData) throws Exception {
		try {
			String payload = this.gson.toJson(new EventPOJO().createEventPOJO(createEventData));
			Response response = this.restUtils.postWithPayLoad(
					urlGenerator.eventURL.replace("{event_id}", eventId), payload);
			if (response.getStatusCode() != 200) {
				throw new Exception(
						"Failure while update existing Event using the API :" + this.urlGenerator.eventURL
								+ ". The response code was:" + response.getStatusCode()
								+ "and the response body received is: " + response.getBody().asString());

			} else {
				return response;
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Common method to copy event from existing event
	 * 
	 * @param eventId - String event id
	 * 
	 * @return
	 * @throws Exception
	 */
	public Response copyEvent(String eventId) throws Exception {
		try {
			Response response = this.restUtils.postWithoutPayLoad(urlGenerator.copyEventURL.replace("{event_id}", eventId));
			if (response.getStatusCode() != 200) {
				throw new Exception(
						"Failure while copying existing Event using the API :" + this.urlGenerator.copyEventURL
								+ ". The response code was:" + response.getStatusCode()
								+ "and the response body received is: " + response.getBody().asString());

			} else {
				return response;
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Common method to Cancel Existing Event
	 * 
	 * @param eventId - String event id
	 * @return
	 * @throws Exception
	 */
	public Response cancelEvent(String eventId) throws Exception {
		try {
			Response response = this.restUtils.postWithoutPayLoad(urlGenerator.cancelEventURL.replace("{event_id}", eventId));
			if (response.getStatusCode() != 200) {
				throw new Exception(
						"Failure while cancelling existing Event using the API :" + this.urlGenerator.cancelEventURL
								+ ". The response code was:" + response.getStatusCode()
								+ "and the response body received is: " + response.getBody().asString());

			} else {
				return response;
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Common method to Delete Event
	 * 
	 * @param eventId - String event id
	 * @return
	 * @throws Exception
	 */
	public Response deleteEvent(String eventId) throws Exception {
		try {
			Response response = this.restUtils.deleteWithoutPayload(urlGenerator.eventURL.replace("{event_id}", eventId));
			if (response.getStatusCode() != 200) {
				throw new Exception(
						"Failure while deleting existing Event using the API :" + this.urlGenerator.eventURL
								+ ". The response code was:" + response.getStatusCode()
								+ "and the response body received is: " + response.getBody().asString());

			} else {
				return response;
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
}

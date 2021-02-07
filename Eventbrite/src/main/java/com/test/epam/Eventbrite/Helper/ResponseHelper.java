package com.test.epam.Eventbrite.Helper;

import java.util.HashMap;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class ResponseHelper {

	/**
	 * Common method to get specific field Value From API Response
	 * @param response
	 * @param fieldName
	 * @return
	 */
	public String getFieldValueFromResponse(Response response, String fieldName) {
		JsonPath path = new JsonPath(response.getBody().asString());
		return path.getString(fieldName);
	}

	/**
	 * Common method to update API Response based on field
	 * @param response
	 * @param eventAssertionData
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> updateEventCreationAPIResponse(Response response, HashMap<String, String> eventAssertionData) throws Exception {
		for(String keyValue : eventAssertionData.keySet()) {
			if(keyValue.equalsIgnoreCase("eventName"))
				eventAssertionData.put("eventName", getFieldValueFromResponse(response, "name.html"));
			else if(keyValue.equalsIgnoreCase("timeZone"))
				eventAssertionData.put("timeZone", getFieldValueFromResponse(response, "start.timezone"));
			else if(keyValue.equalsIgnoreCase("startDateTime"))
				eventAssertionData.put("startDateTime", getFieldValueFromResponse(response, "start.utc"));
			else if(keyValue.equalsIgnoreCase("endDateTime"))
				eventAssertionData.put("endDateTime", getFieldValueFromResponse(response, "end.utc"));
			else
				eventAssertionData.put(keyValue, getFieldValueFromResponse(response, keyValue));			
		}
		return eventAssertionData;
	}
}
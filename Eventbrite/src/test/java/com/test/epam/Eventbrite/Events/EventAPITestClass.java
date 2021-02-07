package com.test.epam.Eventbrite.Events;

import java.util.HashMap;
import java.util.Properties;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.response.Response;
import com.test.epam.Eventbrite.Helper.Effeciency;
import com.test.epam.Eventbrite.Helper.ResponseHelper;
import com.test.epam.Eventbrite.Helper.RestHelper;
import com.test.epam.Eventbrite.Utils.RestUtils;

public class EventAPITestClass {

	RestUtils restUtils;
	Effeciency effeciency;
	Properties property;
	RestHelper restHelper;
	SoftAssert softAssert;
	ResponseHelper responseHelper;
	
	public String instanceURL;
	public static String eventId;
	public static String copyEventId;
	
	@BeforeClass(alwaysRun = true)
	@Parameters({ "environment" })
	public void CreateAuth(String environment) throws Exception {
		restUtils = new RestUtils();
		effeciency = new Effeciency();
		property = effeciency.loadPropertyFile(environment);
		instanceURL = property.getProperty("baseURL");
		restHelper = new RestHelper(instanceURL, restUtils);
		responseHelper = new ResponseHelper();
		restUtils.setAccessToken(property.getProperty("authKey"));
	}

	/**
	 * @description: Verify Create new event functionality via rest api
	 * 
	 * @throws Exception
	 */
	@Test(description = "Create New Event under Specific Organization", groups = { "SmokeTest" })
	public void CreateEvent_API_01() throws Exception {
		String jsonFilePath = "/Events/CreateEvent_01.json";

		try {
			softAssert = new SoftAssert();

			HashMap<String, String> data = effeciency.readJsonElementInOrder(jsonFilePath, "Data");
			HashMap<String, String> createEventData = effeciency.readJsonElementInOrder(jsonFilePath,
					"createEventData");
			HashMap<String, String> expectedEventResponse = effeciency.readJsonElementInOrder(jsonFilePath,
					"verifyEventData");

			// Create New Event
			Response createEventResponse = restHelper.createEvent(data.get("organizationId"), createEventData);

			// Assert Response code
			softAssert.assertEquals(createEventResponse.getStatusCode(), 200, "Verify Status code of Response after Creating new Event");
			if (createEventResponse.getStatusCode() == 200) {

				// get Event ID
				eventId = responseHelper.getFieldValueFromResponse(createEventResponse, "id");
			
				// Verify Response of Event Creation API
				HashMap<String, String> actualEventResponse = responseHelper
						.updateEventCreationAPIResponse(createEventResponse, expectedEventResponse);

				for (String keyValue : expectedEventResponse.keySet()) {
					softAssert.assertEquals(actualEventResponse.get(keyValue).toString(),
							expectedEventResponse.get(keyValue).toString(), "Verify " + keyValue + " from Response");
				}
			}
			softAssert.assertAll();

		} catch (Exception e) {
			throw new Exception(e.toString());
		}
	}

	/**
	 * @description: Verify Get Event Information using Event Id
	 * 
	 * @throws Exception
	 */
	@Test(description = "Get Event Information using Event Id", groups = { "SmokeTest" }, dependsOnMethods = {
			"CreateEvent_API_01" }, priority= 2)
	public void GetEventInfo_API_01() throws Exception {
		String jsonFilePath = "/Events/GetEventInfo_01.json";

		try {
			softAssert = new SoftAssert();
			
			HashMap<String, String> data = effeciency.readJsonElementInOrder(jsonFilePath, "Data");
			HashMap<String, String> expectedEventResponse = effeciency.readJsonElementInOrder(jsonFilePath,
					"verifyEventData");

			// Get Event infor using Event Id
			Response getEventResponse = restHelper.getEventInfo(eventId);

			// Assert Response code
			softAssert.assertEquals(getEventResponse.getStatusCode(), 200, "Verify Status code of Response while getting Event information");
			if (getEventResponse.getStatusCode() == 200) {

				expectedEventResponse.put("organization_id", data.get("organizationId"));
				// Verify Response of Event Creation API
				HashMap<String, String> actualEventResponse = responseHelper
						.updateEventCreationAPIResponse(getEventResponse, expectedEventResponse);

				for (String keyValue : expectedEventResponse.keySet()) {
					softAssert.assertEquals(actualEventResponse.get(keyValue).toString(),
							expectedEventResponse.get(keyValue).toString(), "Verify " + keyValue + " from Response");
				}
			}
			softAssert.assertAll();

		} catch (Exception e) {
			throw new Exception(e.toString());
		}
	}

	/**
	 * @description: Update Existing Event
	 * 
	 * @throws Exception
	 */
	@Test(description = "Update Existing Event", groups = { "SmokeTest" }, dependsOnMethods = {
			"CreateEvent_API_01" }, priority= 3)
	public void UpdateEvent_API_01() throws Exception {
		String jsonFilePath = "/Events/UpdateEvent_01.json";

		try {
			softAssert = new SoftAssert();
			
			HashMap<String, String> updateEventData = effeciency.readJsonElementInOrder(jsonFilePath,
					"updateEventData");
			HashMap<String, String> expectedEventResponse = effeciency.readJsonElementInOrder(jsonFilePath,
					"verifyEventData");

			// Update Existing Event
			Response updateEventResponse = restHelper.updateEvent(eventId, updateEventData);

			// Assert Response code
			softAssert.assertEquals(updateEventResponse.getStatusCode(), 200, "Verify Status code of Response after Updating Event");
			if (updateEventResponse.getStatusCode() == 200) {

				// Verify Response of Update Event API
				HashMap<String, String> actualEventResponse = responseHelper
						.updateEventCreationAPIResponse(updateEventResponse, expectedEventResponse);

				for (String keyValue : expectedEventResponse.keySet()) {
					softAssert.assertEquals(actualEventResponse.get(keyValue).toString(),
							expectedEventResponse.get(keyValue).toString(), "Verify " + keyValue + " from Response");
				}
			}
			softAssert.assertAll();

		} catch (Exception e) {
			throw new Exception(e.toString());
		}
	}

	/**
	 * @description: Copy Existing Event
	 * 
	 * @throws Exception
	 */
	@Test(description = "Copy Existing Event", groups = { "SmokeTest" }, dependsOnMethods = {
			"CreateEvent_API_01" }, priority= 4)
	public void CopyEvent_API_01() throws Exception {
		String jsonFilePath = "/Events/CopyEvent_01.json";

		try {
			softAssert = new SoftAssert();
			
			HashMap<String, String> expectedEventResponse = effeciency.readJsonElementInOrder(jsonFilePath,
					"verifyEventData");

			// Copy Event from Existing One
			Response copyEventResponse = restHelper.copyEvent(eventId);

			// Assert Response code
			softAssert.assertEquals(copyEventResponse.getStatusCode(), 200,
					"Verify Status code of Response after copying Event");
			if (copyEventResponse.getStatusCode() == 200) {

				// get Event ID
				softAssert.assertNotEquals(responseHelper.getFieldValueFromResponse(copyEventResponse, "id"), eventId,
						"After Copying Event, Event ID shoudn't match");
				copyEventId = responseHelper.getFieldValueFromResponse(copyEventResponse, "id");

				// Verify Response of Update Event API
				HashMap<String, String> actualEventResponse = responseHelper
						.updateEventCreationAPIResponse(copyEventResponse, expectedEventResponse);

				for (String keyValue : expectedEventResponse.keySet()) {
					softAssert.assertEquals(actualEventResponse.get(keyValue).toString(),
							expectedEventResponse.get(keyValue).toString(), "Verify " + keyValue + " from Response");
				}
			}
			softAssert.assertAll();

		} catch (Exception e) {
			throw new Exception(e.toString());
		}
	}

	/**
	 * @description: Cancel Existing Event
	 * 
	 * @throws Exception
	 */
	@Test(description = "Cancel Existing Event", groups = { "SmokeTest" }, dependsOnMethods = {
			"CreateEvent_API_01" }, priority= 5)
	public void CancelEvent_API_01() throws Exception {
		
		try {
			softAssert = new SoftAssert();
			
			// Cancel Existing event
			Response cancelEventResponse = restHelper.cancelEvent(eventId);

			// Assert Response code
			softAssert.assertEquals(cancelEventResponse.getStatusCode(), 200,
					"Verify Status code of Response after cancelling Event");
			if (cancelEventResponse.getStatusCode() == 200) {

				// get Event ID
				softAssert.assertEquals(responseHelper.getFieldValueFromResponse(cancelEventResponse, "canceled"), "true",
						"Verifying Cancelled status after Cancelling Event");

			}
			softAssert.assertAll();

		} catch (Exception e) {
			throw new Exception(e.toString());
		}
	}

	/**
	 * @description: Delete Existing Event
	 * 
	 * @throws Exception
	 */
	@Test(description = "Delete Existing Event", groups = { "SmokeTest" }, dependsOnMethods = {
			"CreateEvent_API_01" }, priority = 6)
	public void DeleteEvent_API_01() throws Exception {
		
		try {
			softAssert = new SoftAssert();
			
			// Delete Existing Event
			Response deleteEventResponse = restHelper.deleteEvent(eventId);

			// Assert Response code
			softAssert.assertEquals(deleteEventResponse.getStatusCode(), 200,
					"Verify Status code of Response after Deleting Event");
			if (deleteEventResponse.getStatusCode() == 200) {

				// get Event ID
				softAssert.assertEquals(responseHelper.getFieldValueFromResponse(deleteEventResponse, "deleted"), "true",
						"Verifying Deleted status after Deleting Event");

			}
			
			// Delete copied Event
			restHelper.deleteEvent(copyEventId);
			softAssert.assertAll();

		} catch (Exception e) {
			throw new Exception(e.toString());
		}
	}

}

package com.test.epam.Eventbrite.Utils;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class RestUtils {
	private String accessToken;
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return this.accessToken;
	}
	
	/**
	 * Fetches the data
	 * @param url Provide the APIURI
	 * @return response
	 */
	public Response getData(String url) {
		return RestAssured.given()
				.header("Authorization", "Bearer " + accessToken)
				.header("Content-Type", "application/json")
				.get(url);
	}
	
	/**
	 * Creates the data with payload
	 * @param url Provide the APIURI
	 * @param body Provide the Request Body
	 * @return response
	 */
	public Response postWithPayLoad(String url, String body) {
		return RestAssured.given()
				.header("Authorization", "Bearer " + accessToken)
				.header("Content-Type", "application/json")
				.body(body)
				.post(url);
	}
	
	/**
	 * Creates the data without payload
	 * @param url Provide the APIURI
	 * @return response
	 */
	public Response postWithoutPayLoad(String url) {
		return RestAssured.given()
				.header("Authorization", "Bearer " + accessToken)
				.header("Content-Type", "application/json")
				.post(url);
	}

	/**
	 * Deletes the data without payload
	 * @param url Provide the APIURI
	 * @return response
	 */
	public Response deleteWithoutPayload(String url) {
		return RestAssured.given()
				.header("Authorization", "Bearer " + accessToken)
				.header("Content-Type", "application/json")
				.delete(url);
	}
	
}

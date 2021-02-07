package com.test.epam.Eventbrite.Utils;

public class URLGenerator {

	public String API_VERSION = "/v3";
	public String organizationBasedEventURL = "/organizations/{organization_id}/events/";
	public String eventURL = "/events/{event_id}/";
	public String copyEventURL = "/copy/";
	public String cancelEventURL = "/cancel/";
	public String venueBasedEventURL = "/venues/{venue_id}/events/";

	public URLGenerator(String instanceURL) {

		String baseURL = instanceURL + this.API_VERSION;
		this.organizationBasedEventURL = baseURL + this.organizationBasedEventURL;	
		this.eventURL = baseURL + this.eventURL;
		this.copyEventURL = this.eventURL + this.copyEventURL;
		this.cancelEventURL = this.eventURL + this.cancelEventURL;
		this.venueBasedEventURL = baseURL + venueBasedEventURL;
	}
}

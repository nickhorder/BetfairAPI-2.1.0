package com.nickhorder.betfairapi.api;

import com.nickhorder.betfairapi.entities.EventType;
import com.nickhorder.betfairapi.entities.MarketFilter;
import com.nickhorder.betfairapi.enums.ListEventTypesEnums;
import com.nickhorder.betfairapi.exceptions.APINGException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class EventTypeResult {
	private static Properties prop = new Properties();
	private static String bettingAPIURL;
	private EventType eventType ;
	private int marketCount;
	protected static final String FILTER = "filter";
	protected static final String LOCALE = "locale";
	protected static final String locale = Locale.getDefault().toString();

	static {
		try {
			InputStream in = EventTypeResult.class.getResourceAsStream("/apingdemo.properties");

			prop.load(in);
			in.close();

			bettingAPIURL = prop.getProperty("BETTING_URL");

		} catch (IOException e) {
			System.out.println("Error loading the properties file: " + e);
		}
	}





	public static List<EventTypeResult> listEventTypes(MarketFilter filter, String appKey, String ssoId) throws APINGException, IOException {
		Map<String, Object> params = new HashMap<>();
		params.put(FILTER, filter);
		params.put(LOCALE, locale);
		String result = HttpApiCaller.makeRequest(ListEventTypesEnums.LISTEVENTTYPES.getOperationName(), params, appKey, ssoId, bettingAPIURL);
		//  if(ApiNGAuthMain.isDebug())
		//     System.out.println("\nlistEventTypes Response: "+result);

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<EventTypeResult> container = objectMapper.readValue(result, new TypeReference<List<EventTypeResult>>() {});
	//	List<EventTypeResult> container = JsonConverter.convertFromJson(result, new TypeToken<List<EventTypeResult>>() {}.getType());

		return container;

	}
	
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	public int getMarketCount() {
		return marketCount;
	}
	public void setMarketCount(int marketCount) {
		this.marketCount = marketCount;
	}

}

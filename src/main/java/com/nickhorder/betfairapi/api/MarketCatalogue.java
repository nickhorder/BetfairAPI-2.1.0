package com.nickhorder.betfairapi.api;

import com.nickhorder.betfairapi.entities.*;
import com.nickhorder.betfairapi.enums.FlowControllerEnums;
import com.nickhorder.betfairapi.enums.MarketProjection;
import com.nickhorder.betfairapi.enums.MarketSort;
import com.nickhorder.betfairapi.exceptions.APINGException;
//import com.betfair.aping.util.JsonConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nickhorder.betfairapi.entities.*;

import java.io.IOException;
import java.util.*;

public class MarketCatalogue {

	private String marketId;
	private String marketName;
	private MarketDescription description;
	private String marketStartTime;
	private List<RunnerCatalog> runners = null;
	private EventType eventType;
	private Competition competition;
	private Event event;
	protected static final String FILTER = "filter";
	protected static final String SORT = "sort";
	protected static final String LOCALE = "locale";
	protected static final String locale = Locale.getDefault().toString();
	protected static final String MAX_RESULT = "maxResults";
	protected static final String MARKET_PROJECTION = "marketProjection";

	public static List<MarketCatalogue> listMarketCatalogue(MarketFilter filter, Set<MarketProjection> marketProjection,
															MarketSort sort, String maxResult, String appKey, String ssoId) throws APINGException, IOException {
		Map<String, Object> params = new HashMap<>();
		params.put(LOCALE, locale);
		params.put(FILTER, filter);
		params.put(SORT, sort);
		params.put(MAX_RESULT, maxResult);
		params.put(MARKET_PROJECTION, marketProjection);
		String result = HttpApiCaller.makeRequest(FlowControllerEnums.LISTMARKETCATALOGUE.getOperationName(), params, appKey, ssoId);
		//  if(ApiNGAuthMain.isDebug())
		System.out.println("\nlistMarketCatalogue Response: "+result);

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<MarketCatalogue> container = objectMapper.readValue(result, new TypeReference<List<MarketCatalogue>>() {});
	//	List<MarketCatalogue> container = JsonConverter.convertFromJson(result, new TypeToken< List<MarketCatalogue>>(){}.getType() );

		return container;

	}



	public String getMarketId() {
		return marketId;
	}

	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public MarketDescription getDescription() {
		return description;
	}

	public void setDescription(MarketDescription description) {
		this.description = description;
	}

	public String getMarketStartTime() {return marketStartTime; }
	public void setMarketStartTime(String marketStartTime) {this.marketStartTime = marketStartTime;}

	public List<RunnerCatalog> getRunners() {
		return runners;
	}

	public void setRunners(List<RunnerCatalog> runners) {
		this.runners = runners;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public Competition getCompetition() {
		return competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String toString() {
		return getMarketName();
	}

}

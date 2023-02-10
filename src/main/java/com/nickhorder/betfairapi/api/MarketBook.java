package com.nickhorder.betfairapi.api;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nickhorder.betfairapi.entities.*;
import com.nickhorder.betfairapi.enums.FlowControllerEnums;
import com.nickhorder.betfairapi.enums.MatchProjection;
import com.nickhorder.betfairapi.enums.OrderProjection;
import com.nickhorder.betfairapi.exceptions.APINGException;
//import com.betfair.aping.util.JsonConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nickhorder.betfairapi.entities.PriceProjection;
import com.nickhorder.betfairapi.entities.PriceSize;
import com.nickhorder.betfairapi.entities.Runner;

import java.io.IOException;
import java.util.List;
import java.util.*;



public class MarketBook {
	private String marketId;

	private Boolean isMarketDataDelayed;
	private String status;
	private int betDelay;
	private Boolean bspReconciled;
	private Boolean complete;
	private Boolean inplay;
	private int numberOfWinners;
	private int numberOfRunners;
	private int numberOfActiveRunners;
	private Date lastMatchTime;
	private Double totalMatched;
	private Double totalAvailable;
	private Boolean crossMatching;
	private Boolean runnersVoidable;
	private Long version;

 	private List<Runner> runners;
	private List<PriceSize> price;
	protected static final String locale = Locale.getDefault().toString();
	protected static final String MARKET_IDS = "marketIds";
	protected static final String LOCALE = "locale";
	protected static final String PRICE_PROJECTION = "priceProjection";
	protected static final String MATCH_PROJECTION = "matchProjection";
	protected static final String ORDER_PROJECTION = "orderProjection";

	public static List<MarketBook> listMarketBook(List<String> marketIds, PriceProjection priceProjection, OrderProjection orderProjection,
												  MatchProjection matchProjection, String currencyCode, String appKey, String ssoId) throws APINGException, IOException {
		Map<String, Object> params = new HashMap<>();
		params.put(LOCALE, locale);
		params.put(MARKET_IDS, marketIds);
		params.put(PRICE_PROJECTION, priceProjection);
		params.put(ORDER_PROJECTION, orderProjection);
		params.put(MATCH_PROJECTION, matchProjection);
		String result = HttpApiCaller.makeRequest(FlowControllerEnums.LISTMARKETBOOK.getOperationName(), params, appKey, ssoId);

		final ObjectMapper objectMapper = new ObjectMapper();
		// 	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<MarketBook> container = objectMapper.readValue(result, new TypeReference<List<MarketBook>>() {});

	//	List<MarketBook> container = JsonConverter.convertFromJson(result, new TypeToken<List<MarketBook>>() {
	//	}.getType());
	//	 System.out.println("Arrays?" + Arrays.toString(container.toArray()));

		return container;

	}

	public List<PriceSize> getPrice() {
		return price;
	}

	public void setPrice(List<PriceSize> price) {
		this.price = price;
	}

 	public List<Runner> getRunners() {
 		return runners;
 	}

 	public void setRunners(List<Runner> runners) {
 		this.runners = runners;
 	}

	public String getMarketId() {
		return marketId;
	}

	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}

	public Boolean getIsMarketDataDelayed() {
		return isMarketDataDelayed;
	}

	public void setIsMarketDataDelayed(Boolean isMarketDataDelayed) {
		this.isMarketDataDelayed = isMarketDataDelayed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getBetDelay() {
		return betDelay;
	}

	public void setBetDelay(int betDelay) {
		this.betDelay = betDelay;
	}

	public Boolean getBspReconciled() {
		return bspReconciled;
	}

	public void setBspReconciled(Boolean bspReconciled) {
		this.bspReconciled = bspReconciled;
	}

	public Boolean getComplete() {
		return complete;
	}

	public void setComplete(Boolean complete) {
		this.complete = complete;
	}

	public Boolean getInplay() {
		return inplay;
	}

	public void setInplay(Boolean inplay) {
		this.inplay = inplay;
	}

	public int getNumberOfWinners() {
		return numberOfWinners;
	}

	public void setNumberOfWinners(int numberOfWinners) {
		this.numberOfWinners = numberOfWinners;
	}

	public int getNumberOfRunners() {
		return numberOfRunners;
	}

	public void setNumberOfRunners(int numberOfRunners) {
		this.numberOfRunners = numberOfRunners;
	}

	public int getNumberOfActiveRunners() {
		return numberOfActiveRunners;
	}

	public void setNumberOfActiveRunners(int numberOfActiveRunners) {
		this.numberOfActiveRunners = numberOfActiveRunners;
	}

	public Date getLastMatchTime() {
		return lastMatchTime;
	}

	public void setLastMatchTime(Date lastMatchTime) {
		this.lastMatchTime = lastMatchTime;
	}

	public Double getTotalMatched() {
		return totalMatched;
	}

	public void setTotalMatched(Double totalMatched) {
		this.totalMatched = totalMatched;
	}

	public Double getTotalAvailable() {
		return totalAvailable;
	}

	public void setTotalAvailable(Double totalAvailable) {
		this.totalAvailable = totalAvailable;
	}

	public Boolean getCrossMatching() {
		return crossMatching;
	}

	public void setCrossMatching(Boolean crossMatching) {
		this.crossMatching = crossMatching;
	}

	public Boolean getRunnersVoidable() {
		return runnersVoidable;
	}

	public void setRunnersVoidable(Boolean runnersVoidable) {
		this.runnersVoidable = runnersVoidable;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String toString() {
		return "marketId=" + getMarketId() + "\n"
				+ "isMarketDataDelayed=" + getIsMarketDataDelayed() + "\n"
				+ "status=" + getStatus() + "\n"
				+ "betDelay=" + getBetDelay() + "\n"
				+ "bspReconciled=" + getBspReconciled() + "\n"
				+ "complete=" + getComplete() + "\n"
				+ "inplay=" + getInplay() + "\n"
				+ "numberOfWinners=" + getNumberOfWinners() + "\n"
				+ "numberOfRunners=" + getNumberOfRunners() + "\n"
				+ "numberOfActiveRunners=" + getNumberOfActiveRunners() + "\n"
				+ "lastMatchTime=" + getLastMatchTime()  + "\n"
				+ "totalMatched=" + getTotalMatched()  + "\n"
				+ "totalAvailable="	+ getTotalAvailable()  + "\n"
				+ "crossMatching=" + getCrossMatching()  + "\n"
				+ "runnersVoidable=" + getRunnersVoidable()  + "\n"
				+ "version=" + getVersion()  + "\n"
	 		    + "runners=" + getRunners()  + "\n";
	}


	}

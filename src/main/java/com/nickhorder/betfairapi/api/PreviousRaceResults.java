package com.nickhorder.betfairapi.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nickhorder.betfairapi.entities.ExchangePrices;
import com.nickhorder.betfairapi.entities.PreviousRunner;
import com.nickhorder.betfairapi.entities.Runner;
import com.nickhorder.betfairapi.enums.ListEventTypesEnums;
import com.nickhorder.betfairapi.exceptions.APINGException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PreviousRaceResults {

	private static Properties prop = new Properties();
	private static String bettingAPIURL;
	private String marketId;
	private Boolean isMarketDataDelayed;
	private String status;
	private Boolean bspReconciled;
	private Boolean complete;
 	private List<PreviousRunner> runners;
	protected static final String locale = Locale.getDefault().toString();
	protected static final String MARKET_IDS = "marketIds";
	protected static final String LOCALE = "locale";
	protected static final String PRICE_PROJECTION = "priceProjection";
	protected static final String MATCH_PROJECTION = "matchProjection";
	protected static final String ORDER_PROJECTION = "orderProjection";

	static {
		try {
			InputStream in = PreviousRaceResults.class.getResourceAsStream("/apingdemo.properties");

			prop.load(in);
			in.close();

			bettingAPIURL = prop.getProperty("BETTING_URL");

		} catch (IOException e) {
			System.out.println("Error loading the properties file: " + e);
		}
	}

	public static List<PreviousRaceResults> listPreviousMarketBook(List<String> lastBetMarketID,
																   String appKey, String ssoId) throws APINGException, IOException {
		Map<String, Object> params = new HashMap<>();
		params.put(LOCALE, locale);
		params.put(MARKET_IDS, lastBetMarketID);
		System.out.println("Params: " + params);
		String result = HttpApiCaller.makeRequest(ListEventTypesEnums.LISTMARKETBOOK.getOperationName(), params, appKey, ssoId, bettingAPIURL);

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<PreviousRaceResults> container = objectMapper.readValue(result, new TypeReference<List<PreviousRaceResults>>() {});
	 //	 System.out.println("MarketBook Response: " + container);
        // Clear down lastBetMarketID list, so that we only ever pass the last Market ID here.
		 lastBetMarketID.clear();

		return container;
	}


	public static boolean lastFavouriteWonOrLost(List<Long> lastFavouriteSelectionID,  PreviousRaceResults lastRaceResults) {

		//2. Create a new list to capture the WINNER.
		List<PreviousRunner> previousRunnerList = lastRaceResults.getRunners();
		List<PreviousRunner> previousWinnerList = new ArrayList<>();

		if (previousRunnerList != null) {
			for (PreviousRunner horse : previousRunnerList) {
				String winner = "WINNER";
				if (horse.getStatus().equals(winner)) {
					previousWinnerList.add(horse);
					System.out.println(previousWinnerList.size() + "Previous Winner was: " + previousWinnerList);
				}
			}
		}
		Long previousWinnerSelectionID = null;
		boolean lastFavouriteWon;
		for (PreviousRunner horse : previousWinnerList) {
			previousWinnerSelectionID = horse.getSelectionId();
		//	System.out.println("PWSI " + previousWinnerSelectionID);
		}
		Long immediateLastFavourite = lastFavouriteSelectionID.get(0);
		if (immediateLastFavourite.equals(previousWinnerSelectionID)){
			lastFavouriteWon = true;
			System.out.println("Match. The last favourite Selection ID was " + immediateLastFavourite + "," +
					"and the winner of the last race was Selection ID " + previousWinnerSelectionID);
		}
		else{
			lastFavouriteWon = false;
			System.out.println("No Match. The last favourite Selection ID was " + immediateLastFavourite + "," +
					"and the winner of the last race was Selection ID " + previousWinnerSelectionID);
		}

		return lastFavouriteWon;
	}




 	public List<PreviousRunner> getRunners() {
 		return runners;
 	}

 	public void setRunners(List<PreviousRunner> runners) {
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


	public String toString() {
		return "marketId=" + getMarketId() + "\n"
		 		+ "isMarketDataDelayed=" + getIsMarketDataDelayed() + "\n"
		    	+ "Race Status=" + getStatus() + "\n"
			//	+ "Bet Delay=" + getBetDelay() + "\n"
		 		+ "bspReconciled=" + getBspReconciled() + "\n"
		 		+ "complete=" + getComplete() + "\n"
		//		+ "inplay=" + getInplay() + "\n"
		//		+ "numberOfWinners=" + getNumberOfWinners() + "\n"
		//  		+ "numberOfRunners=" + getNumberOfRunners() + "\n"
		//		+ "numberOfActiveRunners=" + getNumberOfActiveRunners() + "\n"
		//		+ "runnersVoidable=" + getRunnersVoidable()  + "\n"
		//		+ "version=" + getVersion()  + "\n"
	 		    + "runners=" + getRunners()  + "\n";
	}


	}

package com.nickhorder.betfairapi.enums;

public enum FlowControllerEnums {
	LISTEVENTTYPES("listEventTypes"), 
	LISTCOMPETITIONS("listCompetitions"),
	LISTTIMERANGES("listTimeRanges"),
	LISTEVENTS("listEvents"),
	LISTMARKETTYPES("listMarketTypes"),
	LISTCOUNTRIES("listCountries"),
	LISTVENUES("listVenues"),
	LISTMARKETCATALOGUE("listMarketCatalogue"),
	LISTMARKETBOOK("listMarketBook"),
	PLACORDERS("placeOrders");
	
	private String operationName;
	
	private FlowControllerEnums(String operationName){
		this.operationName = operationName;
	}

	public String getOperationName() {
		return operationName;
	}

	

}

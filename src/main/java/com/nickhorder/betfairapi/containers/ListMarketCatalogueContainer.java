package com.nickhorder.betfairapi.containers;

import com.nickhorder.betfairapi.api.MarketCatalogue;

import java.util.List;

public class ListMarketCatalogueContainer extends Container {

	private List<MarketCatalogue> result;

	public List<MarketCatalogue> getResult() {
		return result;
	}

	public void setResult(List<MarketCatalogue> result) {
		this.result = result;
	}

}

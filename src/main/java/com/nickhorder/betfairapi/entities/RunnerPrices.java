package com.nickhorder.betfairapi.entities;
import java.util.List;

public class RunnerPrices {

	private String marketId;
	private String selectionId;
	private String availableToBack;
	private int price;

	public String getMarketId() {
		return marketId;
	}

	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}
	public String getSelectionId() {
		return selectionId;
	}

	public void setSelectionId(String selectionId) {
		this.selectionId = selectionId;
	}

	public String getAvailableToBack() {
		return availableToBack;
	}

	public void setAvailableToBack(String availableToBack) {
		this.availableToBack = availableToBack;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String toString() {
		return  "marketId=" + getMarketId() + "\n"
				+ "selectionId=" + getSelectionId() + "\n"
			 	+ "availableToBack=" + getAvailableToBack() + "\n"
				+ "price=" + getPrice() + "\n";
	}

}

package com.nickhorder.betfairapi.entities;
import java.util.Date;
import java.util.List;

import com.nickhorder.betfairapi.api.MarketBook;

public class Runner extends MarketBook {
	private Long selectionId;
	private Double handicap;
 	private String status;
	private Double adjustmentFactor;
	private Double lastPriceTraded;
	private Double runnerTotalMatched;
	private Date removalDate;
	private StartingPrices sp;
	private StartingPrices nearPrice;
	//more StartingPrices to put in here if necessary
	private ExchangePrices ex;
	private  List<PriceSize> availableToBack;
	private  List<PriceSize> availableToLay;
	private  List<PriceSize> tradedVolume;
	private List<Order> orders;
	private List<Match> matches;
	private Double runnerPrice;
	private Double size;


	public Double getRunnerPrice() {
		return runnerPrice;
	}

	public void setRunnerPrice(Double price) {
		this.runnerPrice = runnerPrice;
	}

	public Long getSelectionId() {
		return selectionId;
	}

	public void setSelectionId(Long selectionId) {
		this.selectionId = selectionId;
	}

	public Double getHandicap() {
		return handicap;
	}

	public void setHandicap(Double handicap) {
		this.handicap = handicap;
	}

 	public String getRunnerStatus() {
 	return status;
 	}

 	public void setRunnerStatus(String status) {
 		this.status = status;
 	}

	public Double getAdjustmentFactor() {
		return adjustmentFactor;
	}

	public void setAdjustmentFactor(Double adjustmentFactor) {
		this.adjustmentFactor = adjustmentFactor;
	}

	public Double getLastPriceTraded() {
		return lastPriceTraded;
	}

	public void setLastPriceTraded(Double lastPriceTraded) {
		this.lastPriceTraded = lastPriceTraded;
	}

	public Double getRunnerTotalMatched() {
		return runnerTotalMatched;
	}

	public void seRunnerTotalMatched(Double runnerTotalMatched) {
		this.runnerTotalMatched = runnerTotalMatched;
	}

	public Date getRemovalDate() {
		return removalDate;
	}

	public void setRemovalDate(Date removalDate) {
		this.removalDate = removalDate;
	}

	public StartingPrices getSp() {
		return sp;
	}

	public void setSp(StartingPrices sp) {
		this.sp = sp;
	}

	public ExchangePrices getEx() {
		return ex;
	}

	public void setEx(ExchangePrices ex) {
		this.ex = ex;
	}

	public  List<PriceSize> getAvailableToBack() {
		return availableToBack;
	}

	public void setAvailableToBack(List<PriceSize> availableToBack) {
		this.availableToBack = availableToBack;
	}

	public List<PriceSize> getAvailableToLay() {
		return availableToLay;
	}

	public void setAvailableToLay(List<PriceSize> availableToLay) {
		this.availableToLay = availableToLay;
	}

	public List<PriceSize> getTradedVolume() {
		return tradedVolume;
	}

	public void setTradedVolume(List<PriceSize> tradedVolume) {
		this.tradedVolume = tradedVolume;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Match> getMatches() {
		return matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	public String toString() {
		return  "selectionId=" + getSelectionId() + "\n"
				+ "handicap=" + getHandicap() + "\n"
		//		+ "runnerStatus" + getRunnerStatus() + "\n"
				+ "Runner Status=" + getStatus() + "\n"
				+ "adjustmentFactor=" + getAdjustmentFactor() + "\n"
				+ "lastPriceTraded=" + getLastPriceTraded() + "\n"
				+ "totalMatched=" + getTotalMatched() + "\n"
				+ "removalDate=" + getRemovalDate() + "\n"
				+ "sp=" + getSp() + "\n"
				//SP data not returned on Development key
				//+ "ex="	+ getEx() + "\n"
				+ "ATL Prices=" + getEx().getAvailableToLay() + "\n"
				+ "ATB Prices=" + getEx().getAvailableToBack() + "\n"
				+ "orders=" + getOrders() + "\n"
				+ "matches=" + getMatches() + "\n" ;
	}

}
package com.nickhorder.betfairapi.entities;

import com.nickhorder.betfairapi.api.MarketBook;

public class PreviousRunner extends MarketBook {
	private Long selectionId;
	private Double handicap;
 	private String status;
	private Double adjustmentFactor;

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

 	public String getStatus() {
 	return status;
 	}

 	public void setStatus(String status) {
 		this.status = status;
 	}

	public Double getAdjustmentFactor() {
		return adjustmentFactor;
	}

	public void setAdjustmentFactor(Double adjustmentFactor) {
		this.adjustmentFactor = adjustmentFactor;
	}


	public String toString() {
		return  "selectionId=" + getSelectionId() + "\n"
				+ "handicap=" + getHandicap() + "\n"
				+ "Runner Status=" + getStatus() + "\n"
				+ "adjustmentFactor=" + getAdjustmentFactor() + "\n";
	}

}
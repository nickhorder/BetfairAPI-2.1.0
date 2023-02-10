package com.nickhorder.betfairapi.entities;

import com.nickhorder.betfairapi.exceptions.APINGException;

public class Data {

	private com.nickhorder.betfairapi.exceptions.APINGException APINGException;

	public APINGException getAPINGException() {
		return APINGException;
	}

	public void setAPINGException(APINGException aPINGException) {
		APINGException = aPINGException;
	}

}

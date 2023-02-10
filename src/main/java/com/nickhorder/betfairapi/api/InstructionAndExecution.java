package com.nickhorder.betfairapi.api;

//import com.betfair.aping.entities.PlaceExecutionReport;
import com.nickhorder.betfairapi.entities.PlaceInstruction;
import com.nickhorder.betfairapi.enums.*;
import com.nickhorder.betfairapi.exceptions.APINGException;
//import com.betfair.aping.util.JsonConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nickhorder.betfairapi.enums.*;

import java.io.IOException;
import java.util.*;

public class InstructionAndExecution {
	//InstructionReport Fields
	private InstructionReportStatus instructionReportStatus;
	private InstructionReportErrorCode instructionReportErrorCode;
	private PlaceInstruction instructionl;
	private String betId;
	private Date placedDate;
	private double averagePriceMatched;
	private double sizeMatched;
	protected static final String MARKET_ID = "marketId";
	protected static final String INSTRUCTIONS = "instructions";
	protected static final String CUSTOMER_REF = "customerRef";
	protected static final String LOCALE = "locale";
	protected static final String locale = Locale.getDefault().toString();

	//PlaceExecutionReport Fields
	private String customerRef;
	private ExecutionReportStatus executionReportStatus;
	private ExecutionReportErrorCode executionReportErrorCode;
	private String marketId;
	private List<InstructionAndExecution> instructionReports;


	public static InstructionAndExecution placeOrders(String marketId, List<PlaceInstruction> instructions, String customerRef , String appKey, String ssoId) throws APINGException, IOException {
		Map<String, Object> params = new HashMap<>();
		params.put(LOCALE, locale);
		params.put(MARKET_ID, marketId);
		params.put(INSTRUCTIONS, instructions);
		params.put(CUSTOMER_REF, customerRef);
		String result = HttpApiCaller.makeRequest(FlowControllerEnums.PLACORDERS.getOperationName(), params, appKey, ssoId);
		//if(ApiNGAuthMain.isDebug())
		System.out.println("\nplaceOrders Response: "+result);

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper.readValue(result, InstructionAndExecution.class);
	//	return JsonConverter.convertFromJson(result, InstructionAndExecution.class);

	}


	//PlaceExecutionReport Getters/Setters

	public String getCustomerRef() {
		return customerRef;
	}

	public void setCustomerRef(String customerRef) {
		this.customerRef = customerRef;
	}

	public ExecutionReportStatus getExecutionReportStatus() {
		return executionReportStatus;
	}

	public void setExecutionReportStatus(ExecutionReportStatus executionReportStatus) {
		this.executionReportStatus = executionReportStatus;
	}

	public ExecutionReportErrorCode getExecutionReportErrorCode() {
		return executionReportErrorCode;
	}

	public void setExecutionReportErrorCode(ExecutionReportErrorCode executionReportErrorCode) {
		this.executionReportErrorCode = executionReportErrorCode;
	}

	public String getMarketId() {
		return marketId;
	}

	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}

	public List<InstructionAndExecution> getInstructionReports() {
		return instructionReports;
	}

	public void setInstructionReports(
			List<InstructionAndExecution> instructionReports) {
		this.instructionReports = instructionReports;
	}


	//InstructionReport Getters/Setters
	public InstructionReportStatus getInstructionReportStatus() {
		return instructionReportStatus;
	}

	public void setInstructionReportStatus(InstructionReportStatus instructionReportStatus) {
		this.instructionReportStatus = instructionReportStatus;
	}

	public InstructionReportErrorCode getInstructionReportErrorCode() {
		return instructionReportErrorCode;
	}

	public void setInstructionReportErrorCode(InstructionReportErrorCode instructionReportErrorCode) {
		this.instructionReportErrorCode = instructionReportErrorCode;
	}

	public PlaceInstruction getInstructionl() {
		return instructionl;
	}

	public void setInstructionl(PlaceInstruction instructionl) {
		this.instructionl = instructionl;
	}

	public String getBetId() {
		return betId;
	}

	public void setBetId(String betId) {
		this.betId = betId;
	}

	public Date getPlacedDate() {
		return placedDate;
	}

	public void setPlacedDate(Date placedDate) {
		this.placedDate = placedDate;
	}

	public double getAveragePriceMatched() {
		return averagePriceMatched;
	}

	public void setAveragePriceMatched(double averagePriceMatched) {
		this.averagePriceMatched = averagePriceMatched;
	}

	public double getSizeMatched() {
		return sizeMatched;
	}

	public void setSizeMatched(double sizeMatched) {
		this.sizeMatched = sizeMatched;
	}

	public String toString() {
		return "customerRef=" + getCustomerRef() + "\n"
				+ "status=" + getExecutionReportStatus() + "\n"
				+ "marketId=" + getMarketId() + "\n"
				+ "instructionReports=" + getInstructionReports() + "\n"
		        + "instructionStatus" + getInstructionReportStatus() + "\n"
				+ "instruction=" + getInstructionl();
	}




}

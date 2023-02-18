package com.nickhorder.betfairapi.enums;

public enum AccountFundsEnums {

    GETACCOUNTFUNDS("getAccountFunds"),
    ALL("Expired"),
    ACTIVATED("Activated"),
    UNACTIVATED("Unactivated"),
    CANCELLED("Cancelled"),
    EXPIRED("Expired"),
    SUCCESS("Success"),
    UNKNOWN("Unknown"),
    UK("UK"),
    DEPOSITS_WITHDRAWALS("Deposits/Withdrawals"),
    EXCHANGE("Exchange"),
    POKER_ROOM("Poker Room"),
    RESULT_ERR("Result Error"),
    RESULT_FIX("Result Fix"),
    RESULT_LOST("Result Lost"),
    RESULT_NOT_APPLICABLE("Result Not Applicable"),
    RESULT_WON("Result Won"),
    COMMISSION_REVERSAL("Commission Reversal"),
    AUTHORIZATION_CODE("Authorization Code"),
    REFRESH_TOKEN("Refresh Token"),
    BEARER("Bearer"),
    INVALID_USER("Invalid User"),
    AFFILIATED("Affiliated"),
    NOT_AFFILIATED("Not Affiliated");

    private String operationName;

    private AccountFundsEnums(String operationName){
        this.operationName = operationName;
    }

    public String getOperationName() {
        return operationName;
    }

}

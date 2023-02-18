package com.nickhorder.betfairapi.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nickhorder.betfairapi.enums.AccountFundsEnums;
import com.nickhorder.betfairapi.exceptions.APINGException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class AccountFunds {

    private static Properties prop = new Properties();
    private static String accountsAPIURL;

    private Double availableToBetBalance; //Amount available to bet.
    private Double exposure;              //Current exposure
    private Double retainedCommission;    //  Sum of retained commission.
    private Double exposureLimit;         //   Exposure limit.
    private Double discountRate;          //   User Discount Rate.
    private int pointsBalance;            //The Betfair points balance.
    private String wallet;                // Wallet location (i.e UK)

    //Initialize and load in Properties file
    static {
        try {
            InputStream in = AccountFunds.class.getResourceAsStream("/apingdemo.properties");

            prop.load(in);
            in.close();

            accountsAPIURL = prop.getProperty("ACCOUNTS_URL");

        } catch (IOException e) {
            System.out.println("Error loading the properties file: " + e);
        }
    }

    public static AccountFunds getAccountFunds(String appKey, String ssoId) throws APINGException, IOException {
        Map<String, Object> params = new HashMap<>();
        String result = HttpApiCaller.makeRequest(AccountFundsEnums.GETACCOUNTFUNDS.getOperationName(), params, appKey, ssoId, accountsAPIURL);

           System.out.println("getAccountFunds Response: " + result);

        final ObjectMapper objectMapper = new ObjectMapper();
       // objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
   //     List<AccountFunds> container = objectMapper.readValue(result, new TypeReference<List<AccountFunds>>() {});
        return objectMapper.readValue(result, AccountFunds.class);

      //  return container;

    }

    public Double getAvailableToBetBalance() {
        return availableToBetBalance;
    }

    public void setAvailableToBetBalance(Double availableToBetBalance) {
        this.availableToBetBalance = availableToBetBalance;
    }
    public Double getExposure() {
        return exposure;
    }

    public void setExposure(Double exposure) {
        this.exposure = exposure;
    }

    public Double getRetainedCommission() {
        return retainedCommission;
    }

    public void setRetainedCommission(Double retainedCommission) {
        this.retainedCommission = retainedCommission;
    }

    public Double getExposureLimit() {
        return exposureLimit;
    }

    public void setExposureLimit(Double exposureLimit) {
        this.exposureLimit = exposureLimit;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public int getPointsBalance() {
        return pointsBalance;
    }

    public void setPointsBalance(int pointsBalance) {
        this.pointsBalance = pointsBalance;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String toString() {
        return  "availableToBetBalance=" + getAvailableToBetBalance() + "\n" +
                "Exposure=" + getExposure() + "\n" +
                "RetainedCommmission=" + getRetainedCommission() + "\n" +
                "ExposureLimit=" + getExposureLimit() + "\n" +
                "DiscountRate=" + getDiscountRate() + "\n" +
                "PointsBalance=" + getPointsBalance() + "\n" +
                "Wallet=" + getWallet();
    }
}


//}

package com.nickhorder.betfairapi.api;

import com.nickhorder.betfairapi.entities.Login;
import com.nickhorder.betfairapi.entities.SyntheticBalanceSheet;
import com.nickhorder.betfairapi.exceptions.APINGException;

import java.io.*;
import java.text.ParseException;
import java.util.*;


/**
  Some comments here
 *
 */

public class ApiNGAuthMain {

     private static Properties prop = new Properties();
     private static String applicationKey;
     private static String sessionToken;
     private static String username;
     private static String password;
     private static String keyStorePassword;
     private static boolean debug;
     private static int port = 443;

     public static Properties getProp() {
         return prop;
     }

     public static boolean isDebug() {
         return debug;
     }


     static {
         try {
             InputStream in = ApiNGAuthMain.class.getResourceAsStream("/apingdemo.properties");
             prop.load(in);
             in.close();

             debug = Boolean.parseBoolean(prop.getProperty("DEBUG"));

         } catch (IOException e) {
             System.out.println("Error loading the properties file: " + e);
         }
     }

     public static void main(String[] args) throws APINGException, ParseException, IOException {


         // 1.
         // Check whether the program can start at the time the user has requested.

         if (TimeHandler.programStartStop(true) == true)
         {
             System.out.println("Welcome to the Betfair API NG!. This program runs within an operating" +
                     " window of "+ TimeHandler.getStartTimeInConsole() + " to "
                     + TimeHandler.getEndTimeInConsole() + ".");
         }
         else{
             System.out.println("This program can be started at or after " + TimeHandler.getStartTimeInConsole() +
              " .Please return after that time.");
         }

         // 2.
         // Check if we have all args needed for automatic login (appkey,username,password).
         // And that the program is being started in an acceptable time range.
         if (args.length >= 3 && TimeHandler.getProgramRunning()) {

             applicationKey = args[0];
             username = args[1];
             password = args[2];
             keyStorePassword = args[3];

             System.out.println("Conducting automatic login.");

             //Call authCall method to login via Keystore.
             HttpAuth authCall = new HttpAuth();
             Login newLogin = authCall.sendAuthRequest(port, applicationKey, username, password, keyStorePassword);
             if(newLogin.getLoginStatus().equals("SUCCESS")) {

                 sessionToken = newLogin.getSessionToken();
                 System.out.println("Your session token is: " + sessionToken);
             }
             else{
                  System.out.println("Call to obtain Session Token Failed.");
             }

             // 3.
             // Call ExcelRW to obtain Stake Multiplier Excel File into HashMap, so HashMap can be re-used during
             // the day, instead of repeated I/O on the Excel file.

                   NavigableMap<Double,Double> stakeMultiplierMap = ExcelRW.createStakeMultiplierMap();
              //     NavigableMap<Double,Double> stakeMultiplierHashMap = ExcelRW.sortStakeMultiplierMap(sm);
                  // System.out.println("is this also blank?" + stakeMultiplierHashMap);

             // 4.
             // Call ExcelRW to obtain SyntheticBalance Excel File into a List, which will be read and added to
             // during the course of the betting day.

             List<SyntheticBalanceSheet> daySyntheticBalanceSheet = ExcelRW.importSyntheticBalanceSheet();
           //  System.out.println("daySyntheticBalanceSheet Size: " + daySyntheticBalanceSheet.size());
         //    System.out.println("First Entry of Bal: " + daySyntheticBalanceSheet.get(0));
         //   System.out.println("All List: " + daySyntheticBalanceSheet);
          //   System.out.println("2nd: Selection ID?  " +
         //            daySyntheticBalanceSheet.get(1).getSyntheticBalanceSheetSelectionID());

             // 5.
             //Initialise betCounter as 0. Will route the first bet of the day past the update for
             // Synthetic Balance Sheet; that can only come after the first race.
             int betCounter = 0;

             // 6.
             // Initialise lastBetMarketID list. This will receive the last MarketID we considered (whether
             // Or not we placed a bet on it, we will need to update the Synthetic Balance. Note that the list is
             // Cleared down in PreviousRaceResults, after the last race is pulled, and then added to again
             // as we loop over APINGFlowController

             List<String> lastBetMarketID = new ArrayList<>();

             // 7.
             // Initialise lastFavouriteSelectionID list. This will receive the Selection ID for the race's Favourite,
             // Which then becomes the last race's Favourite once we start a new loop, and is used for matching to
             // Check whether the last favourite won.
             List<Long> lastFavouriteSelectionID = new ArrayList<>();

             // 8.
             // Initialise lastFavouriteSyntheticStake and lastFavouriteSyntheticReturns. Same principle as above.
             List<Double> lastFavouriteSyntheticStake = new ArrayList<>();
             List<Double> lastFavouriteSyntheticReturns = new ArrayList<>();

             // X.
             // ApiNGFlowController will run in a loop throughout the day, until shutdown criteria met.

             for (int runCount = 0; runCount < 2; runCount++) {
                 if (runCount > 0){
                     betCounter += 1;
                     lastBetMarketID.add(lastBetMarketID.get(0));
                     lastFavouriteSelectionID.add(lastFavouriteSelectionID.get(0));
                 }
                 //call instance of ApiNGFlowController with sessionToken
                 ApiNGFlowController goBetting = new ApiNGFlowController();
                 goBetting.runAPIs(applicationKey,
                                    sessionToken,
                                    stakeMultiplierMap,
                                    daySyntheticBalanceSheet,
                                    betCounter,
                                    lastBetMarketID,
                                    lastFavouriteSelectionID,
                                    lastFavouriteSyntheticStake,
                                    lastFavouriteSyntheticReturns);
             }

             }
         }
     }

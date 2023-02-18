package com.nickhorder.betfairapi.api;

import com.nickhorder.betfairapi.entities.*;
import com.nickhorder.betfairapi.enums.*;
import com.nickhorder.betfairapi.exceptions.APINGException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


//import static com.betfair.aping.enums.MarketProjection.MARKET_START_TIME;

/**
ApiNGFlowController

 This controls the calls to various methods

 */
public class ApiNGFlowController {

    private String applicationKey;
    private String sessionToken;
    private boolean isBackBet;
    private boolean isLayBet;



    public void runAPIs(String appKey,
                        String ssoid,
                        NavigableMap<Double,Double> stakeMultiplierMap,
                        List<SyntheticBalanceSheet> daySyntheticBalanceSheet,
                        double betCounter,
                        List<String> lastBetMarketID,
                        List<Long> lastFavouriteSelectionID,
                        List<Double> lastFavouriteSyntheticStake,
                        List<Double> lastFavouriteSyntheticReturns) throws IOException, APINGException {

        this.applicationKey = appKey;
        this.sessionToken = ssoid;


 /**
 ListMarketCatalogue: Get next available horse races, parameters:
 eventTypeIds : 7 - get all available horse races for event id 7 (horse racing)
 maxResults: 1 - specify number of results returned (narrowed to 1 to get first race)
 marketStartTime: specify date (must be in this format: yyyy-mm-ddTHH:MM:SSZ)
 sort: FIRST_TO_START - specify sort order to first to start race
 **/

        try {
            MarketFilter marketFilter;
            marketFilter = new MarketFilter();

            System.out.println("listMarketCataloque - Get next horse racing market in the UK:");

            TimeRange time = new TimeRange();
            time.setFrom(new Date());

            Set<String> countries = new HashSet<>();
            countries.add("GB");

            Set<String> typesCode = new HashSet<>();
            typesCode.add("WIN");

            //Hard coding eventTypeIds to 7, it's unlikely to change and saves calling the API each time
            Set<String> eventTypeIds = new HashSet<>();
            eventTypeIds.add("7");

            marketFilter = new MarketFilter();
            marketFilter.setEventTypeIds(eventTypeIds);
            marketFilter.setMarketStartTime(time);
            marketFilter.setMarketCountries(countries);
            marketFilter.setMarketTypeCodes(typesCode);

            Set<MarketProjection> marketProjection = new HashSet<>();
            marketProjection.add(MarketProjection.RUNNER_DESCRIPTION);
            marketProjection.add(MarketProjection.MARKET_START_TIME);

            String maxResults = "1";

             List<MarketCatalogue> marketCatalogueResult = MarketCatalogue.listMarketCatalogue(marketFilter,
                    marketProjection,
                     MarketSort.FIRST_TO_START,
                     maxResults,
                    applicationKey, sessionToken);
            //System.out.println("5. Print static marketId, name and runners....\n");
             // printMarketCatalogue(marketCatalogueResult.get(0));

/**
 * nextRaceSleepCalculator: call the nextRaceSleepCalculator method within TimeHandler.
 * This will calculate the time in seconds between now and the next race, subtracting a certain amount
 * Of seconds before "the off".
 * If this calculation is > x seconds, pause until it's not. If less, (or at the end of the pause),
 * we return to the market. The idea here is to get in just before the off, where the market is as
 * liquid as it's going to be.
 * Note that if the next race is a long time away, we infer that racing has finished for the day
 * (or there is none) and call programStartStop to shutdown.
 */
            TimeHandler calculatePause = new TimeHandler();
             String marketStart = marketCatalogueResult.get(0).getMarketStartTime();

             calculatePause.nextRaceSleepCalculator(marketStart);

            //If no more racing today, shut down
            if (!TimeHandler.getMoreRacingToday()) {
                TimeHandler.programStartStop(TimeHandler.getMoreRacingToday());
               }

/**
 * After coming back from our sleep, we check whether this is the first bet of the day (in which case skip this)
 * If not, we need to update the synthetic balance sheet with the result of the last race.
 * Note that when it's a very busy raceday, it's possible for one race not to have finished before the next one starts.
 * Need to put in some pause logic that will wait for the last race result. Knowing the result of the last race is more
 * important than missing the next, for our strategy.
  */
            System.out.println("betCounter after sleep: " + betCounter);

            if (betCounter > 0){
                System.out.println("Going to grabPreviousRaceDetails");

                List<PreviousRaceResults> marketBookReturnPreviousRace = PreviousRaceResults.listPreviousMarketBook(lastBetMarketID,
                        applicationKey, sessionToken);
                PreviousRaceResults lastRaceResults = marketBookReturnPreviousRace.get(0);

            //    System.out.println("Previous Race Details: " + lastRaceResults);

                //Pass runners from previous race to identify whether the favourite won. Remember we need to do this for the
                //Synthetic Balance, regardless of whether we placed an actual bet in the last race or not.
                PreviousRaceResults.lastFavouriteWonOrLost(lastFavouriteSelectionID, lastRaceResults);
            }

/**
 * ListMarketBook: get list of runners in the market, parameters:
 * marketId:  the market we want to list runners
 * Note that I don't think BSP data is available unless you use a Live App Key (I have Delayed currently)
 */
                System.out.println("6.(listMarketBook) Get volatile info for Market including best 3 exchange prices available...\n");
                String nextRaceMarketIdChosen = marketCatalogueResult.get(0).getMarketId();

                //Hardcode a particular market - i.e one that is in the past. When going live, this will be
            // marketIDChosen.
                String lastMarketID = "1.210081920";
            //System.out.println(((Object)nextRaceMarketIdChosen).getClass().getName());

            PriceProjection priceProjection = new PriceProjection();
                Set<PriceData> priceData = new HashSet<>();
                priceData.add(PriceData.EX_BEST_OFFERS);
                priceData.add(PriceData.SP_AVAILABLE);
                priceData.add(PriceData.SP_TRADED);
                priceData.add(PriceData.EX_BEST_OFFERS);
                priceData.add(PriceData.EX_ALL_OFFERS);
                priceData.add(PriceData.EX_TRADED);
                priceProjection.setPriceData(priceData);

                //In this case we don't need these objects, so they are declared null
                OrderProjection orderProjection = null;
                MatchProjection matchProjection = null;
                String currencyCode = null;

                List<String> nextRaceMarketIds = new ArrayList<>();
                nextRaceMarketIds.add(nextRaceMarketIdChosen);

                List<MarketBook> marketBookReturn = MarketBook.listMarketBook(nextRaceMarketIds, priceProjection,
                        orderProjection, matchProjection, currencyCode, applicationKey, sessionToken);

/**
 * identifyFavourite: pass the marketBookReturn to this method within MarketBookProcessing, and take back
 * a map of SelectionID and Back odds[0] for the Favourite.
 */
            MarketBook nextRaceMBReturn = marketBookReturn.get(0);
            Map.Entry<Long, Double> marketBookProcessingReturn = MarketBookProcessing.identifyFavourite(nextRaceMBReturn);

            //Assign Favourite's Selection ID and Odds to primitive types to make clearer in subsequent processing
            long nextRaceFavouriteSelectionID = marketBookProcessingReturn.getKey();
            double nextRaceFavouriteOdds = marketBookProcessingReturn.getValue();

/**
* StrategyManager calls: pass various attributes into the StrategyManager class for decisioning
 */
            // Calculate nextRaceSyntheticStake
            double nextRaceSyntheticStake = StrategyManager.calculateSyntheticStake(daySyntheticBalanceSheet);
            // Calculate nextRaceSyntheticReturns
            double nextRaceSyntheticReturns = StrategyManager.calculateSyntheticReturns(nextRaceFavouriteOdds, nextRaceSyntheticStake);

            System.out.println("nextRaceSyntheticStake: " + nextRaceSyntheticStake);
            System.out.println("nextRaceSyntheticReturns: " + nextRaceSyntheticReturns);

            // Call AccountsAPI to get current live balance
            AccountFunds accountFundsInstance = AccountFunds.getAccountFunds(applicationKey, sessionToken);
            double currentLiveBalance = accountFundsInstance.getAvailableToBetBalance();

            //Identify live stake by multiiplying Stake Multiplied by Balance and SyntheticSMA moderator
            double nextRaceStakeMultiplied = StrategyManager.calculateStakeMultiplied(nextRaceFavouriteOdds, stakeMultiplierMap, currentLiveBalance);

            if (nextRaceStakeMultiplied > 0){
                isBackBet = true;
                betCounter += 1;
                //nextRaceMarketIdChosen will be the list to use when going live, not lastMarketID which is a testing o/r

                lastBetMarketID.add(lastMarketID);
                //Long spoofFavourite = 44544270l;
                //lastFavouriteSelectionID.add(spoofFavourite);
                lastFavouriteSelectionID.add(nextRaceFavouriteSelectionID);
                lastFavouriteSyntheticStake.add(nextRaceSyntheticStake);
                lastFavouriteSyntheticReturns.add(nextRaceSyntheticReturns);
                System.out.println("lastMarketID being added? " + lastBetMarketID);
                System.out.println("Is BackBet? " + isBackBet + " BetCounter: " + betCounter);
                //Call StrategyManager.backBetPrepare
            }
            if (nextRaceStakeMultiplied < 0){
                isLayBet = true;
                betCounter += 1;
                //nextRaceMarketIdChosen will be the list to use when going live, not lastMarketID which is a testing o/r
                // Add the last MarketID to lastBetMarketID, which will be picked up before the next race.
                lastBetMarketID.add(lastMarketID);
                //Long spoofFavourite = 44544270l;
                //lastFavouriteSelectionID.add(spoofFavourite);
                // Add current Favourite SelectionID to lastFavouriteSelectionID, which will be picked up before
                // the next race.
                lastFavouriteSelectionID.add(nextRaceFavouriteSelectionID);
                lastFavouriteSyntheticStake.add(nextRaceSyntheticStake);
                lastFavouriteSyntheticReturns.add(nextRaceSyntheticReturns);
                System.out.println("lastMarketID being added? " + lastBetMarketID);
                System.out.println("Is LayBet? " + isLayBet + " BetCounter: " + betCounter);
            }
            if (nextRaceStakeMultiplied == 0) {
                // skip everything bar synthetic work
                betCounter += 1;
                //nextRaceMarketIdChosen will be the list to use when going live, not lastMarketID which is a testing o/r
                lastBetMarketID.add(lastMarketID);
                lastFavouriteSelectionID.add(nextRaceFavouriteSelectionID);
                lastFavouriteSyntheticStake.add(nextRaceSyntheticStake);
                lastFavouriteSyntheticReturns.add(nextRaceSyntheticReturns);
                System.out.println("lastMarketID being added? " + lastBetMarketID);
                System.out.println("Both should be false: " + isLayBet + " " + isBackBet + " BetCounter: " + betCounter);

            }
            ///If oddsMultiplied is > 0, BACK indicator, if < 0, LAY indicator needs to be added


            System.out.println("nextRaceStakeMultiplied at end of loop iteration " + nextRaceStakeMultiplied);

            System.out.println("Fav SelID at end of loop iteration: " + nextRaceFavouriteSelectionID);
            System.out.println("Fav Odds at end of loop iteration: " + nextRaceFavouriteOdds);


        //     printMarketBook(marketBookReturn.get(0));


 /**
* PlaceOrders: we try to place a bet, based on the previous request we provide the following:
  * marketId: the market id
  * selectionId: the runner selection id we want to place the bet on
  * side: BACK - specify side, can be Back or Lay
  * orderType: LIMIT - specify order type
  * size: the size of the bet
  * price: the price of the bet
  * customerRef: 1 - unique reference for a transaction specified by user, must be different for each request
  *
  */
                long selectionId = 0;
                if (marketBookReturn.size() != 0) {


                    // This appears to be the point where we request the particular runner (selection)
                    //Yes indeed. Just tested and setting getRunners to get(1) places a bet on the 2nd
                    // horse in the array.

                    //Also, need to put logic in here to stop going back to place another bet, after
                    // one has been placed, and the race hasn't yet hit the start time. That's only
                    // going to be about 5 seconds based on our sleep function, but still it's possible

                    //This gets the first runner, which we don't want to limit ourselves to
                    Runner runner = marketBookReturn.get(0).getRunners().get(0);

                    System.out.println("Race Status: " + runner.getStatus());

                    selectionId = marketBookProcessingReturn.getKey();

                    System.out.println("7. Place a bet below minimum stake to prevent the bet actually " +
                            "being placed for marketId: " + nextRaceMarketIdChosen + " with selectionId: " + selectionId + "...\n\n");
                    List<PlaceInstruction> instructions = new ArrayList<>();
                    PlaceInstruction instruction = new PlaceInstruction();
                    instruction.setHandicap(0);
                    instruction.setSide(Side.BACK);
                    instruction.setOrderType(OrderType.LIMIT);

                    LimitOrder limitOrder = new LimitOrder();
                    limitOrder.setPersistenceType(PersistenceType.LAPSE);

                    //You can adjust the size and price value in the "apingdemo.properties" file
                    limitOrder.setPrice(getPrice());
                    limitOrder.setSize(getSize());

                    instruction.setLimitOrder(limitOrder);
                    instruction.setSelectionId(selectionId);
                    instructions.add(instruction);

                    //Build customerRef
                    int minCustomerRef = 1000000, maxCustomerRef = 9999999;
                    int randomNum = ThreadLocalRandom.current().nextInt(minCustomerRef, maxCustomerRef + 1);
                    String customerRef = ("Sharptrading" + randomNum);

   //No More Bets    InstructionAndExecution placeBetResult = InstructionAndExecution.placeOrders(nextRaceMarketIdChosen, instructions, customerRef, applicationKey, sessionToken);

                    // Handling the operation result
                    //No More Bets                if (placeBetResult.getExecutionReportStatus() == ExecutionReportStatus.SUCCESS) {
                    //No More Bets                   System.out.println("Your bet has been placed!!");
                    //No More Bets                  System.out.println(placeBetResult.getInstructionReports());
                    //No More Bets             } else if (placeBetResult.getExecutionReportStatus() == ExecutionReportStatus.FAILURE) {
                    //No More Bets               System.out.println("Your bet has NOT been placed.");
                    //No More Bets                System.out.println("The error is: " + placeBetResult.getExecutionReportErrorCode() + ": "
                    //No More Bets                     + placeBetResult.getExecutionReportErrorCode().getMessage());
                    //No More Bets           }
                    //No More Bets       } else {
                    //No More Bets          System.out.println("Sorry, no runners found\n\n");
                }

            } catch(APINGException apiExc){
                System.out.println(apiExc.toString());
            } catch(InterruptedException e){
                throw new RuntimeException(e);
            } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        private static double getPrice () {

            try {
                Double d = Double.valueOf((String) ApiNGAuthMain.getProp().get("BET_PRICE"));
                return d;
            } catch (NumberFormatException e) {
                //returning the default value
                Double def = 1000d;
                return def;
            }
        }


        private static double getSize () {
            try {
                // return new Double((String)ApiNGAuthMain.getProp().get("BET_SIZE"));
                Double d = Double.valueOf((String) ApiNGAuthMain.getProp().get("BET_SIZE"));
                return d;
            } catch (NumberFormatException e) {
                //returning the default value
                // return new Double(0.01);
                Double def = 0.01d;
                return def;
            }
        }

        private void printMarketCatalogue (MarketCatalogue mk){
            System.out.println("Market Name: " + mk.getMarketName() + "; Id: " + mk.getMarketId() + "\n");
            List<RunnerCatalog> runners = mk.getRunners();
            if (runners != null) {
                for (RunnerCatalog rCat : runners) {
                    System.out.println("Runner Name: " + rCat.getRunnerName() + "; Selection Id: " + rCat.getSelectionId() + "\n");
                }
            }
        }
    private void printMarketBook (MarketBook mb) {
        System.out.println("Market ID: " + mb.getMarketId() + "\n");
        List<Runner> runners = mb.getRunners();

        if (runners != null) {
            for (Runner rCat : runners) {
                System.out.println("Selection Id: " + rCat.getSelectionId() + "\n" +
                        "Status: " + rCat.getStatus() + "\n" +
                        "EX: " + rCat.getEx());
            }
        }
    }
}




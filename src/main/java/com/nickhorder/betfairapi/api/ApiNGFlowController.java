package com.nickhorder.betfairapi.api;

//import com.betfair.aping.api.ApiNgOperations;
import com.nickhorder.betfairapi.entities.*;
import com.nickhorder.betfairapi.enums.*;
import com.nickhorder.betfairapi.exceptions.APINGException;
import com.nickhorder.betfairapi.entities.*;
import com.nickhorder.betfairapi.enums.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


//import static com.betfair.aping.enums.MarketProjection.MARKET_START_TIME;

/**
ApiNGFlowController

 This controls the calls to various methods

 */
public class ApiNGFlowController {

    //	private ApiNgFlowControllerOperations instanceOfOperations = ApiNgFlowControllerOperations.getInstance();
    private String applicationKey;
    private String sessionToken;

    public void runAPIs(String appKey, String ssoid) {

        this.applicationKey = appKey;
        this.sessionToken = ssoid;

        try {
/**
 ListEventTypes: Search for the event types and then for the "Horse Racing" in the returned list to finally get
 the listEventTypeId
 */
            MarketFilter marketFilter;
            marketFilter = new MarketFilter();
            Set<String> eventTypeIds = new HashSet<String>();

            //1.(listEventTypes) Get all Event Types
            List<EventTypeResult> r = EventTypeResult.listEventTypes(marketFilter, applicationKey, sessionToken);
            //". Extract Event Type Id for Horse Racing...\n");
            for (EventTypeResult eventTypeResult : r) {
                if (eventTypeResult.getEventType().getName().equals("Horse Racing")) {
                    //System.out.println("3. EventTypeId for \"Horse Racing\" is: " + eventTypeResult.getEventType().getId()+"\n");
                    eventTypeIds.add(eventTypeResult.getEventType().getId().toString());
                }
            }

            /**
             * ListMarketCatalogue: Get next available horse races, parameters:
             * eventTypeIds : 7 - get all available horse races for event id 7 (horse racing)
             * maxResults: 1 - specify number of results returned (narrowed to 1 to get first race)
             * marketStartTime: specify date (must be in this format: yyyy-mm-ddTHH:MM:SSZ)
             * sort: FIRST_TO_START - specify sort order to first to start race
             */
            System.out.println("4.(listMarketCataloque) Get next horse racing market in the UK:");

            //old method
            TimeRange time = new TimeRange();
            time.setFrom(new Date());
            //System.out.println(time.getFrom());

            Set<String> countries = new HashSet<>();
            countries.add("GB");

            Set<String> typesCode = new HashSet<>();
            typesCode.add("WIN");

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
 * sleepCalculator: call the sleepCalculator method within Program Pause.
 * This will calculate the time in seconds between now and the next race, subtracting a certain amount
 * Of seconds before "the off".
 * If this calculation is > x seconds, pause until it's not. If less, (or at the end of the pause),
 * we return to the market. The idea here is to get in just before the off, where the market is as
 * liquid as it's going to be.
 * Note that if the next race is over 6 hours away, we infer that racing has finished for the day
 * (or there is none) and call programCheckStart to shutdown.
 */
            ProgramPause calculatePause = new ProgramPause();
            String marketStart = marketCatalogueResult.get(0).getMarketStartTime();
            calculatePause.nextRaceSleepCalculator(marketStart);

            if (!ProgramPause.getMoreRacingToday()) {
                ProgramPause.programStartStop(ProgramPause.getMoreRacingToday());
               }


/**
 * ListMarketBook: get list of runners in the market, parameters:
 * marketId:  the market we want to list runners
 * Note that I don't think BSP data is available unless you use a Live App Key (I have Delayed currently)
 *
 */
                System.out.println("6.(listMarketBook) Get volatile info for Market including best 3 exchange prices available...\n");
                String marketIdChosen = marketCatalogueResult.get(0).getMarketId();

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

                List<String> marketIds = new ArrayList<>();
                marketIds.add(marketIdChosen);

                List<MarketBook> marketBookReturn = MarketBook.listMarketBook(marketIds, priceProjection,
                        orderProjection, matchProjection, currencyCode, applicationKey, sessionToken);


         //   ArrayList<List> arrList=new ArrayList<>();
          //  arrList.add(marketBookReturn.get(0).getRunners());
          //  System.out.println("Number of elements in arrList: "+arrList.size()); ,its one, this is rubbish
         //   System.out.println("arrList: " + arrList);



         //   printMarketBook(marketBookReturn.get(0));
         //   printMarketBookTest(marketBookReturn.get(0));





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

                    Runner runner = marketBookReturn.get(0).getRunners().get(0);

                    Runner runner2 = marketBookReturn.get(0).getRunners().get(0);

                    System.out.println("Bet this won't work: " + runner2.getStatus());
                    ExchangePrices bob = runner2.getEx();
                    System.out.println("bob ATB Size: " + bob.getAvailableToBack().size());
                    System.out.println("bob0: " + bob.getAvailableToBack().get(0).getPrice());
                   // StrategyManager strategy = new StrategyManager();
                    if (bob.getAvailableToBack().get(0).getPrice() > 1){
                     System.out.println("Bob is greater than 1! YES!!");
                    }
                    else{
                        System.out.println("Bob not greater than 1.");
                    }
                    System.out.println("bob1: " + bob.getAvailableToBack().get(1));
                    System.out.println("bob2: " + bob.getAvailableToBack().get(2));

                    System.out.println("Prices for one runner: " + bob);



                    selectionId = runner.getSelectionId();
                   // System.out.println("here we are back in flow controller " + runner.getEx());


                    System.out.println("7. Place a bet below minimum stake to prevent the bet actually " +
                            "being placed for marketId: " + marketIdChosen + " with selectionId: " + selectionId + "...\n\n");
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
                    int min = 1000000, max = 9999999;
                    int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
                    String customerRef = ("Sharptrading" + randomNum);

                    InstructionAndExecution placeBetResult = InstructionAndExecution.placeOrders(marketIdChosen, instructions, customerRef, applicationKey, sessionToken);

                    // Handling the operation result
                    if (placeBetResult.getExecutionReportStatus() == ExecutionReportStatus.SUCCESS) {
                        System.out.println("Your bet has been placed!!");
                        System.out.println(placeBetResult.getInstructionReports());
                    } else if (placeBetResult.getExecutionReportStatus() == ExecutionReportStatus.FAILURE) {
                        System.out.println("Your bet has NOT been placed.");
                        System.out.println("The error is: " + placeBetResult.getExecutionReportErrorCode() + ": "
                                + placeBetResult.getExecutionReportErrorCode().getMessage());
                    }
                } else {
                    System.out.println("Sorry, no runners found\n\n");
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
        System.out.println("; Id: " + mb.getMarketId() + "\n");
        List<Runner> runners = mb.getRunners();

        if (runners != null) {
            for (Runner rCat : runners) {
                System.out.println("Selection Id: " + rCat.getSelectionId() + "\n" +
                        "Status: " + rCat.getStatus() + "\n" +
                        "EX: " + rCat.getEx() + "\n" +
                        "Price: " + mb.getPrice() + "\n" +
                        "Last Price Traded: " + rCat.getLastPriceTraded());
            }
        }
    }
        private void printMarketBookTest (MarketBook mb) {

            if (mb.getMarketId() != null) {

                    System.out.println("mahket Id: " + mb.getMarketId());
                }
            }

    }




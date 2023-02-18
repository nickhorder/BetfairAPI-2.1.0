package com.nickhorder.betfairapi.api;

import com.nickhorder.betfairapi.entities.ExchangePrices;
import com.nickhorder.betfairapi.entities.Runner;

import java.util.*;
import java.util.stream.Collectors;

public class MarketBookProcessing {

    public static Map.Entry<Long, Double> identifyFavourite(MarketBook latestMBReturn) {

        //1. Create a new list, for all the getRunners return from marketBookReturn
        List<Runner> allrunnersList = latestMBReturn.getRunners();

        //2. Create a new list for only ACTIVE, i.e not non-runners.
        List<Runner> onlyActiveRunnersList = new ArrayList<>();
        if (allrunnersList != null) {
            for (Runner horse : allrunnersList) {
                String active = "ACTIVE";
                if (horse.getStatus().equals(active)) {
                    onlyActiveRunnersList.add(horse);
                }
            }
        }
        else {
            System.out.println("No active runners in Market ID: " + latestMBReturn.getMarketId());
        }
        //System.out.println("All Runners: " + allrunnersList.size());
        //System.out.println("Only Actives: " + onlyActiveRunnersList.size());

        //3. Add SelectionID and Back Odds for each runner to a Hashmap
        HashMap<Long, Double> selectionAndBackOddsUnsorted = new HashMap<>();

        if (onlyActiveRunnersList != null) {
            for (Runner horse : onlyActiveRunnersList) {
                ExchangePrices exForRunners = horse.getEx();

                if (exForRunners.getAvailableToBack().size() != 0 && exForRunners.getAvailableToLay().size() != 0) {
                //    System.out.println("SelectionID: " + horse.getSelectionId() +
                //            " Back Price: " + exForRunners.getAvailableToBack().get(0).getPrice() +
                //            " Size: " + exForRunners.getAvailableToBack().get(0).getSize() +
                 //           " Lay Price: " + exForRunners.getAvailableToLay().get(0).getPrice() +
                 //           " Size: " + exForRunners.getAvailableToLay().get(0).getSize());

                    //ADD TO HASHMAP HERE
                selectionAndBackOddsUnsorted.put(horse.getSelectionId(), exForRunners.getAvailableToBack().get(0).getPrice());

                } else {
                    System.out.println("No Prices Found For a Runner");
                }
            }
        }
        //4. Order the unsorted SelectionID & Back HashMap, into sorted by lowest (favourite)
        LinkedHashMap<Long, Double> selectionAndBackOddsSorted = selectionAndBackOddsUnsorted.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(/* Optional: Comparator.reverseOrder() */))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        //System.out.println("Is it sorted? : " + selectionAndBackOddsSorted);

        //5. Extract the favourite, by the min function of Collection.
        Map.Entry<Long, Double> mapFavourite = Collections.min(selectionAndBackOddsSorted.entrySet(),
                Map.Entry.comparingByValue());
        //System.out.println("Has the Min Worked? " + mapFavourite);


        return mapFavourite;
    }


}

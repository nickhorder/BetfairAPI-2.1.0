package com.nickhorder.betfairapi.api;
import java.util.List;
import com.nickhorder.betfairapi.entities.PriceSize;

public class StrategyManager {
    private List<PriceSize> oddsLowerBand;
 //   private double oddsLowerBand = 1;

    public List<PriceSize> getOddsLowerBand() {
        return oddsLowerBand;
    }

    public void setOddsLowerBand(List<PriceSize> oddsLowerBand) {
        this.oddsLowerBand = oddsLowerBand;
    }



}

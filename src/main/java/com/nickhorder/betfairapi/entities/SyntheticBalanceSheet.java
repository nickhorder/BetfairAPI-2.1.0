package com.nickhorder.betfairapi.entities;

import java.util.Comparator;
import java.util.List;

//MarketID	SelectionID	Stake	Returns	Balance	SMA
//        1.209948964	38084176	1	-1	99	99
//        1.209948964	1234352	1	2.46	101.46	100.23

public class SyntheticBalanceSheet {
    private Double syntheticBalanceSheetMarketID;
    private Long syntheticBalanceSheetSelectionID;  //This is usually long
    private Double sythenticBalanceSheetStake;
    private Double sythenticBalanceSheetReturn;
    private Double sythenticBalanceSheetBalance;
    private Double sythenticBalanceSheetSMA;


    public Double getSyntheticBalanceSheetMarketID() { return syntheticBalanceSheetMarketID;  }
    public void setSyntheticBalanceSheetMarketID(Double syntheticBalanceSheetMarketID) { this.syntheticBalanceSheetMarketID = syntheticBalanceSheetMarketID;  }

    public Long getSyntheticBalanceSheetSelectionID() { return syntheticBalanceSheetSelectionID;  }
    public void setSyntheticBalanceSheetSelectionID(Long syntheticBalancesheetSelectionID) { this.syntheticBalanceSheetSelectionID = syntheticBalancesheetSelectionID;  }

    public Double getSythenticBalanceSheetReturn() { return sythenticBalanceSheetReturn;  }
    public void setSythenticBalanceSheetReturn(Double sythenticBalanceSheetReturn) { this.sythenticBalanceSheetReturn = sythenticBalanceSheetReturn;  }

    public Double getSythenticBalanceSheetStake() { return sythenticBalanceSheetStake;  }
    public void setSythenticBalanceSheetStake(Double sythenticBalanceSheetStake) { this.sythenticBalanceSheetStake = sythenticBalanceSheetStake;  }

    public Double getSythenticBalanceSheetBalance() { return sythenticBalanceSheetBalance;  }
    public void setSythenticBalanceSheetBalance(Double sythenticBalanceSheetBalance) { this.sythenticBalanceSheetBalance = sythenticBalanceSheetBalance;  }

    public Double getSythenticBalanceSheetSMA() { return sythenticBalanceSheetSMA;  }
    public void setSythenticBalanceSheetSMA(Double sythenticBalanceSheetSMA) { this.sythenticBalanceSheetSMA = sythenticBalanceSheetSMA;  }


    public String toString() {
        return  "MarketID=" + getSyntheticBalanceSheetMarketID() + "\n"
                +"SelectionID=" + getSyntheticBalanceSheetSelectionID() + "\n"
                +"Stake=" + getSythenticBalanceSheetStake() + "\n"
                + "Return=" + getSythenticBalanceSheetReturn() + "\n"
                + "Balance=" + getSythenticBalanceSheetBalance() + "\n"
                + "SMA=" + getSythenticBalanceSheetSMA() + "\n";
    }


}

package com.gbce.sssm.coreObjectModel.dataStore;



import java.time.Instant;
import java.util.List;

import com.gbce.sssm.coreObjectModel.data.BuySellIndicator;
import com.gbce.sssm.coreObjectModel.data.Stock;
import com.gbce.sssm.coreObjectModel.data.Trade;



public interface TradeDAO {

    boolean add(Stock            stock,
                Instant          timestamp,
                long             quantity,
                BuySellIndicator buySellIndicator,
                long             price);



    List<Trade> getByStock(Stock stock);
}

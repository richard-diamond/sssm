package com.gbce.sssm.coreObjectModel.dataStore;



import java.util.List;

import com.gbce.sssm.coreObjectModel.data.Stock;
import com.gbce.sssm.coreObjectModel.data.Trade;



public interface TradeDAO {

    List<Trade> getByStock(Stock stock);
}

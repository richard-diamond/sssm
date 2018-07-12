package com.gbce.sssm.coreObjectModel.dataStore;



import java.util.List;

import com.gbce.sssm.coreObjectModel.data.Stock;



public interface StockDAO {

    List<Stock> getAll();
}

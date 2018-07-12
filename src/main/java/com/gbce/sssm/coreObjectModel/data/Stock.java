package com.gbce.sssm.coreObjectModel.data;



import java.math.BigDecimal;



public interface Stock {

    String getSymbol();



    StockType getStockType();



    long getLastDividend();



    Double getFixedDividend();



    long getParValue();
}

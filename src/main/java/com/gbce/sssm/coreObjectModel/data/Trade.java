package com.gbce.sssm.coreObjectModel.data;



import java.time.Instant;



public interface Trade {

    Stock getStock();



    Instant getTimeStamp();



    long getQuantity();



    BuySellIndicator getBuySellIndicator();



    long getPrice();
}

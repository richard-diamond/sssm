package com.gbce.sssm.coreObjectModel.operations;



import java.math.BigDecimal;
import java.time.Instant;

import com.gbce.sssm.coreObjectModel.data.BuySellIndicator;
import com.gbce.sssm.coreObjectModel.data.Stock;



/**
 * This interface defines trading ancillary functionality that exists as part of the core object model of the
 *  GBCE drinks stock market trading platform.
 * <p/>
 * <p/>
 * @since 1.0
 */



public interface CoreOps {

    /**
     * This method calculates the dividend yield of the specified stock entity with relation to the given price value.
     *  The dividend yield is a value calculated by dividing the dividend of a stock unit by the price of the stock
     *  unit.
     * <p/>
     * @param stock a Stock instance modelling the details of the relevant stock entity
     * @param price an integer representing the price value (in pennies) to be used in the calculation
     * @return      a BigDecimal representing the dividend yield for the specified stock entity, or null if an error occurred
     */

    BigDecimal calculateDividendYield(Stock stock,
                                      long  price);



    /**
     * This method calculates the P/E ratio of the specified stock entity with relation to the given price value.
     *  The P/E ratio is a value representing the price of a stock unit divided by the earnings (dividend) of the
     *  stock unit.
     * <p/>
     * @param stock a Stock instance modelling the details of the relevant stock entity
     * @param price an integer representing the price value (in pennies) to be used in the calculation
     * @return      a BigDecimal representing the P/E ratio for the specified stock entity, or null if an error occurred
     */

    BigDecimal calculatePERatio(Stock stock,
                                long  price);



    /**
     * This method attempts to record the trade of the specified number of units of the passed stock entity at
     *  the given price per unit.
     * <p/>
     * @param stock            a Stock instance modelling the details of the relevant stock entity
     * @param timestamp        an Instant instance modelling the date and time of the trade
     * @param quantity         an integer specifying the number of stock units traded
     * @param buySellIndicator a value indicating whether the trade was a buy or a sell operation
     * @param price            an integer representing the price value (in pennies) of each unit of stock traded
     * @return                 true if the trade was stored successfully, otherwise false
     */

    boolean recordTrade(Stock            stock,
                        Instant          timestamp,
                        long             quantity,
                        BuySellIndicator buySellIndicator,
                        long             price);



    /**
     * This method calculates the volume weighted stock price of the specified stock entity over the past 5 minutes
     *  of trading. This calculation basically divides the sum of trade price multiplied by trade quantity by the sum
     *  of the traded quantities.
     * <p/>
     * @param stock a Stock instance modelling the details of the relevant stock entity
     * @return      a BigDecimal representing the volume weighted stock price of the specified stock entity, or null if an error occurred
     */

    BigDecimal calculateVolumeWeightedStockPriceInLastFiveMins(Stock stock);



    /**
     * This method calculates the all share index for all stocks traded. This calculation basically finds the geometric
     *  mean of the volume weighted stock prices of all stocks traded.
     * <p/>
     * @return a BigDecimal representing the all share index
     */

    BigDecimal calculateGBCEAllShareIndex();
}

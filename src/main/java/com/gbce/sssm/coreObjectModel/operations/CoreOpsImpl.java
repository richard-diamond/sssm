package com.gbce.sssm.coreObjectModel.operations;



import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.util.List;

import com.gbce.sssm.coreObjectModel.data.BuySellIndicator;
import com.gbce.sssm.coreObjectModel.data.Stock;
import com.gbce.sssm.coreObjectModel.data.Trade;
import com.gbce.sssm.coreObjectModel.dataStore.StockDAO;
import com.gbce.sssm.coreObjectModel.dataStore.TradeDAO;
import com.gbce.sssm.coreObjectModel.util.Maths;



public class CoreOpsImpl implements CoreOps {

    private static final int ROUNDING_MODE   = BigDecimal.ROUND_HALF_DOWN;
    private static final int ROUNDING_DIGITS = 4;

    private final TradeDAO tradeDAO;
    private final StockDAO stockDAO;



    public CoreOpsImpl(TradeDAO tradeDAO,
                       StockDAO stockDAO) {

        this.tradeDAO = tradeDAO;
        this.stockDAO = stockDAO;
    }



    @Override
    public BigDecimal calculateDividendYield(Stock stock,
                                             long  price) {

        BigDecimal yield;

        if (stock == null)
            yield = null;                           // TODO: need to determine the business requirement for this scenario (exception?)
        else if (stock.getStockType() == null)
            yield = null;                           // TODO: need to determine the business requirement for this scenario (exception?)
        else
            switch (stock.getStockType()) {
                case COMMON:
                    yield = divideAndRound(stock.getLastDividend(), price);
                    break;

                case PREFERRED:
                    if (stock.getFixedDividend() == null)
                        yield = BigDecimal.ZERO;    // TODO: need to determine the business requirement for this scenario
                    else
                        yield = divideAndRound(stock.getFixedDividend() * stock.getParValue(), price);
                    break;

                default:
                    yield = BigDecimal.ZERO;        // TODO: need to determine the business requirement for this scenario
            }

        return yield;
    }



    @Override
    public BigDecimal calculatePERatio(Stock stock,
                                       long  price) {

        BigDecimal peRatio;

        if (stock == null)
            peRatio = null;                         // TODO: need to determine the business requirement for this scenario (exception?)
        else if (stock.getLastDividend() == 0)
            peRatio = BigDecimal.ZERO;              // TODO: need to determine the business requirement for this scenario (exception?)
        else
            peRatio = divideAndRound(price, stock.getLastDividend());

        return peRatio;
    }



    @Override
    public boolean recordTrade(Stock            stock,
                               Instant          timestamp,
                               long             quantity,
                               BuySellIndicator buySellIndicator,
                               long             price) {

        boolean status;

        if (stock == null)
            status = false;                         // TODO: obvious assumption... if no stock, then no trade
        else if (buySellIndicator == null)
            status = false;                         // TODO: (fairly) obvious assumption... if we don't know if it's buy or sell, then it's not a trade
        else {
            // TODO: need to determine the business requirement for price or quantity being less than or equal to zero. For now, just accept it...

            if (timestamp == null)
                timestamp = Instant.now();          // TODO: need to determine the business requirement for this scenario (error?)

            status = tradeDAO.add(stock, timestamp, quantity, buySellIndicator, price);
        }

        return status;
    }



    @Override
    public BigDecimal calculateVolumeWeightedStockPriceInLastFiveMins(Stock stock) {

        BigDecimal volumeWaitedStockPrice;

        if (stock == null)
            volumeWaitedStockPrice = null;          // TODO: need to determine the business requirement for this scenario (exception?)
        else
            volumeWaitedStockPrice = convertAndRound(calculateRawVolumeWeightedStockPriceForPastFiveMins(stock));

        return volumeWaitedStockPrice;
    }



    @Override
    public BigDecimal calculateGBCEAllShareIndex() {

        List<Stock> stocks;
        double      productVWSP;
        double      allShareIndex;

        stocks = stockDAO.getAll();

        if (stocks.size() == 0)
            allShareIndex = 0.0;                    // TODO: need to determine the business requirement for this scenario
        else
        {
            productVWSP = 1.0;

            for (Stock stock : stocks)
            {
                double volumeWeightedStockPrice;

                volumeWeightedStockPrice = calculateRawVolumeWeightedStockPriceForPastFiveMins(stock);

                // TODO: need to check with business if zero values should be excluded, as it was not mentioned in the requirements
                productVWSP *= volumeWeightedStockPrice;
            }

            allShareIndex = Maths.nthRoot(stocks.size(), productVWSP);
        }

        return convertAndRound(allShareIndex);
    }



    private double calculateRawVolumeWeightedStockPriceForPastFiveMins(Stock stock) {

        List<Trade> trades;
        double      volumeWaitedStockPrice;
        long        quantities               = 0;
        long        tradedPricesByQuantities = 0;

        trades = tradeDAO.getByStockAndPastTime(stock, 5);

        for (Trade trade : trades) {

            quantities               += trade.getQuantity();
            tradedPricesByQuantities += trade.getPrice() * trade.getQuantity();
        }

        if (quantities == 0)
            volumeWaitedStockPrice = 0.0;           // TODO: need to determine the business requirement for this scenario
        else
            volumeWaitedStockPrice = (double)tradedPricesByQuantities / quantities;

        return volumeWaitedStockPrice;
    }



    private BigDecimal divideAndRound(long dividend,
                                      long divisor) {

        return divideAndRound(new BigDecimal(dividend), new BigDecimal(divisor));
    }



    private BigDecimal divideAndRound(double dividend,
                                      long   divisor) {

        return divideAndRound(new BigDecimal(dividend), new BigDecimal(divisor));
    }



    private BigDecimal divideAndRound(BigDecimal dividend,
                                      BigDecimal divisor) {

        return dividend.divide(divisor, ROUNDING_DIGITS, ROUNDING_MODE);
    }



    private BigDecimal convertAndRound(double value) {

        return new BigDecimal(value, MathContext.DECIMAL32).setScale(ROUNDING_DIGITS, ROUNDING_MODE);
    }
}

package com.gbce.sssm.coreObjectModel.operations;



import java.math.BigDecimal;
import java.time.Instant;

import com.gbce.sssm.coreObjectModel.data.BuySellIndicator;
import com.gbce.sssm.coreObjectModel.data.Stock;
import com.gbce.sssm.coreObjectModel.dataStore.TradeDAO;



public class CoreOpsImpl implements CoreOps {

    private static final int ROUNDING_MODE   = BigDecimal.ROUND_HALF_DOWN;
    private static final int ROUNDING_DIGITS = 4;

    private final TradeDAO tradeDAO;



    public CoreOpsImpl(TradeDAO tradeDAO) {
        this.tradeDAO = tradeDAO;
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

        return false;
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
}

package com.gbce.sssm.coreObjectModel.operations;



import java.math.BigDecimal;

import com.gbce.sssm.coreObjectModel.data.Stock;



public class CoreOpsImpl implements CoreOps {

    private static final int ROUNDING_MODE   = BigDecimal.ROUND_HALF_DOWN;
    private static final int ROUNDING_DIGITS = 4;



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
        return null;
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

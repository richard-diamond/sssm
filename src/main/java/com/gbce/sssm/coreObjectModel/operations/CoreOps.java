package com.gbce.sssm.coreObjectModel.operations;



import java.math.BigDecimal;
import java.time.Instant;

import com.gbce.sssm.coreObjectModel.data.BuySellIndicator;
import com.gbce.sssm.coreObjectModel.data.Stock;



public interface CoreOps {

    BigDecimal calculateDividendYield(Stock stock,
                                      long  price);
}

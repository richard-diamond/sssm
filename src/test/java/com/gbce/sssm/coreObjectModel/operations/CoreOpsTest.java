package com.gbce.sssm.coreObjectModel.operations;



import java.math.BigDecimal;

import com.gbce.sssm.coreObjectModel.data.Stock;
import com.gbce.sssm.coreObjectModel.data.StockType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;



public class CoreOpsTest {

    private static final Stock STOCK_TEA = new MockStock("TEA", StockType.COMMON, 0, null, 100);
    private static final Stock STOCK_POP = new MockStock("POP", StockType.COMMON, 8, null, 100);
    private static final Stock STOCK_ALE = new MockStock("ALE", StockType.COMMON, 23, null, 60);
    private static final Stock STOCK_GIN = new MockStock("GIN", StockType.PREFERRED, 8, 0.02, 100);
    private static final Stock STOCK_JOE = new MockStock("JOE", StockType.COMMON, 13, null, 250);

    private CoreOps coreOps;



    @Before
    public void setup() {

        coreOps  = new CoreOpsImpl();
    }



    @Test
    public void testDividendYieldWithNullStock() {

        BigDecimal dividendYield;

        dividendYield = coreOps.calculateDividendYield(null, 100);
        assertNull(dividendYield);
    }



    @Test
    public void testDividendYieldWithNullStockType() {

        Stock      stock;
        BigDecimal dividendYield;

        stock         = new MockStock("WINE", null, 11, null, 200);
        dividendYield = coreOps.calculateDividendYield(stock, 100);
        assertNull(dividendYield);
    }



    @Test
    public void testCommonDividendYieldWithPositiveLastDividend() {

        BigDecimal dividendYield;

        dividendYield = coreOps.calculateDividendYield(STOCK_POP, 320);
        assertEquals("0.0250", dividendYield.toString());
    }



    @Test
    public void testCommonDividendYieldWithZeroLastDividend() {

        BigDecimal dividendYield;

        dividendYield = coreOps.calculateDividendYield(STOCK_TEA, 320);
        assertEquals("0.0000", dividendYield.toString());
    }



    @Test
    public void testPreferredDividendYieldWithNonNullFixedDividend() {

        BigDecimal dividendYield;

        dividendYield = coreOps.calculateDividendYield(STOCK_GIN, 160);
        assertEquals("0.0125", dividendYield.toString());
    }



    @Test
    public void testPreferredDividendYieldWithNullFixedDividend() {

        Stock      stock;
        BigDecimal dividendYield;

        stock         = new MockStock("WINE", StockType.PREFERRED, 11, null, 200);
        dividendYield = coreOps.calculateDividendYield(stock, 160);
        assertEquals("0", dividendYield.toString());
    }



    @Test
    public void testPERatioWithNullStock() {

        BigDecimal dividendYield;

        dividendYield = coreOps.calculatePERatio(null, 100);
        assertNull(dividendYield);
    }



    @Test
    public void testPERatioWithZeroLastDividend() {

        BigDecimal dividendYield;

        dividendYield = coreOps.calculatePERatio(STOCK_TEA, 100);
        assertEquals("0", dividendYield.toString());
    }



    @Test
    public void testPERatioWithPositiveLastDividend() {

        BigDecimal dividendYield;

        dividendYield = coreOps.calculatePERatio(STOCK_GIN, 200);
        assertEquals("25.0000", dividendYield.toString());
    }



    private static class MockStock implements Stock {

        private String    symbol;
        private StockType stockType;
        private long      lastDividend;
        private Double    fixedDividend;
        private long      parValue;



        MockStock(String    symbol,
                  StockType stockType,
                  long      lastDividend,
                  Double    fixedDividend,
                  long      parValue) {

            this.symbol        = symbol;
            this.stockType     = stockType;
            this.lastDividend  = lastDividend;
            this.fixedDividend = fixedDividend;
            this.parValue      = parValue;
        }



        @Override
        public String getSymbol() {

            return symbol;
        }



        @Override
        public StockType getStockType() {

            return stockType;
        }



        @Override
        public long getLastDividend() {

            return lastDividend;
        }



        @Override
        public Double getFixedDividend() {

            return fixedDividend;
        }



        @Override
        public long getParValue() {

            return parValue;
        }
    }
}

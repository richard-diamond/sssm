package com.gbce.sssm.coreObjectModel.operations;



import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.gbce.sssm.coreObjectModel.data.BuySellIndicator;
import com.gbce.sssm.coreObjectModel.data.Stock;
import com.gbce.sssm.coreObjectModel.data.StockType;

import com.gbce.sssm.coreObjectModel.data.Trade;
import com.gbce.sssm.coreObjectModel.dataStore.TradeDAO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;



public class CoreOpsTest {

    private static final Stock STOCK_TEA = new MockStock("TEA", StockType.COMMON, 0, null, 100);
    private static final Stock STOCK_POP = new MockStock("POP", StockType.COMMON, 8, null, 100);
    private static final Stock STOCK_ALE = new MockStock("ALE", StockType.COMMON, 23, null, 60);
    private static final Stock STOCK_GIN = new MockStock("GIN", StockType.PREFERRED, 8, 0.02, 100);
    private static final Stock STOCK_JOE = new MockStock("JOE", StockType.COMMON, 13, null, 250);

    private CoreOps  coreOps;
    private TradeDAO tradeDAO;



    @Before
    public void setup() {

        tradeDAO = new MockTradeDAO();
        coreOps  = new CoreOpsImpl(tradeDAO);
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



    @Test
    public void testRecordTradeWithNullStock() {

        boolean status;

        status = coreOps.recordTrade(null, Instant.now(), 10, BuySellIndicator.BUY, 50);
        assertFalse(status);
    }



    @Test
    public void testRecordTradeWithNullBuySellIndicator() {

        boolean status;

        status = coreOps.recordTrade(STOCK_JOE, Instant.now(), 10, null, 50);
        assertFalse(status);
    }



    @Test
    public void testRecordTradeWithNegativeQuantity() {

        boolean status;
        int     preRecord;
        int     postRecord;

        preRecord  = tradeDAO.getByStock(STOCK_JOE).size();
        status     = coreOps.recordTrade(STOCK_JOE, Instant.now(), -10, BuySellIndicator.BUY, 50);
        postRecord = tradeDAO.getByStock(STOCK_JOE).size();

        assertTrue(status);
        assertTrue(preRecord < postRecord);
    }



    @Test
    public void testRecordTradeWithZeroQuantity() {

        boolean status;
        int     preRecord;
        int     postRecord;

        preRecord  = tradeDAO.getByStock(STOCK_JOE).size();
        status     = coreOps.recordTrade(STOCK_JOE, Instant.now(), 0, BuySellIndicator.BUY, 50);
        postRecord = tradeDAO.getByStock(STOCK_JOE).size();

        assertTrue(status);
        assertTrue(preRecord < postRecord);
    }



    @Test
    public void testRecordTradeWithNegativePrice() {

        boolean status;
        int     preRecord;
        int     postRecord;

        preRecord  = tradeDAO.getByStock(STOCK_JOE).size();
        status     = coreOps.recordTrade(STOCK_JOE, Instant.now(), 10, BuySellIndicator.BUY, -50);
        postRecord = tradeDAO.getByStock(STOCK_JOE).size();

        assertTrue(status);
        assertTrue(preRecord < postRecord);
    }



    @Test
    public void testRecordTradeWithZeroPrice() {

        boolean status;
        int     preRecord;
        int     postRecord;

        preRecord  = tradeDAO.getByStock(STOCK_JOE).size();
        status     = coreOps.recordTrade(STOCK_JOE, Instant.now(), 10, BuySellIndicator.BUY, 0);
        postRecord = tradeDAO.getByStock(STOCK_JOE).size();

        assertTrue(status);
        assertTrue(preRecord < postRecord);
    }



    @Test
    public void testRecordTradeWithValidData() {

        boolean status;
        int     preRecord;
        int     postRecord;

        preRecord  = tradeDAO.getByStock(STOCK_JOE).size();
        status     = coreOps.recordTrade(STOCK_JOE, Instant.now(), 10, BuySellIndicator.BUY, 50);
        postRecord = tradeDAO.getByStock(STOCK_JOE).size();

        assertTrue(status);
        assertTrue(preRecord < postRecord);
    }



    private class MockTradeDAO implements TradeDAO {

        private final List<Trade> tradeStore = new ArrayList<>();



        @Override
        public List<Trade> getByStock(Stock stock) {

            return tradeStore.stream()
                             .filter(trade -> trade.getStock().equals(stock))
                             .collect(Collectors.toList());
        }
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

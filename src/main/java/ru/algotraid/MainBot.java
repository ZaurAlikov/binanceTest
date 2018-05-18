package ru.algotraid;

import java.util.ArrayList;
import java.util.List;

public class MainBot {

    private BalanceCache balanceCache;
    private InArBot inArBot;
    private Double myBalance;
    private Double percentForBet = 1.0;
    private Double diffInPresent = 0.1;
    private Double diff2InPresent = 0.3;
    private List<PairTriangle> pairTriangleList;

    public MainBot(String apiKey, String secretKey) throws InterruptedException {
        inArBot = new InArBot(apiKey, secretKey, true);
        balanceCache = inArBot.getBalanceCache();
        pairTriangleList = new ArrayList<>();
        initPairTriangle();
    }

    public void start() throws InterruptedException {
        while (true) {
            myBalance = 15.0;
//            myBalance = Double.valueOf(balanceCache.getAccountBalanceCache().get("USDT").getFree());
            Double bet = myBalance * percentForBet;
            Double profit;
//            Double balanceBefore;
//            Double balanceAfter;
//            Double resultBalance;

            for (PairTriangle pairTriangle : pairTriangleList) {
                long t1 = System.currentTimeMillis();
                profit = inArBot.getProfit(bet, pairTriangle);
                if (profit >= diffInPresent) {
                    System.out.printf("%s Diff: %.3f%% \n", pairTriangle.toString() ,profit);
                    do {
//                    balanceBefore = getFullBalance();
                        inArBot.buyCycle(bet, pairTriangle);
//                    balanceAfter = getFullBalance();
//                    resultBalance = balanceAfter - balanceBefore;
                        profit = inArBot.getProfit(bet, pairTriangle);
                    } while (profit >= diff2InPresent/* && resultBalance >= 0.0*/);
//                    System.out.println(System.currentTimeMillis() - t1);
                } else /*System.out.println(profit);*/
                Thread.sleep(100);
            }
        }
    }

    public Double getFullBalance(){
        return Double.valueOf(balanceCache.getAccountBalanceCache().get("USDT").getFree()) +
                Double.valueOf(balanceCache.getAccountBalanceCache().get("USDT").getLocked());
    }

    private void initPairTriangle(){
        pairTriangleList.add(new PairTriangle("ADAUSDT", "ADABNB", "BNBUSDT", true));
        pairTriangleList.add(new PairTriangle("BCCUSDT", "BCCBNB", "BNBUSDT", true));
        pairTriangleList.add(new PairTriangle("BTCUSDT", "BNBBTC", "BNBUSDT", true));
        pairTriangleList.add(new PairTriangle("ETHUSDT", "BNBETH", "BNBUSDT", true));
        pairTriangleList.add(new PairTriangle("LTCUSDT", "LTCBNB", "BNBUSDT", true));
        pairTriangleList.add(new PairTriangle("NEOUSDT", "NEOBNB", "BNBUSDT", true));
        pairTriangleList.add(new PairTriangle("QTUMUSDT", "QTUMBNB", "BNBUSDT", true));
        pairTriangleList.add(new PairTriangle("BNBUSDT", "ADABNB", "ADAUSDT", false));
        pairTriangleList.add(new PairTriangle("BNBUSDT", "BCCBNB", "BCCUSDT", false));
        pairTriangleList.add(new PairTriangle("BNBUSDT", "BNBBTC", "BTCUSDT", false));
        pairTriangleList.add(new PairTriangle("BNBUSDT", "BNBETH", "ETHUSDT", false));
        pairTriangleList.add(new PairTriangle("BNBUSDT", "LTCBNB", "LTCUSDT", false));
        pairTriangleList.add(new PairTriangle("BNBUSDT", "NEOBNB", "NEOUSDT", false));
        pairTriangleList.add(new PairTriangle("BNBUSDT", "QTUMBNB", "QTUMUSDT", false));
    }
}

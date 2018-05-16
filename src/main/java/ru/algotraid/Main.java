package ru.algotraid;

public class Main {

    private static String apiKey = "";
    private static String secretKey = "";
    private static BalanceCache balanceCache;
    private static InArBot inArBot;

    private static Double myBalance;
    private static Double percentForBet = 0.9;
    private static Double step = 0.1;
    private static Double step2 = 0.3;

    public static void main(String[] args) throws InterruptedException {

        inArBot = new InArBot(apiKey, secretKey, true);
        balanceCache = new BalanceCache(apiKey, secretKey);

        while (true) {

            myBalance = 33.0;
//            myBalance = Double.valueOf(balanceCache.getAccountBalanceCache().get("USDT").getFree());
            Double bet = myBalance * percentForBet;
            Double profit;
//            Double balanceBefore;
//            Double balanceAfter;
//            Double resultBalance;

//            System.out.println(balanceCache.getAccountBalanceCache().get("USDT").getFree());
//            System.out.println(inArBot.getFreeBalance("USDT"));

            profit = inArBot.getProfit(bet, "ADAUSDT", "ADABNB", "BNBUSDT", true);
            if (profit > step) {
                System.out.println("USDT -> ADA -> BNB -> USDT " + profit);
                do{
//                    balanceBefore = getFullBalance();
                    inArBot.buyCycle(bet,"ADAUSDT", "ADABNB", "BNBUSDT", true);
//                    balanceAfter = getFullBalance();
//                    resultBalance = balanceAfter - balanceBefore;
                    profit = inArBot.getProfit(bet, "ADAUSDT", "ADABNB", "BNBUSDT", true);
                } while (profit >= step2/* && resultBalance >= 0.0*/);
            }
            Thread.sleep(100);

            profit = inArBot.getProfit(bet, "BCCUSDT", "BCCBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BCC -> BNB -> USDT " + profit);
                do{
                    inArBot.buyCycle(bet,"BCCUSDT", "BCCBNB", "BNBUSDT", true);
                    profit = inArBot.getProfit(bet, "BCCUSDT", "BCCBNB", "BNBUSDT", true);
                } while (profit >= step2);
            }

            profit = inArBot.getProfit(bet, "BTCUSDT", "BNBBTC", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BTC -> BNB -> USDT " + profit);
                do{
                    inArBot.buyCycle(bet,"BTCUSDT", "BNBBTC", "BNBUSDT", true);
                    profit = inArBot.getProfit(bet, "BTCUSDT", "BNBBTC", "BNBUSDT", true);
                } while (profit >= step2);
            }

            profit = inArBot.getProfit(bet, "ETHUSDT", "BNBETH", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> ETH -> BNB -> USDT " + profit);
                do{
                    inArBot.buyCycle(bet,"ETHUSDT", "BNBETH", "BNBUSDT", true);
                    profit = inArBot.getProfit(bet, "ETHUSDT", "BNBETH", "BNBUSDT", true);
                } while (profit >= step2);
            }

            profit = inArBot.getProfit(bet, "LTCUSDT", "LTCBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> LTC -> BNB -> USDT " + profit);
                do{
                    inArBot.buyCycle(bet,"LTCUSDT", "LTCBNB", "BNBUSDT", true);
                    profit = inArBot.getProfit(bet, "LTCUSDT", "LTCBNB", "BNBUSDT", true);
                } while (profit >= step2);
            }

            profit = inArBot.getProfit(bet, "NEOUSDT", "NEOBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> NEO -> BNB -> USDT " + profit);
                do{
                    inArBot.buyCycle(bet,"NEOUSDT", "NEOBNB", "BNBUSDT", true);
                    profit = inArBot.getProfit(bet, "NEOUSDT", "NEOBNB", "BNBUSDT", true);
                } while (profit >= step2);
            }

            profit = inArBot.getProfit(bet, "QTUMUSDT", "QTUMBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> QTUM -> BNB -> USDT " + profit);
                do{
                    inArBot.buyCycle(bet,"QTUMUSDT", "QTUMBNB", "BNBUSDT", true);
                    profit = inArBot.getProfit(bet, "QTUMUSDT", "QTUMBNB", "BNBUSDT", true);
                } while (profit >= step2);
            }

            profit = inArBot.getProfit(bet, "BNBUSDT", "ADABNB", "ADAUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BNB -> ADA -> USDT " + profit);
                do{
                    inArBot.buyCycle(bet,"BNBUSDT", "ADABNB", "ADAUSDT", false);
                    profit = inArBot.getProfit(bet, "BNBUSDT", "ADABNB", "ADAUSDT", false);
                } while (profit >= step2);
            }

            profit = inArBot.getProfit(bet, "BNBUSDT", "BCCBNB", "BCCUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BNB -> BCC -> USDT " + profit);
                do{
                    inArBot.buyCycle(bet,"BNBUSDT", "BCCBNB", "BCCUSDT", false);
                    profit = inArBot.getProfit(bet, "BNBUSDT", "BCCBNB", "BCCUSDT", false);
                } while (profit >= step2);
            }

            profit = inArBot.getProfit(bet, "BNBUSDT", "BNBBTC", "BTCUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BNB -> BTC -> USDT " + profit);
                do{
                    inArBot.buyCycle(bet,"BNBUSDT", "BNBBTC", "BTCUSDT", false);
                    profit = inArBot.getProfit(bet, "BNBUSDT", "BNBBTC", "BTCUSDT", false);
                } while (profit >= step2);
            }

            profit = inArBot.getProfit(bet, "BNBUSDT", "BNBETH", "ETHUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BNB -> ETH -> USDT " + profit);
                do{
                    inArBot.buyCycle(bet,"BNBUSDT", "BNBETH", "ETHUSDT", false);
                    profit = inArBot.getProfit(bet, "BNBUSDT", "BNBETH", "ETHUSDT", false);
                } while (profit >= step2);
            }

            profit = inArBot.getProfit(bet, "BNBUSDT", "LTCBNB", "LTCUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BNB -> LTC -> USDT " + profit);
                do{
                    inArBot.buyCycle(bet,"BNBUSDT", "LTCBNB", "LTCUSDT", false);
                    profit = inArBot.getProfit(bet, "BNBUSDT", "LTCBNB", "LTCUSDT", false);
                } while (profit >= step2);
            }

            profit = inArBot.getProfit(bet, "BNBUSDT", "NEOBNB", "NEOUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BNB -> NEO -> USDT " + profit);
                do{
                    inArBot.buyCycle(bet,"BNBUSDT", "NEOBNB", "NEOUSDT", false);
                    profit = inArBot.getProfit(bet, "BNBUSDT", "NEOBNB", "NEOUSDT", false);
                } while (profit >= step2);
            }

            profit = inArBot.getProfit(bet, "BNBUSDT", "QTUMBNB", "QTUMUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BNB -> QTUM -> USDT " + profit);
                do{
                    inArBot.buyCycle(bet,"BNBUSDT", "QTUMBNB", "QTUMUSDT", false);
                    profit = inArBot.getProfit(bet, "BNBUSDT", "QTUMBNB", "QTUMUSDT", false);
                } while (profit >= step2);
            }
        }
    }

    public static Double getFullBalance(){
        return Double.valueOf(balanceCache.getAccountBalanceCache().get("USDT").getFree()) +
                Double.valueOf(balanceCache.getAccountBalanceCache().get("USDT").getLocked());
    }
}

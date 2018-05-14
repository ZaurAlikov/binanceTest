package ru.algotraid;

public class Main {

    private static String apiKey = "";
    private static String secretKey = "";

    public static void main(String[] args) throws InterruptedException {
        InArBot inArBot = new InArBot(apiKey, secretKey, true);

        while (true) {
            Double profit;

            profit = inArBot.getProfit("ADAUSDT", "ADABNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > 0.1) {
                System.out.println("USDT -> ADA -> BNB -> USDT " + profit);
            }

            profit = inArBot.getProfit("BCCUSDT", "BCCBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > 0.1) {
                System.out.println("USDT -> BCC -> BNB -> USDT " + profit);
            }

            profit = inArBot.getProfit("BTCUSDT", "BNBBTC", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > 0.1) {
                System.out.println("USDT -> BTC -> BNB -> USDT " + profit);
            }

            profit = inArBot.getProfit("ETHUSDT", "BNBETH", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > 0.1) {
                System.out.println("USDT -> ETH -> BNB -> USDT " + profit);
            }

            profit = inArBot.getProfit("LTCUSDT", "LTCBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > 0.1) {
                System.out.println("USDT -> LTC -> BNB -> USDT " + profit);
            }

            profit = inArBot.getProfit("NEOUSDT", "NEOBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > 0.1) {
                System.out.println("USDT -> NEO -> BNB -> USDT " + profit);
            }

            profit = inArBot.getProfit("QTUMUSDT", "QTUMBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > 0.1) {
                System.out.println("USDT -> QTUM -> BNB -> USDT " + profit);
            }

            profit = inArBot.getProfit("BNBUSDT", "ADABNB", "ADAUSDT", false);
            Thread.sleep(100);
            if (profit > 0.1) {
                System.out.println("USDT -> BNB -> ADA -> USDT " + profit);
            }

            profit = inArBot.getProfit("BNBUSDT", "BCCBNB", "BCCUSDT", false);
            Thread.sleep(100);
            if (profit > 0.1) {
                System.out.println("USDT -> BNB -> BCC -> USDT " + profit);
            }

            profit = inArBot.getProfit("BNBUSDT", "BNBBTC", "BTCUSDT", false);
            Thread.sleep(100);
            if (profit > 0.1) {
                System.out.println("USDT -> BNB -> BTC -> USDT " + profit);
            }

            profit = inArBot.getProfit("BNBUSDT", "BNBETH", "ETHUSDT", false);
            Thread.sleep(100);
            if (profit > 0.1) {
                System.out.println("USDT -> BNB -> ETH -> USDT " + profit);
            }

            profit = inArBot.getProfit("BNBUSDT", "LTCBNB", "LTCUSDT", false);
            Thread.sleep(100);
            if (profit > 0.1) {
                System.out.println("USDT -> BNB -> LTC -> USDT " + profit);
            }

            profit = inArBot.getProfit("BNBUSDT", "NEOBNB", "NEOUSDT", false);
            Thread.sleep(100);
            if (profit > 0.1) {
                System.out.println("USDT -> BNB -> NEO -> USDT " + profit);
            }

            profit = inArBot.getProfit("BNBUSDT", "QTUMBNB", "QTUMUSDT", false);
            Thread.sleep(100);
            if (profit > 0.1) {
                System.out.println("USDT -> BNB -> QTUM -> USDT " + profit);
            }
        }
    }
}

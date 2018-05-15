package ru.algotraid;

public class Main {

    private static String apiKey = "";
    private static String secretKey = "";

    public static void main(String[] args) throws InterruptedException {

        InArBot inArBot = new InArBot(apiKey, secretKey, true);
        BalanceCache balanceCache = new BalanceCache(apiKey, secretKey);

        while (true) {

            Double bet = 1000.0; //ставка ($)
            Double step = 0.001; //значение срабатывания (%)
            Double profit; //профит (%)

            System.out.println(balanceCache.getAccountBalanceCache().get("USDT").getFree());
//            System.out.println(inArBot.getFreeBalance("USDT"));

            profit = inArBot.getProfit(bet, "ADAUSDT", "ADABNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> ADA -> BNB -> USDT " + profit);
                inArBot.buyCycle(bet,"ADAUSDT", "ADAUSDT", "BNBUSDT", true);

            }

            profit = inArBot.getProfit(bet, "BCCUSDT", "BCCBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BCC -> BNB -> USDT " + profit);
                inArBot.buyCycle(bet,"BCCUSDT", "BCCBNB", "BNBUSDT", true);

            }

            profit = inArBot.getProfit(bet, "BTCUSDT", "BNBBTC", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BTC -> BNB -> USDT " + profit);
                inArBot.buyCycle(bet,"BTCUSDT", "BNBBTC", "BNBUSDT", true);

            }

            profit = inArBot.getProfit(bet, "ETHUSDT", "BNBETH", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> ETH -> BNB -> USDT " + profit);
                inArBot.buyCycle(bet,"ETHUSDT", "BNBETH", "BNBUSDT", true);

            }

            profit = inArBot.getProfit(bet, "LTCUSDT", "LTCBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> LTC -> BNB -> USDT " + profit);
                inArBot.buyCycle(bet,"LTCUSDT", "LTCBNB", "BNBUSDT", true);

            }

            profit = inArBot.getProfit(bet, "NEOUSDT", "NEOBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> NEO -> BNB -> USDT " + profit);
                inArBot.buyCycle(bet,"NEOUSDT", "NEOBNB", "BNBUSDT", true);

            }

            profit = inArBot.getProfit(bet, "QTUMUSDT", "QTUMBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> QTUM -> BNB -> USDT " + profit);
                inArBot.buyCycle(bet,"QTUMUSDT", "QTUMBNB", "BNBUSDT", true);

            }

            profit = inArBot.getProfit(bet, "BNBUSDT", "ADABNB", "ADAUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BNB -> ADA -> USDT " + profit);
                inArBot.buyCycle(bet,"BNBUSDT", "ADABNB", "ADAUSDT", false);

            }

            profit = inArBot.getProfit(bet, "BNBUSDT", "BCCBNB", "BCCUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BNB -> BCC -> USDT " + profit);
                inArBot.buyCycle(bet,"BNBUSDT", "BCCBNB", "BCCUSDT", false);

            }

            profit = inArBot.getProfit(bet, "BNBUSDT", "BNBBTC", "BTCUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BNB -> BTC -> USDT " + profit);
                inArBot.buyCycle(bet,"BNBUSDT", "BNBBTC", "BTCUSDT", false);

            }

            profit = inArBot.getProfit(bet, "BNBUSDT", "BNBETH", "ETHUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BNB -> ETH -> USDT " + profit);
                inArBot.buyCycle(bet,"BNBUSDT", "BNBETH", "ETHUSDT", false);

            }

            profit = inArBot.getProfit(bet, "BNBUSDT", "LTCBNB", "LTCUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BNB -> LTC -> USDT " + profit);
                inArBot.buyCycle(bet,"BNBUSDT", "LTCBNB", "LTCUSDT", false);

            }

            profit = inArBot.getProfit(bet, "BNBUSDT", "NEOBNB", "NEOUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BNB -> NEO -> USDT " + profit);
                inArBot.buyCycle(bet,"BNBUSDT", "NEOBNB", "NEOUSDT", false);

            }

            profit = inArBot.getProfit(bet, "BNBUSDT", "QTUMBNB", "QTUMUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                System.out.println("USDT -> BNB -> QTUM -> USDT " + profit);
                inArBot.buyCycle(bet,"BNBUSDT", "QTUMBNB", "QTUMUSDT", false);

            }
        }
    }
}

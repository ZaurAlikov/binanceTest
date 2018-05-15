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

            profit = inArBot.getProfit(bet,"ADAUSDT", "ADABNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                inArBot.buyCicle("ADAUSDT", "ADAUSDT", "BNBUSDT", bet);
                System.out.println("USDT -> ADA -> BNB -> USDT " + profit);
            }

            profit = inArBot.getProfit(bet,"BCCUSDT", "BCCBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                inArBot.buyCicle("BCCUSDT", "BCCBNB", "BNBUSDT", bet);
                System.out.println("USDT -> BCC -> BNB -> USDT " + profit);
            }

            profit = inArBot.getProfit(bet,"BTCUSDT", "BNBBTC", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                inArBot.buyCicle("BTCUSDT", "BNBBTC", "BNBUSDT", bet);
                System.out.println("USDT -> BTC -> BNB -> USDT " + profit);
            }

            profit = inArBot.getProfit(bet,"ETHUSDT", "BNBETH", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                inArBot.buyCicle("ETHUSDT", "BNBETH", "BNBUSDT", bet);
                System.out.println("USDT -> ETH -> BNB -> USDT " + profit);
            }

            profit = inArBot.getProfit(bet,"LTCUSDT", "LTCBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                inArBot.buyCicle("LTCUSDT", "LTCBNB", "BNBUSDT", bet);
                System.out.println("USDT -> LTC -> BNB -> USDT " + profit);
            }

            profit = inArBot.getProfit(bet,"NEOUSDT", "NEOBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                inArBot.buyCicle("NEOUSDT", "NEOBNB", "BNBUSDT", bet);
                System.out.println("USDT -> NEO -> BNB -> USDT " + profit);
            }

            profit = inArBot.getProfit(bet,"QTUMUSDT", "QTUMBNB", "BNBUSDT", true);
            Thread.sleep(100);
            if (profit > step) {
                inArBot.buyCicle("QTUMUSDT", "QTUMBNB", "BNBUSDT", bet);
                System.out.println("USDT -> QTUM -> BNB -> USDT " + profit);
            }

            profit = inArBot.getProfit(bet,"BNBUSDT", "ADABNB", "ADAUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                inArBot.buyCicle("BNBUSDT", "ADABNB", "ADAUSDT", bet);
                System.out.println("USDT -> BNB -> ADA -> USDT " + profit);
            }

            profit = inArBot.getProfit(bet,"BNBUSDT", "BCCBNB", "BCCUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                inArBot.buyCicle("BNBUSDT", "BCCBNB", "BCCUSDT", bet);
                System.out.println("USDT -> BNB -> BCC -> USDT " + profit);
            }

            profit = inArBot.getProfit(bet,"BNBUSDT", "BNBBTC", "BTCUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                inArBot.buyCicle("BNBUSDT", "BNBBTC", "BTCUSDT", bet);
                System.out.println("USDT -> BNB -> BTC -> USDT " + profit);
            }

            profit = inArBot.getProfit(bet,"BNBUSDT", "BNBETH", "ETHUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                inArBot.buyCicle("BNBUSDT", "BNBETH", "ETHUSDT", bet);
                System.out.println("USDT -> BNB -> ETH -> USDT " + profit);
            }

            profit = inArBot.getProfit(bet,"BNBUSDT", "LTCBNB", "LTCUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                inArBot.buyCicle("BNBUSDT", "LTCBNB", "LTCUSDT", bet);
                System.out.println("USDT -> BNB -> LTC -> USDT " + profit);
            }

            profit = inArBot.getProfit(bet,"BNBUSDT", "NEOBNB", "NEOUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                inArBot.buyCicle("BNBUSDT", "NEOBNB", "NEOUSDT", bet);
                System.out.println("USDT -> BNB -> NEO -> USDT " + profit);
            }

            profit = inArBot.getProfit(bet,"BNBUSDT", "QTUMBNB", "QTUMUSDT", false);
            Thread.sleep(100);
            if (profit > step) {
                inArBot.buyCicle("BNBUSDT", "QTUMBNB", "QTUMUSDT", bet);
                System.out.println("USDT -> BNB -> QTUM -> USDT " + profit);
            }
        }
    }
}

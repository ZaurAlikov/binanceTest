package ru.algotraid;

public class Main {

    private static String apiKey = "";
    private static String secretKey = "";

    public static void main(String[] args) throws InterruptedException {
        InArBot inArBot = new InArBot(apiKey, secretKey, true);

        while (true){
            System.out.println("USDT -> ADA -> BNB -> USDT " +
                    inArBot.getProfit("ADAUSDT","ADABNB","BNBUSDT", true));
            Thread.sleep(100);

            System.out.println("USDT -> BCC -> BNB -> USDT " +
                    inArBot.getProfit("BCCUSDT","BCCBNB","BNBUSDT", true));
            Thread.sleep(100);

            System.out.println("USDT -> BTC -> BNB -> USDT " +
                    inArBot.getProfit("BTCUSDT","BNBBTC","BNBUSDT", true));
            Thread.sleep(100);

            System.out.println("USDT -> ETH -> BNB -> USDT " +
                    inArBot.getProfit("ETHUSDT","BNBETH","BNBUSDT", true));
            Thread.sleep(100);

            System.out.println("USDT -> LTC -> BNB -> USDT " +
                    inArBot.getProfit("LTCUSDT","LTCBNB","BNBUSDT", true));
            Thread.sleep(100);

            System.out.println("USDT -> NEO -> BNB -> USDT " +
                    inArBot.getProfit("NEOUSDT","NEOBNB","BNBUSDT", true));
            Thread.sleep(100);

            System.out.println("USDT -> QTUM -> BNB -> USDT " +
                    inArBot.getProfit("QTUMUSDT","QTUMBNB","BNBUSDT", true));
            Thread.sleep(100);

            System.out.println("USDT -> BNB -> ADA -> USDT " +
                    inArBot.getProfit("BNBUSDT","ADABNB","ADAUSDT", false));
            Thread.sleep(100);

            System.out.println("USDT -> BNB -> BCC -> USDT " +
                    inArBot.getProfit("BNBUSDT","BCCBNB","BCCUSDT", false));
            Thread.sleep(100);

            System.out.println("USDT -> BNB -> BTC -> USDT " +
                    inArBot.getProfit("BNBUSDT","BNBBTC","BTCUSDT", false));
            Thread.sleep(100);

            System.out.println("USDT -> BNB -> ETH -> USDT " +
                    inArBot.getProfit("BNBUSDT","BNBETH","ETHUSDT", false));
            Thread.sleep(100);

            System.out.println("USDT -> BNB -> LTC -> USDT " +
                    inArBot.getProfit("BNBUSDT","LTCBNB","LTCUSDT", false));
            Thread.sleep(100);

            System.out.println("USDT -> BNB -> NEO -> USDT " +
                    inArBot.getProfit("BNBUSDT","NEOBNB","NEOUSDT", false));
            Thread.sleep(100);

            System.out.println("USDT -> BNB -> QTUM -> USDT " +
                    inArBot.getProfit("BNBUSDT","QTUMBNB","QTUMUSDT", false));
            Thread.sleep(100);
        }
    }
}

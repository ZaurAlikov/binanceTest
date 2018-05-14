package ru.algotraid;

import com.binance.api.client.impl.BinanceApiRestClientImpl;

public class InArBot {

    private Double initCoin = 1000.0;
    private Double commission;
    private BinanceApiRestClientImpl apiRestClient;

    public InArBot(String apiKey, String secretKey, Boolean BNBCommission) {
        apiRestClient = new BinanceApiRestClientImpl(apiKey, secretKey);
        if (BNBCommission) commission = 0.0005;
        else commission = 0.001;
    }

    public Double getProfit(String firstPair, String secondPair, String thirdPair, boolean directCalc){
        Double firstBuy;
        Double secondBuy;
        Double thirdBuy;

        Double firstPairPrice = Double.valueOf(apiRestClient.getPrice(firstPair).getPrice());
        Double secondPairPrice = Double.valueOf(apiRestClient.getPrice(secondPair).getPrice());
        Double thirdPairPrice = Double.valueOf(apiRestClient.getPrice(thirdPair).getPrice());
        if (directCalc) firstBuy = withCommission(initCoin) / firstPairPrice;
        else firstBuy = withCommission(initCoin) * firstPairPrice;
        if (secondPair.contains("BTC") || secondPair.contains("ETH")){
            secondBuy = withCommission(firstBuy) / secondPairPrice;
        } else {
            secondBuy = withCommission(firstBuy) * secondPairPrice;
        }
        if (directCalc) thirdBuy = withCommission(secondBuy) * thirdPairPrice;
        else thirdBuy = withCommission(secondBuy) / thirdPairPrice;
        return thirdBuy - initCoin;
    }

    private Double withCommission(Double withoutCommission){
        return withoutCommission - (withoutCommission * commission);
    }
}

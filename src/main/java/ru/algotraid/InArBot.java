package ru.algotraid;

import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.impl.BinanceApiRestClientImpl;

import java.util.List;
import java.util.Optional;

public class InArBot {

    private Double initCoin = 100.0;
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
        Double firstPairPrice;
        Double secondPairPrice;
        Double thirdPairPrice;

        List<TickerPrice> prices = apiRestClient.getAllPrices();

        firstPairPrice = getPrice(prices, firstPair);
        secondPairPrice = getPrice(prices, secondPair);
        thirdPairPrice = getPrice(prices, thirdPair);

        if (firstPairPrice != 0.0 && secondPairPrice != 0.0 && thirdPairPrice !=0.0){
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
        return 0.0;
    }

    private Double withCommission(Double withoutCommission){
        return withoutCommission - (withoutCommission * commission);
    }

    private Double getPrice(List<TickerPrice> prices, String pair){
        Optional<TickerPrice> tickerPrice = prices.stream().filter(s -> s.getSymbol().equals(pair)).findFirst();
        return tickerPrice.map(tickerPrice1 -> Double.valueOf(tickerPrice1.getPrice())).orElse(0.0);
    }
}

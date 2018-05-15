package ru.algotraid;

import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.SymbolInfo;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.impl.BinanceApiRestClientImpl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class InArBot {

    private Double commission;
    private BinanceApiRestClientImpl apiRestClient;
    private ExchangeInfo exchangeInfo;
    private Double firstPairPrice;
    private Double secondPairPrice;
    private Double thirdPairPrice;

    public InArBot(String apiKey, String secretKey, Boolean BNBCommission) {
        this.apiRestClient = new BinanceApiRestClientImpl(apiKey, secretKey);
        this.exchangeInfo = apiRestClient.getExchangeInfo();
        if (BNBCommission) commission = 0.0005;
        else commission = 0.001;
    }

    public Double getProfit(Double bet, String firstPair, String secondPair, String thirdPair, boolean directCalc) {
        Double firstBuy;
        Double secondBuy;
        Double thirdBuy;

        List<TickerPrice> prices = apiRestClient.getAllPrices();
        firstPairPrice = getPrice(prices, firstPair);
        secondPairPrice = getPrice(prices, secondPair);
        thirdPairPrice = getPrice(prices, thirdPair);

        if (firstPairPrice != 0.0 && secondPairPrice != 0.0 && thirdPairPrice != 0.0) {
            if (directCalc) firstBuy = withCommission(bet) / firstPairPrice;
            else firstBuy = withCommission(bet) * firstPairPrice;
            if (secondPair.contains("BTC") || secondPair.contains("ETH")) {
                secondBuy = withCommission(firstBuy) / secondPairPrice;
            } else {
                secondBuy = withCommission(firstBuy) * secondPairPrice;
            }
            if (directCalc) thirdBuy = withCommission(secondBuy) * thirdPairPrice;
            else thirdBuy = withCommission(secondBuy) / thirdPairPrice;
            return (thirdBuy - bet) * 100 / bet;
        }
        return 0.0;
    }

    public Double getFreeBalance(String symbol) {
        return Double.valueOf(apiRestClient.getAccount(100000L, System.currentTimeMillis()).getAssetBalance(symbol).getFree());
    }

    public void buyCycle(Double sumForTrade, String firstPair, String secondPair, String thirdPair, boolean directBuy){
        Double countPayCoins;
        countPayCoins = buyCoins(sumForTrade, firstPair, firstPairPrice, directBuy, 1);
        countPayCoins = buyCoins(countPayCoins, secondPair, secondPairPrice, directBuy ,2);
        countPayCoins = buyCoins(countPayCoins, thirdPair, thirdPairPrice, directBuy ,3);
        System.out.println("Profit = " + (countPayCoins - sumForTrade));
    }

    private Double buyCoins(Double sumForTrade, String pair, Double pairPrice, boolean direct, int numPair){
        Double pairQuantity = 0.0;
        switch (numPair){
            case 1:
                if (direct) pairQuantity = withCommission(sumForTrade) / pairPrice;
                else pairQuantity = withCommission(sumForTrade) / pairPrice;
                break;
            case 2:
                if (pair.contains("BTC") || pair.contains("ETH")) {
                    pairQuantity = withCommission(sumForTrade) / pairPrice;
                } else {
                    if (direct) pairQuantity = withCommission(sumForTrade) * pairPrice;
                    else pairQuantity = withCommission(sumForTrade) / pairPrice;
                }
                break;
            case 3:
                if (direct) pairQuantity = withCommission(sumForTrade) * pairPrice;
                else pairQuantity = withCommission(sumForTrade) * pairPrice;
                break;
        }

//        if (pairQuantity == 0.0){
            SymbolInfo pairInfo = exchangeInfo.getSymbolInfo(pair);
            Double step = Double.valueOf(pairInfo.getFilters().get(1).getStepSize());
            String normalQuantity = String.format(Locale.UK,"%.8f", Math.floor(pairQuantity / step) * step);
//        NewOrderResponse response = apiRestClient.newOrder(NewOrder.marketBuy(pair, normalQuantity));
//        return Double.valueOf(response.getExecutedQty());
//        } else return 0.0;


        // for tests
        apiRestClient.newOrderTest(NewOrder.marketBuy(pair, normalQuantity));
        return Double.valueOf(normalQuantity);

    }

    private Double withCommission(Double withoutCommission) {
        return withoutCommission - (withoutCommission * commission);
    }

    private Double getPrice(List<TickerPrice> prices, String pair) {
        Optional<TickerPrice> tickerPrice = prices.stream().filter(s -> s.getSymbol().equals(pair)).findFirst();
        return tickerPrice.map(tickerPrice1 -> Double.valueOf(tickerPrice1.getPrice())).orElse(0.0);
    }

}

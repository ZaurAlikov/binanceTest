package ru.algotraid;

import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.SymbolInfo;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.impl.BinanceApiRestClientImpl;

import java.util.List;
import java.util.Optional;

public class InArBot {

    private Double commission;
    private BinanceApiRestClientImpl apiRestClient;
    private ExchangeInfo exchangeInfo;

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
        Double firstPairPrice;
        Double secondPairPrice;
        Double thirdPairPrice;

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

    public void buyCicle(String firstPair, String secondPair, String thirdPair, Double sumForTrade){

        SymbolInfo firstPairInfo = exchangeInfo.getSymbolInfo(firstPair);
        SymbolInfo secondPairInfo = exchangeInfo.getSymbolInfo(secondPair);
        SymbolInfo thirdPairInfo = exchangeInfo.getSymbolInfo(thirdPair);

        apiRestClient.newOrderTest(NewOrder.marketBuy(firstPair, sumForTrade.toString()));
    }

    private Double withCommission(Double withoutCommission) {
        return withoutCommission - (withoutCommission * commission);
    }

    private Double getPrice(List<TickerPrice> prices, String pair) {
        Optional<TickerPrice> tickerPrice = prices.stream().filter(s -> s.getSymbol().equals(pair)).findFirst();
        return tickerPrice.map(tickerPrice1 -> Double.valueOf(tickerPrice1.getPrice())).orElse(0.0);
    }

}

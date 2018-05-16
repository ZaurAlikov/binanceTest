package ru.algotraid;

import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.SymbolInfo;
import com.binance.api.client.domain.general.SymbolStatus;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.exception.BinanceApiException;
import com.binance.api.client.impl.BinanceApiRestClientImpl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class InArBot {

    private Double commission;
    private BinanceApiRestClientImpl apiRestClient;
    private ExchangeInfo exchangeInfo;
    private BalanceCache balanceCache;
    private List<TickerPrice> prices;

    private Double allProfit = 0.0;

    public InArBot(String apiKey, String secretKey, Boolean BNBCommission) throws InterruptedException {
        this.apiRestClient = new BinanceApiRestClientImpl(apiKey, secretKey);
        this.balanceCache = new BalanceCache(apiKey, secretKey);
        this.exchangeInfo = apiRestClient.getExchangeInfo();
        refreshPrices();
        if (BNBCommission) commission = 0.0005;
        else commission = 0.001;
    }

    public Double getProfit(Double bet, String firstPair, String secondPair, String thirdPair, boolean directCalc) throws InterruptedException {
        Double firstBuy;
        Double secondBuy;
        Double thirdBuy;
        refreshPrices();
        Double firstPairPrice = getPrice(firstPair);
        Double secondPairPrice = getPrice(secondPair);
        Double thirdPairPrice = getPrice(thirdPair);
        if (firstPairPrice != 0.0 && secondPairPrice != 0.0 && thirdPairPrice != 0.0) {
            firstBuy = withCommission(bet) / firstPairPrice;
            firstBuy = Double.valueOf(normalizeQuantity(firstPair, firstBuy));
            secondBuy = secondPairCalc(secondPair, firstBuy, directCalc);
            secondBuy = Double.valueOf(normalizeQuantity(secondPair, secondBuy));
            thirdBuy = withCommission(secondBuy) * thirdPairPrice;
            thirdBuy = Double.valueOf(normalizeQuantity(thirdPair, thirdBuy));
            return (thirdBuy - bet) * (100 / bet);
        }
        return 0.0;
    }

    public Double getFreeBalance(String symbol) {
        return Double.valueOf(apiRestClient.getAccount(100000L, System.currentTimeMillis()).getAssetBalance(symbol).getFree());
    }

    public void buyCycle(Double sumForTrade, String firstPair, String secondPair, String thirdPair, boolean directBuy) {
        Double countPayCoins;
        countPayCoins = Double.valueOf(buyCoins(sumForTrade, firstPair, directBuy, 1).getExecutedQty());
        countPayCoins = Double.valueOf(buyCoins(countPayCoins, secondPair, directBuy, 2).getExecutedQty());
        countPayCoins = Double.valueOf(buyCoins(countPayCoins, thirdPair, directBuy, 3).getExecutedQty());

        if (countPayCoins != 0.0) {
            allProfit = allProfit + (countPayCoins - sumForTrade);
            System.out.println("Profit = " + (countPayCoins - sumForTrade) + " | All profit = " + allProfit);
        } else {
            System.out.println("Profit = " + 0.0);
        }
    }

    private NewOrderResponse buyCoins(Double sumForTrade, String pair, boolean direct, int numPair) {
        Double pairQuantity;
        String normalQuantity = "0.0";
        NewOrderResponse orderResponse = new NewOrderResponse();
//        refreshPrices();

        switch (numPair) {
            case 1:
                pairQuantity = withCommission(sumForTrade) / getPrice(pair);
                normalQuantity = normalizeQuantity(pair, pairQuantity);
//                if (isValidQty(pair, normalQuantity)) orderResponse = buyOrSell(pair, normalQuantity, numPair, direct);
                break;
            case 2:
                pairQuantity = secondPairCalc(pair, sumForTrade, direct);
                normalQuantity = normalizeQuantity(pair, pairQuantity);
//                if (isValidQty(pair, normalQuantity)) orderResponse = buyOrSell(pair, normalQuantity, numPair, direct);
                break;
            case 3:
                pairQuantity = withCommission(sumForTrade) * getPrice(pair);
                normalQuantity = normalizeQuantity(pair, pairQuantity);
//                if (isValidQty(pair, normalQuantity)) orderResponse = buyOrSell(pair, normalQuantity, numPair, direct);
                break;
            default:
        }
//--------- tests ------------------------------------------------------------------------------------------------------
        if (isValidQty(pair, normalQuantity)) {
            apiRestClient.newOrderTest(NewOrder.marketBuy(pair, normalQuantity));
            orderResponse.setExecutedQty(normalQuantity);
            return orderResponse;
        }
        orderResponse.setExecutedQty(normalQuantity);
        return orderResponse;
//----------------------------------------------------------------------------------------------------------------------
//        return orderResponse;
    }

    private Double withCommission(Double withoutCommission) {
        return withoutCommission - (withoutCommission * commission);
    }

    private String normalizeQuantity(String pair, Double pairQuantity) {
        SymbolInfo pairInfo = exchangeInfo.getSymbolInfo(pair);
        Double step = Double.valueOf(pairInfo.getFilters().get(1).getStepSize());
        return String.format(Locale.UK, "%.8f", Math.floor(pairQuantity / step) * step);
    }

    private Boolean isValidQty(String pair, String normalQuantity) {
        SymbolInfo pairInfo = exchangeInfo.getSymbolInfo(pair);
        Double minQty = Double.valueOf(pairInfo.getFilters().get(1).getMinQty());
        Double maxQty = Double.valueOf(pairInfo.getFilters().get(1).getMaxQty());
        return Double.valueOf(normalQuantity) > minQty && Double.valueOf(normalQuantity) < maxQty &&
                pairInfo.getStatus().equals(SymbolStatus.TRADING);
    }

    private NewOrderResponse buyOrSell(String pair, String normalQuantity, int numPair, boolean direct) {
        NewOrderResponse response;
        switch (numPair) {
            case 1:
                response = apiRestClient.newOrder(NewOrder.marketBuy(pair, normalQuantity));
                break;
            case 2:
                if (direct) {
                    if (pair.contains("BTC") || pair.contains("ETH")) {
                        response = apiRestClient.newOrder(NewOrder.marketBuy(pair, normalQuantity));
                    } else {
                        response = apiRestClient.newOrder(NewOrder.marketSell(pair, normalQuantity));
                    }
                } else {
                    if (pair.contains("BTC") || pair.contains("ETH")) {
                        response = apiRestClient.newOrder(NewOrder.marketSell(pair, normalQuantity));
                    } else {
                        response = apiRestClient.newOrder(NewOrder.marketBuy(pair, normalQuantity));
                    }
                }
                break;
            case 3:
                response = apiRestClient.newOrder(NewOrder.marketSell(pair, normalQuantity));
                break;
            default:
                response = new NewOrderResponse();
                response.setExecutedQty("0.0");
        }
        return response;
    }

    private Double secondPairCalc(String pair, Double firstBuy, boolean direct) {
        Double secondBuy;
        if (direct) {
            if (pair.contains("BTC") || pair.contains("ETH")) {
                secondBuy = withCommission(firstBuy) / getPrice(pair);
            } else {
                secondBuy = withCommission(firstBuy) * getPrice(pair);
            }
        } else {
            if (pair.contains("BTC") || pair.contains("ETH")) {
                secondBuy = withCommission(firstBuy) * getPrice(pair);
            } else {
                secondBuy = withCommission(firstBuy) / getPrice(pair);
            }
        }
        return secondBuy;
    }

    private Double getPrice(String pair) {
        Optional<TickerPrice> tickerPrice = prices.stream().filter(s -> s.getSymbol().equals(pair)).findFirst();
        return tickerPrice.map(tickerPrice1 -> Double.valueOf(tickerPrice1.getPrice())).orElse(0.0);
    }

    private void refreshPrices() throws InterruptedException {
        int timeoutCount = 0;
        do {
            try {
                prices = apiRestClient.getAllPrices();
                break;
            } catch (BinanceApiException e) {
                ++timeoutCount;
                System.err.println("Сработал таймаут, пробую еще раз");
                Thread.sleep(100);
            }
        } while (timeoutCount <= 9);
    }

    public BalanceCache getBalanceCache() {
        return balanceCache;
    }
}

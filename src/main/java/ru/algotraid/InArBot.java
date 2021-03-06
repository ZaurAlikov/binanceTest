package ru.algotraid;

import com.binance.api.client.BinanceApiAsyncRestClient;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.SymbolInfo;
import com.binance.api.client.domain.general.SymbolStatus;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.exception.BinanceApiException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class InArBot {

    private Double commission;
    private BinanceApiRestClient apiRestClient;
    private BinanceApiAsyncRestClient apiAsyncRestClient;
    private ExchangeInfo exchangeInfo;
    private BalanceCache balanceCache;
    private List<TickerPrice> prices;
    private Double allProfit = 0.0;

    public InArBot(String apiKey, String secretKey, Boolean BNBCommission) throws InterruptedException {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secretKey);
        this.apiRestClient = factory.newRestClient();
        this.apiAsyncRestClient = factory.newAsyncRestClient();
        this.balanceCache = new BalanceCache(apiKey, secretKey);
        prices = apiRestClient.getAllPrices();
        exchangeInfo = apiRestClient.getExchangeInfo();
        startRefreshingExchangeInfo();
        startRefreshingPrices();
        if (BNBCommission) commission = 0.0005;
        else commission = 0.001;
    }

    public Double getProfit(Double startAmt, PairTriangle pairTriangle) throws InterruptedException {
        String firstPair = pairTriangle.getFirstPair();
        String secondPair = pairTriangle.getSecondPair();
        String thirdPair = pairTriangle.getThirdPair();
        Double firstPairPrice = getPrice(firstPair);
        Double secondPairPrice = getPrice(secondPair);
        Double thirdPairPrice = getPrice(thirdPair);
        Double amtAfterFirstTransaction;
        Double amtAfterSecondTransaction;
        Double amtAfterThirdTransaction;
        if (firstPairPrice != 0.0 && secondPairPrice != 0.0 && thirdPairPrice != 0.0) {
            amtAfterFirstTransaction = Double.valueOf(normalizeQuantity(firstPair, withCommission(startAmt / firstPairPrice)));
            boolean isNotional1 = isNotional(amtAfterFirstTransaction, firstPair);
            boolean isNotional2;
            if (pairTriangle.isDirect()) {
                if (secondPair.contains("BTC") || secondPair.contains("ETH")) {
                    amtAfterSecondTransaction = Double.valueOf(normalizeQuantity(secondPair, withCommission(amtAfterFirstTransaction / secondPairPrice)));
                    isNotional2 = isNotional(amtAfterSecondTransaction, secondPair);
                } else {
                    amtAfterSecondTransaction = Double.valueOf(normalizeQuantity(secondPair, withCommission(amtAfterFirstTransaction * secondPairPrice)));
                    isNotional2 = isNotional(amtAfterSecondTransaction, secondPair);
                }
            } else {
                if (secondPair.contains("BTC") || secondPair.contains("ETH")) {
                    amtAfterSecondTransaction = Double.valueOf(normalizeQuantity(secondPair, withCommission(amtAfterFirstTransaction * secondPairPrice)));
                    isNotional2 = isNotional(amtAfterSecondTransaction, secondPair);
                } else {
                    amtAfterSecondTransaction = Double.valueOf(normalizeQuantity(secondPair, withCommission(amtAfterFirstTransaction / secondPairPrice)));
                    isNotional2 = isNotional(amtAfterSecondTransaction, secondPair);
                }
            }
            amtAfterThirdTransaction = withCommission(amtAfterSecondTransaction * thirdPairPrice);
            boolean isNotional3 = isNotional(amtAfterThirdTransaction, thirdPair);
            if (isNotional1 && isNotional2 && isNotional3){
                Double d = (amtAfterThirdTransaction - startAmt) * (100 / startAmt);
//                System.out.println("Профит в методе getProfit" + amtAfterThirdTransaction);
                return (amtAfterThirdTransaction - startAmt) * (100 / startAmt);
            } else return 0.0;
        }
        return 0.0;
    }

    public Double getFreeBalance(String symbol) {
        return Double.valueOf(apiRestClient.getAccount(100000L, System.currentTimeMillis()).getAssetBalance(symbol).getFree());
    }

    public void buyCycle(Double startAmt, PairTriangle pairTriangle) throws InterruptedException {
        String firstPair = pairTriangle.getFirstPair();
        String secondPair = pairTriangle.getSecondPair();
        String thirdPair = pairTriangle.getThirdPair();
        boolean direct = pairTriangle.isDirect();
        if (isAllPairTrading(pairTriangle)) {
            Double amtAfterFirstTransaction = Double.valueOf(TestBuyCoins(startAmt, firstPair, direct, 1).getExecutedQty());
            Double amtAfterSecondTransaction = Double.valueOf(TestBuyCoins(amtAfterFirstTransaction, secondPair, direct, 2).getExecutedQty());
            Double amtAfterThirdTransaction = Double.valueOf(TestBuyCoins(amtAfterSecondTransaction, thirdPair, direct, 3).getExecutedQty());
//            System.out.println("Профит в методе buyCycle" + countPayCoins);
            if (amtAfterThirdTransaction != 0.0) {
                allProfit = allProfit + (amtAfterThirdTransaction - startAmt);
                System.out.println("Profit = " + (amtAfterThirdTransaction - startAmt) + " | All profit = " + allProfit);
            } else {
                System.out.println("Profit = " + 0.0);
            }
        }
    }

    private NewOrderResponse buyCoins(Double amtForTrade, String pair, boolean direct, int numPair) {
        Double pairQuantity;
        String normalQuantity;
        NewOrderResponse orderResponse = new NewOrderResponse();

        switch (numPair) {
            case 1:
                pairQuantity = amtForTrade / getPrice(pair);
                normalQuantity = normalizeQuantity(pair, pairQuantity);
                if (isValidQty(pair, normalQuantity)) orderResponse = buyOrSell(pair, normalQuantity, numPair, direct);
                break;
            case 2:
                if (direct) {
                    if (pair.contains("BTC") || pair.contains("ETH")) {
                        pairQuantity = amtForTrade / getPrice(pair);
                        normalQuantity = normalizeQuantity(pair, pairQuantity);
                        if (isValidQty(pair, normalQuantity)) orderResponse = buyOrSell(pair, normalQuantity, numPair, true);
                    } else {
                        normalQuantity = normalizeQuantity(pair, amtForTrade);
                        if (isValidQty(pair, normalQuantity)) orderResponse = buyOrSell(pair, normalQuantity, numPair, true);
                    }
                } else {
                    if (pair.contains("BTC") || pair.contains("ETH")) {
                        normalQuantity = normalizeQuantity(pair, amtForTrade);
                        if (isValidQty(pair, normalQuantity)) orderResponse = buyOrSell(pair, normalQuantity, numPair, false);
                    } else {
                        pairQuantity = amtForTrade / getPrice(pair);
                        normalQuantity = normalizeQuantity(pair, pairQuantity);
                        if (isValidQty(pair, normalQuantity)) orderResponse = buyOrSell(pair, normalQuantity, numPair, false);
                    }
                }
                break;
            case 3:
                normalQuantity = normalizeQuantity(pair, amtForTrade);
                if (isValidQty(pair, normalQuantity)) orderResponse = buyOrSell(pair, normalQuantity, numPair, direct);
                break;
            default:
        }
        return orderResponse;
    }

    private NewOrderResponse TestBuyCoins(Double amtForTrade, String pair, boolean direct, int numPair) {
        Double pairQuantity;
        String normalQuantity;
        NewOrderResponse orderResponse = new NewOrderResponse();

        switch (numPair) {
            case 1:
                pairQuantity = withCommission(amtForTrade / getPrice(pair));
                normalQuantity = normalizeQuantity(pair, pairQuantity);
                if (isValidQty(pair, normalQuantity)) {
                    apiRestClient.newOrderTest(NewOrder.marketBuy(pair, normalQuantity));
                    orderResponse.setExecutedQty(normalQuantity);
                }
                break;
            case 2:
                if (direct) {
                    if (pair.contains("BTC") || pair.contains("ETH")) {
                        pairQuantity = withCommission(amtForTrade / getPrice(pair));
                        normalQuantity = normalizeQuantity(pair, pairQuantity);
                        if (isValidQty(pair, normalQuantity)) {
                            apiRestClient.newOrderTest(NewOrder.marketBuy(pair, normalQuantity));
                            orderResponse.setExecutedQty(normalQuantity);
                        }
                    } else {
                        pairQuantity = withCommission(amtForTrade * getPrice(pair));
                        normalQuantity = normalizeQuantity(pair, pairQuantity);
                        if (isValidQty(pair, amtForTrade.toString())) {
                            apiRestClient.newOrderTest(NewOrder.marketSell(pair, normalizeQuantity(pair, amtForTrade)));
                            orderResponse.setExecutedQty(normalQuantity);
                        }
                    }
                } else {
                    if (pair.contains("BTC") || pair.contains("ETH")) {
                        pairQuantity = withCommission(amtForTrade * getPrice(pair));
                        normalQuantity = normalizeQuantity(pair, pairQuantity);
                        if (isValidQty(pair, amtForTrade.toString())) {
                            apiRestClient.newOrderTest(NewOrder.marketSell(pair, normalizeQuantity(pair, amtForTrade)));
                            orderResponse.setExecutedQty(normalQuantity);
                        }
                    } else {
                        pairQuantity = withCommission(amtForTrade / getPrice(pair));
                        normalQuantity = normalizeQuantity(pair, pairQuantity);
                        if (isValidQty(pair, normalQuantity)) {
                            apiRestClient.newOrderTest(NewOrder.marketBuy(pair, normalQuantity));
                            orderResponse.setExecutedQty(normalQuantity);
                        }
                    }
                }
                break;
            case 3:
                pairQuantity = withCommission(amtForTrade * getPrice(pair));
                normalQuantity = String.valueOf(pairQuantity);
                if (isValidQty(pair, amtForTrade.toString())) {
                    apiRestClient.newOrderTest(NewOrder.marketSell(pair, normalizeQuantity(pair, amtForTrade)));
                    orderResponse.setExecutedQty(normalQuantity);
                }
                break;
            default:
        }
        return orderResponse;
    }

    private Double withCommission(Double withoutCommission) {
        return withoutCommission - (withoutCommission * commission);
    }

    private String normalizeQuantity(String pair, Double pairQuantity) {
        SymbolInfo pairInfo = exchangeInfo.getSymbolInfo(pair);
        Double step = Double.valueOf(pairInfo.getFilters().get(1).getStepSize());
        //        System.out.printf("qty = %s, normQty = %s newPrice = %s;\n", pairQuantity, normalQuantity, getPrice(pair));
        return String.format(Locale.UK, "%.8f", Math.floor(pairQuantity / step) * step);
    }

    private Boolean isValidQty(String pair, String normalQuantity) {
        SymbolInfo pairInfo = exchangeInfo.getSymbolInfo(pair);
        Double minQty = Double.valueOf(pairInfo.getFilters().get(1).getMinQty());
        Double maxQty = Double.valueOf(pairInfo.getFilters().get(1).getMaxQty());
        return Double.valueOf(normalQuantity) > minQty && Double.valueOf(normalQuantity) < maxQty;
    }

    private Boolean isNotional(Double qty, String pair){
        SymbolInfo pairInfo = exchangeInfo.getSymbolInfo(pair);
        return qty * getPrice(pair) > Double.valueOf(pairInfo.getFilters().get(2).getMinNotional());
    }

    private Boolean isAllPairTrading(PairTriangle pairTriangle) {
        boolean pair1 = exchangeInfo.getSymbolInfo(pairTriangle.getFirstPair()).getStatus().equals(SymbolStatus.TRADING);
        boolean pair2 = exchangeInfo.getSymbolInfo(pairTriangle.getSecondPair()).getStatus().equals(SymbolStatus.TRADING);
        boolean pair3 = exchangeInfo.getSymbolInfo(pairTriangle.getThirdPair()).getStatus().equals(SymbolStatus.TRADING);
        return pair1 && pair2 && pair3;
    }

    private NewOrderResponse buyOrSell(String pair, String normalQuantity, int numPair, boolean direct) {
        NewOrderResponse response = new NewOrderResponse();
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
                response.setExecutedQty("0.0");
        }
        return response;
    }

//    private Double secondPairCalc(String pair, Double firstBuy, boolean direct) {
////        System.out.printf("pricePair = %s, bet = %s, betWithCom = %s; \n%s; \n", getPrice(pair), firstBuy, withCommission(firstBuy), balanceCache.getAccountBalanceCache());
//        Double secondBuy;
//        if (direct) {
//            if (pair.contains("BTC") || pair.contains("ETH")) {
//                secondBuy = withCommission(firstBuy) / getPrice(pair);
//            } else {
//                secondBuy = withCommission(firstBuy) * getPrice(pair);
//            }
//        } else {
//            if (pair.contains("BTC") || pair.contains("ETH")) {
//                secondBuy = withCommission(firstBuy) * getPrice(pair);
//            } else {
//                secondBuy = withCommission(firstBuy) / getPrice(pair);
//            }
//        }
//        return secondBuy;
//    }

    private Double getPrice(String pair) {
        Optional<TickerPrice> tickerPrice = prices.stream().filter(s -> s.getSymbol().equals(pair)).findFirst();
        return tickerPrice.map(tickerPrice1 -> Double.valueOf(tickerPrice1.getPrice())).orElse(0.0);
    }

    private void startRefreshingPrices() {
        new Thread(() -> {
            while (true) {
                int timeoutCount = 0;
                do {
                    try {
                        apiAsyncRestClient.getAllPrices((List<TickerPrice> response) -> prices = response);
                        break;
                    } catch (BinanceApiException e) {
                        ++timeoutCount;
                        System.err.println("Что-то пошло не так, пробую еще раз");
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                } while (timeoutCount <= 100);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }).start();
    }

//    private void refreshPrices() throws InterruptedException {
//        int timeoutCount = 0;
//        do {
//            try {
//                prices = apiRestClient.getAllPrices();
//                break;
//            } catch (BinanceApiException e) {
//                ++timeoutCount;
//                System.err.println("Сработал таймаут, пробую еще раз");
//                Thread.sleep(100);
//            }
//        } while (timeoutCount <= 9);
//    }

    private void startRefreshingExchangeInfo() {
        new Thread(() -> {
            while (true){
                int timeoutCount = 0;
                do {
                    try {
                        apiAsyncRestClient.getExchangeInfo((ExchangeInfo response) -> exchangeInfo = response);
                        break;
                    } catch (BinanceApiException e){
                        ++timeoutCount;
                        System.err.println("Что-то пошло не так, пробую еще раз");
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                } while (timeoutCount <= 100);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }).start();
    }

    public BalanceCache getBalanceCache() {
        return balanceCache;
    }
}

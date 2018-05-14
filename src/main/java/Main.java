import com.binance.api.client.impl.BinanceApiRestClientImpl;

import java.text.SimpleDateFormat;

public class Main {

    private String apiKey = "";
    private String secretKey = "";
    private static Double initCoin = 100.0;
    private static Double comission = 0.1;

    private BinanceApiRestClientImpl apiRestClient;

    public Main(){
        apiRestClient = new BinanceApiRestClientImpl(apiKey, secretKey);
    }

    public static void main(String[] args) throws InterruptedException {
        while (true){
            System.out.println(getProfit("LTCUSDT","LTCBTC","BTCUSDT"));
            Thread.sleep(200);
        }
    }

    static Double getProfit(String firstPair, String secondPair, String thirdPair){
        Main main = new Main();
        Double firstPairPrice = Double.valueOf(main.apiRestClient.getPrice(firstPair).getPrice());
        Double secondPairPrice = Double.valueOf(main.apiRestClient.getPrice(secondPair).getPrice());
        Double thirdPairPrice = Double.valueOf(main.apiRestClient.getPrice(thirdPair).getPrice());
        Double firstBuy = withCommision(initCoin) / firstPairPrice;
        Double secondBuy = withCommision(firstBuy) * secondPairPrice;
        Double thirdBuy = withCommision(secondBuy) * thirdPairPrice;
        return thirdBuy - initCoin;
    }

    static Double withCommision(Double withoutComission){
        return withoutComission - (withoutComission * comission / 100);
    }
}

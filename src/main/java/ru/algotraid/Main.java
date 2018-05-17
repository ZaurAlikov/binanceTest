package ru.algotraid;

public class Main {

    private static String apiKey = "";
    private static String secretKey = "";

    public static void main(String[] args) throws InterruptedException {
        MainBot mainBot = new MainBot(apiKey, secretKey);
        mainBot.start();
    }
}

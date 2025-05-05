package com.example.bitcoin.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TechnicalIndicatorUtil {

    public static double calculateSMA(List<Double> prices, int period) {
        if (prices.size() < period) return 0;
        return prices.subList(prices.size() - period, prices.size()).stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }

    public static double calculateEMA(List<Double> prices, int period) {
        if (prices.size() < period) return 0.0;

        double k = 2.0 / (period + 1);
        double ema = prices.subList(0, period).stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        for (int i = period; i < prices.size(); i++) {
            ema = prices.get(i) * k + ema * (1 - k);
        }

        return ema;
    }

    public static double calculateRSI(List<Double> prices, int period) {
        if (prices.size() <= period) return 0;

        double gain = 0, loss = 0;
        for (int i = prices.size() - period; i < prices.size() - 1; i++) {
            double change = prices.get(i + 1) - prices.get(i);
            if (change > 0) gain += change;
            else loss -= change;
        }

        if (loss == 0) return 100;
        double rs = gain / loss;
        return 100 - (100 / (1 + rs));
    }

    public static Map<String, Double> calculateMACD(List<Double> prices, int shortPeriod, int longPeriod, int signalPeriod) {
        Map<String, Double> result = new HashMap<>();
        double shortEMA = calculateEMA(prices, shortPeriod);
        double longEMA = calculateEMA(prices, longPeriod);
        double macdLine = shortEMA - longEMA;

        List<Double> macdHistory = prices.stream()
                .map(price -> calculateEMA(prices.subList(0, prices.indexOf(price) + 1), shortPeriod)
                        - calculateEMA(prices.subList(0, prices.indexOf(price) + 1), longPeriod))
                .toList();

        double signalLine = calculateEMA(macdHistory, signalPeriod);

        result.put("macd", macdLine);
        result.put("signal", signalLine);
        return result;
    }

    public static Map<String, Double> calculateBollingerBands(List<Double> prices, int period) {
        Map<String, Double> result = new HashMap<>();
        if (prices.size() < period) return result;

        List<Double> recentPrices = prices.subList(prices.size() - period, prices.size());
        double sma = recentPrices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        double variance = recentPrices.stream()
                .mapToDouble(p -> Math.pow(p - sma, 2))
                .average()
                .orElse(0.0);

        double stdDev = Math.sqrt(variance);

        result.put("upper", sma + 2 * stdDev);
        result.put("lower", sma - 2 * stdDev);
        result.put("middle", sma);
        return result;
    }
}

package com.example.bitcoin.util;

import java.util.List;

public class TechnicalIndicatorUtil {

    public static double calculateSMA(List<Double> prices, int period) {
        if (prices.size() < period) return 0;
        return prices.subList(prices.size() - period, prices.size()).stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
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
}


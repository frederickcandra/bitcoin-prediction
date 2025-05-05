package com.example.bitcoin.service;

import com.example.bitcoin.model.PriceData;
import com.example.bitcoin.util.TechnicalIndicatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PredictionService {

    @Autowired
    private BitcoinPriceService priceService;

    public Map<String, Object> predict() {
        List<PriceData> data = priceService.getHistoricalPrices();
        List<Double> prices = data.stream()
                .map(PriceData::getPrice)
                .collect(Collectors.toList());

        double sma5 = TechnicalIndicatorUtil.calculateSMA(prices, 5);
        double sma10 = TechnicalIndicatorUtil.calculateSMA(prices, 10);
        double ema5 = TechnicalIndicatorUtil.calculateEMA(prices, 5);
        double ema10 = TechnicalIndicatorUtil.calculateEMA(prices, 10);
        double rsi14 = TechnicalIndicatorUtil.calculateRSI(prices, 14);
        double latestPrice = prices.get(prices.size() - 1);

        Map<String, Double> macdResult = TechnicalIndicatorUtil.calculateMACD(prices, 12, 26, 9);
        double macd = macdResult.getOrDefault("macd", 0.0);
        double signal = macdResult.getOrDefault("signal", 0.0);

        Map<String, Double> bb = TechnicalIndicatorUtil.calculateBollingerBands(prices, 20);
        double lowerBand = bb.getOrDefault("lower", 0.0);

        String prediction;
        String reason;

        if (ema5 > ema10 && rsi14 < 70 && macd > signal && latestPrice > lowerBand) {
            prediction = "Naik";
        } else if (ema5 < ema10 && rsi14 > 30 && macd < signal && latestPrice < sma10) {
            prediction = "Turun";
        } else {
            prediction = "Tidak pasti";
        }

        reason = String.format(
                "SMA-5=%.2f | SMA-10=%.2f | EMA-5=%.2f | EMA-10=%.2f | RSI-14=%.2f | MACD=%.2f | Signal=%.2f | Bollinger Lower=%.2f",
                sma5, sma10, ema5, ema10, rsi14, macd, signal, lowerBand
        );

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sma5", sma5);
        result.put("sma10", sma10);
        result.put("ema5", ema5);
        result.put("ema10", ema10);
        result.put("rsi14", rsi14);
        result.put("macd", macd);
        result.put("macd_signal", signal);
        result.put("bollinger_lower", lowerBand);
        result.put("latest_price", latestPrice);
        result.put("reason", reason);
        result.put("prediction", prediction);

        return result;
    }
}

package com.example.bitcoin.service;

import com.example.bitcoin.model.PriceData;
import com.example.bitcoin.util.TechnicalIndicatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        double rsi14 = TechnicalIndicatorUtil.calculateRSI(prices, 14);
        double latestPrice = prices.get(prices.size() - 1);

        String prediction;
        String reason;

        if (sma5 > sma10 && rsi14 < 70) {
            prediction = "Naik";
            reason = "SMA-5 berada di atas SMA-10 (golden cross) dan RSI belum overbought (" + String.format("%.2f", rsi14) + ")";
        } else if (sma5 < sma10 && rsi14 > 30) {
            prediction = "Turun";
            reason = "SMA-5 di bawah SMA-10 (death cross) dan RSI menunjukkan penurunan (" + String.format("%.2f", rsi14) + ")";
        } else {
            prediction = "Tidak pasti";
            reason = "Indikator tidak memberikan sinyal jelas: SMA5=" + sma5 + ", SMA10=" + sma10 + ", RSI=" + rsi14;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("latest_price", latestPrice);
        result.put("prediction", prediction);
        result.put("reason", reason);

        return result;
    }
}

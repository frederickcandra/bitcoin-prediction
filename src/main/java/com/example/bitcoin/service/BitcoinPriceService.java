package com.example.bitcoin.service;

import com.example.bitcoin.model.PriceData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BitcoinPriceService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<PriceData> getHistoricalPrices() {
        String url = "https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=30&interval=daily";

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<List<Number>> prices = (List<List<Number>>) response.get("prices");

        List<PriceData> priceDataList = new ArrayList<>();
        for (List<Number> entry : prices) {
            long timestamp = entry.get(0).longValue();
            double price = entry.get(1).doubleValue();
            priceDataList.add(new PriceData(timestamp, price));
        }

        return priceDataList;
    }
}

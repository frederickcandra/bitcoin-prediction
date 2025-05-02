package com.example.bitcoin.controller;

import com.example.bitcoin.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/bitcoin")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @GetMapping("/predict")
    public ResponseEntity<Map<String, Object>> predictBitcoin() {
        Map<String, Object> result = predictionService.predict();
        return ResponseEntity.ok(result);
    }
}

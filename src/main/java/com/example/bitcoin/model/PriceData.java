package com.example.bitcoin.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceData {
    private long timestamp;
    private double price;
}
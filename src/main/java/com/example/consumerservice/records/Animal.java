package com.example.consumerservice.records;

import java.math.BigDecimal;

public record Animal(boolean hasHair, String name, BigDecimal price) {
}

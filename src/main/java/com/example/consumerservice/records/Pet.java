package com.example.consumerservice.records;

import com.example.consumerservice.utils.PetType;

public record Pet(String name, int legs, PetType type) {
}

package com.example.consumerservice.records;

import reactor.util.annotation.Nullable;

public record Person(String name, int age, @Nullable Pet pet) {
}

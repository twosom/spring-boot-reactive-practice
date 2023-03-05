package com.example.springbootreactive.entity;

import org.springframework.data.annotation.Id;

public record Item(
        @Id String id,
        String name,
        String description,
        double price
) {

    public Item(String name, double price) {
        this(null, name, null, price);
    }

    public Item(String name, String description, double price) {
        this(null, name, description, price);
    }
}

package com.example.springbootreactive.entity;

import org.springframework.data.annotation.Id;
import org.springframework.util.Assert;

public record Item(
        @Id String id,
        String name,
        double price
) {

    public Item(String name, double price) {
        this(null, name, price);
    }
}

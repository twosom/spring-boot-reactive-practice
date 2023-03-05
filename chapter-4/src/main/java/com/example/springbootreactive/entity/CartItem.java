package com.example.springbootreactive.entity;

public record CartItem(
        Item item,
        int quantity
) {

    public CartItem(Item item) {
        this(item, 1);
    }

    public CartItem increment() {
        return new CartItem(this.item, this.quantity + 1);
    }
}

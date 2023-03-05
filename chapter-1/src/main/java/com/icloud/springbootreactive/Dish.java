package com.icloud.springbootreactive;

public record Dish(
        String description,
        boolean delivered
) {

    public Dish(String description) {
        this(description, false);
    }

    public static Dish deliver(final Dish dish) {
        return new Dish(dish.description, true);
    }
}

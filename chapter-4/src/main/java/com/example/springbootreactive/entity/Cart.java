package com.example.springbootreactive.entity;

import org.springframework.data.annotation.Id;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public record Cart(
        @Id String id,
        List<CartItem> cartItems
) {
    public Cart(final String id) {
        this(id, new ArrayList<>());
    }

    public Cart addCartItem(final CartItem cartItem) {
        this.cartItems.add(cartItem);
        return new Cart(
                this.id,
                this.cartItems
        );
    }

    public Optional<CartItem> findCartItemById(final String id) {
        return this.cartItems.stream()
                .filter(hasCartId(id))
                .findAny();
    }

    private Predicate<CartItem> hasCartId(final String id) {
        return cartItem -> cartItem.item().id().equals(id);
    }

    public Mono<Cart> incrementCartItemQuantity(final CartItem cartItem) {
        this.cartItems.set(this.cartItems.indexOf(cartItem), cartItem.increment());
        return Mono.just(this);
    }
}

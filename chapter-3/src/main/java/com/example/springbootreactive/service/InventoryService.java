package com.example.springbootreactive.service;

import com.example.springbootreactive.entity.Cart;
import com.example.springbootreactive.entity.CartItem;
import com.example.springbootreactive.entity.Item;
import com.example.springbootreactive.repository.CartRepository;
import com.example.springbootreactive.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.ExampleMatcher.matchingAll;
import static org.springframework.data.domain.ExampleMatcher.matchingAny;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ItemRepository itemRepository;

    private final ReactiveMongoOperations fluentOperations;

    private final CartRepository cartRepository;

    public Mono<Cart> addToCart(String id) {
        return this.cartRepository.findById("My Cart")
                .defaultIfEmpty(new Cart("My Cart"))
                .flatMap(cart -> cart.findCartItemById(id)
                        .map(cartItem -> {
                            cart.incrementCartItemQuantity(cartItem);
                            return Mono.just(cart);
                        })
                        .orElseGet(() -> this.itemRepository.findById(id)
                                .map(CartItem::new)
                                .doOnNext(cart::addCartItem)
                                .map(cartItem -> cart)
                        )
                )
                .flatMap(this.cartRepository::save);
    }

    public Mono<Cart> addItemToCart(final String cartId, final String itemId) {
        return this.cartRepository.findById(cartId)
                .log("foundCart")
                .defaultIfEmpty(new Cart(cartId))
                .log("emptyCart")
                .flatMap(cart -> cart.findCartItemById(itemId)
                        .map(cartItem -> cart.incrementCartItemQuantity(cartItem)
                                .log("newCartItem"))
                        .orElseGet(() -> this.itemRepository.findById(itemId)
                                .log("fetchedItem")
                                .map(CartItem::new)
                                .log("cartItem")
                                .map(cart::addCartItem)
                                .log("addedCartItem"))
                )
                .log("cartWithAnotherItem")
                .flatMap(this.cartRepository::save)
                .log("savedCart");
    }


    Flux<Item> searchByExample(final String name, final String description, final boolean useAnd) {
        final Item item = new Item(name, description, 0.0);
        final ExampleMatcher matcher = (useAnd ? matchingAll() : matchingAny())
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withIgnorePaths("price");
        final Example<Item> prove = Example.of(item, matcher);
        return this.itemRepository.findAll(prove);
    }

    Flux<Item> searchByExample(final String keyword) {
        final Item item = new Item(keyword, 0.0);
        final ExampleMatcher matcher = matchingAny()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        final Example<Item> prove = Example.of(item, matcher);
        return this.itemRepository.findAll(prove);
    }

    Flux<Item> searchByFluentExample(final String name, final String description, final boolean useAnd) {
        final Item item = new Item(name, description, 0.0);

        final ExampleMatcher matcher = (useAnd ? matchingAll() : matchingAny())
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withIgnorePaths("price");

        return fluentOperations.query(Item.class)
                .matching(query(byExample(Example.of(item, matcher))))
                .all();
    }

}

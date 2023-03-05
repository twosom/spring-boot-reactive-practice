package com.example.springbootreactive.service;

import com.example.springbootreactive.entity.Cart;
import com.example.springbootreactive.entity.CartItem;
import com.example.springbootreactive.repository.CartRepository;
import com.example.springbootreactive.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AltInventoryService {

    private final ItemRepository itemRepository;

    private final CartRepository cartRepository;

    public Mono<Cart> addItemToCart(final String cartId, final String itemId) {
        final Cart myCart = this.cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .block();

        return myCart.findCartItemById(itemId)
                .map(myCart::incrementCartItemQuantity)
                .orElseGet(() -> this.itemRepository.findById(itemId)
                        .map(CartItem::new)
                        .map(myCart::addCartItem)
                )
                .flatMap(this.cartRepository::save);
    }
}

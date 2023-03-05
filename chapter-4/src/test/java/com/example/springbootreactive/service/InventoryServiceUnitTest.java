package com.example.springbootreactive.service;

import com.example.springbootreactive.entity.Cart;
import com.example.springbootreactive.entity.CartItem;
import com.example.springbootreactive.entity.Item;
import com.example.springbootreactive.repository.CartRepository;
import com.example.springbootreactive.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class InventoryServiceUnitTest {

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private CartRepository cartRepository;

    private InventoryService inventoryService;


    @BeforeEach
    void setUp() {
        final Item sampleItem = new Item("item1", "TV tray", "Alf TV tray", 19.99);
        final CartItem sampleCartItem = new CartItem(sampleItem);
        final Cart sampleCart = new Cart("My Cart", Collections.singletonList(sampleCartItem));

        when(cartRepository.findById(anyString())).thenReturn(Mono.empty());
        when(itemRepository.findById(anyString())).thenReturn(Mono.just(sampleItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(sampleCart));

        inventoryService = new InventoryService(itemRepository, cartRepository);
    }

    @Test
    void addItemToEmptyCartShouldProduceOneCartItem() {
        this.inventoryService.addItemToCart("My Cart", "item1")
                .as(StepVerifier::create)
                .expectNextMatches(cart -> {
                    assertThat(cart.cartItems()).extracting(CartItem::quantity)
                            .containsExactlyInAnyOrder(1);

                    assertThat(cart.cartItems()).extracting(CartItem::item)
                            .containsExactly(new Item("item1", "TV tray", "Alf TV tray", 19.99));
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void alternativeWayToTest() {
        StepVerifier.create(inventoryService.addItemToCart("My Cart", "item1"))
                .expectNextMatches(cart -> {
                    assertThat(cart.cartItems()).extracting(CartItem::quantity)
                            .containsExactlyInAnyOrder(1);

                    assertThat(cart.cartItems()).extracting(CartItem::item)
                            .containsExactly(new Item("item1", "TV tray", "Alf TV tray", 19.99));
                    return true;
                })
                .verifyComplete();
    }
}
package com.example.springbootreactive;

import com.example.springbootreactive.entity.Cart;
import com.example.springbootreactive.entity.CartItem;
import com.example.springbootreactive.entity.Item;
import com.example.springbootreactive.repository.CartRepository;
import com.example.springbootreactive.repository.ItemRepository;
import com.example.springbootreactive.service.AltInventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class BlockHoundIntegrationTest {

    AltInventoryService inventoryService;

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        final Item sampleItem = new Item("item1", "TV tray", "Alf TV tray", 19.99);
        final CartItem sampleCartItem = new CartItem(sampleItem);
        final Cart sampleCart = new Cart("My Cart", Collections.singletonList(sampleCartItem));

        when(cartRepository.findById(anyString())).thenReturn(Mono.<Cart>empty().hide());
        when(itemRepository.findById(anyString())).thenReturn(Mono.just(sampleItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(sampleCart));
        inventoryService = new AltInventoryService(itemRepository, cartRepository);
    }

    @Test
    void blockHoundShouldTrapBlockingCall() {
        Mono.delay(Duration.ofSeconds(1))
                .flatMap(tick -> inventoryService.addItemToCart("My Cart", "item1"))
                .as(StepVerifier::create)
                .verifyErrorSatisfies(throwable -> {
                    assertThat(throwable).hasMessageContaining(
                            "block()/blockFirst()/blockLast() are blocking"
                    );
                });
    }



}

package com.example.springbootreactive.service;

import com.example.springbootreactive.entity.Cart;
import com.example.springbootreactive.repository.CartRepository;
import com.example.springbootreactive.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.CorePublisher;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final InventoryService inventoryService;

    public Map<String, CorePublisher<? extends Record>> getHomeAttribute() {
        return Map.of(
                "items", this.itemRepository.findAll()
                        .doOnNext(System.out::println),
                "cart", this.cartRepository.findById("My Cart")
                        .defaultIfEmpty(new Cart("My Cart"))
        );
    }

    public Map<String, CorePublisher<? extends Record>> search(String name, String description, boolean useAnd) {
        return Map.of(
                "items", this.inventoryService.searchByExample(name, description, useAnd),
                "cart", this.cartRepository.findById("My Cart")
                        .defaultIfEmpty(new Cart("My Cart"))
        );
    }
}

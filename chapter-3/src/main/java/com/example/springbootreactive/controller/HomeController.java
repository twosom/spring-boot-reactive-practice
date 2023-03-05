package com.example.springbootreactive.controller;

import com.example.springbootreactive.service.HomeService;
import com.example.springbootreactive.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final InventoryService inventoryService;
    private final HomeService homeService;

    @GetMapping("/")
    Mono<Rendering> home() {
        final var modelAttributes = homeService.getHomeAttribute();
        return Mono.just(Rendering.view("home")
                .model(modelAttributes)
                .build());
    }

    @PostMapping("/add/{id}")
    Mono<String> addToCart(@PathVariable String id) {
        return inventoryService.addItemToCart("My Cart", id)
                .thenReturn("redirect:/");
    }

    @GetMapping("/search")
    Mono<Rendering> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam boolean useAnd) {
        final var attributes = homeService.search(name, description, useAnd);
        return Mono.just(Rendering.view("home")
                .model(attributes)
                .build());
    }
}

package com.example.springbootreactive;

import com.example.springbootreactive.entity.Item;
import com.example.springbootreactive.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
public class MongoDbSliceTest {

    @Autowired
    ItemRepository repository;

    @Test
    void itemRepositorySaveItems() {
        final Item sampleItem = new Item("name", "description", 1.99);
        repository.save(sampleItem)
                .as(StepVerifier::create)
                .expectNextMatches(item -> {
                    assertNotNull(item.id());
                    assertEquals(item.name(), "name");
                    assertEquals(item.description(), "description");
                    assertEquals(item.price(), 1.99);
                    return true;
                })
                .verifyComplete();
    }

}

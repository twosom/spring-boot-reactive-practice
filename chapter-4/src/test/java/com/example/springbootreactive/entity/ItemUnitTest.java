package com.example.springbootreactive.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemUnitTest {

    @Test
    void itemBasicsShouldWork() {
        final Item sampleItem = new Item("item1", "TV tray", "Alf TV tray", 19.99);
        assertEquals(sampleItem.id(), "item1");
        assertEquals(sampleItem.name(), "TV tray");
        assertEquals(sampleItem.description(), "Alf TV tray");
        assertEquals(sampleItem.price(), 19.99);
        assertEquals(sampleItem.toString(), "Item[id=item1, name=TV tray, description=Alf TV tray, price=19.99]");
        final Item sampleItem2 = new Item("item1", "TV tray", "Alf TV tray", 19.99);
        assertEquals(sampleItem, sampleItem2);
    }

}
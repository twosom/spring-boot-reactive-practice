package com.example.springbootreactive;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class SimpleExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        List<Integer> source;
        if (new Random().nextBoolean()) {
            source = IntStream.range(1, 11).boxed()
                    .toList();
        } else {
            source = List.of(1, 2, 3, 4);
        }

        try {
            executor.submit(() -> source.get(5)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

}

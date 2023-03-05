package com.example.springbootreactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Chapter4Application {

    public static void main(String[] args) {
//        BlockHound.builder()
//                .allowBlockingCallsInside(
//                        TemplateEngine.class.getCanonicalName(), "process"
//                )
//                .install();
        SpringApplication.run(Chapter4Application.class, args);
    }

}

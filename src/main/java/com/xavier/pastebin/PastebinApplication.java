package com.xavier.pastebin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class PastebinApplication {

    public static void main(String[] args) {
        SpringApplication.run(PastebinApplication.class, args);
    }

}

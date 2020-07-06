package com.dchristofolli.projects.webfluxessentials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class WebfluxEssentialsApplication {
    static {
        BlockHound.install(
                builder -> builder.allowBlockingCallsInside("java.util.UUID", "randomUUID")
        );
    }

    public static void main(String[] args) {
        SpringApplication.run(WebfluxEssentialsApplication.class, args);
    }

}

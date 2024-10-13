package com.savolodya.predictiveportfolio.configuration.database;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@AllArgsConstructor
public class DatabaseInitializer {
    private final RolesCreator rolesCreator;

    @PostConstruct
    public void init() {
        log.info("Initializing database...");
        rolesCreator.create();
        log.info("Database initialized");
    }
}

package com.example.backend.config;

import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchDbConfig {

    @Value("${couchdb.url}")
    private String url;

    @Value("${couchdb.username}")
    private String username;

    @Value("${couchdb.password}")
    private String password;

    @Value("${couchdb.db}")
    private String dbName;

    @Bean
    public CouchDbClient couchDbClient() {
        return new CouchDbClient(
                dbName,
                true,
                "http",
                "localhost",
                5984,
                username,
                password
        );
    }
}


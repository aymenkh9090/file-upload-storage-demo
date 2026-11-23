package com.aymen.doc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
        Storage storage
) {
    public record Storage(String location) {}
}

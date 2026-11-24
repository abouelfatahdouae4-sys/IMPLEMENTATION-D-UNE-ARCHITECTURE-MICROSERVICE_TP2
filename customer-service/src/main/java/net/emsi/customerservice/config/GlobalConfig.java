package net.emsi.customerservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Lit global.params.p1 / p2 depuis config-repo/application.properties
@ConfigurationProperties(prefix = "global.params")
public record GlobalConfig(int p1, int p2) {
}

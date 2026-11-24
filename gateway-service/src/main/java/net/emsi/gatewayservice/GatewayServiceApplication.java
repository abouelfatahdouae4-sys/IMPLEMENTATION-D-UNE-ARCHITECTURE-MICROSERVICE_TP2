package net.emsi.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    /**
     * 6. Configuration DYNAMIQUE des routes :
     * chaque service enregistre dans Eureka devient accessible via
     * http://localhost:8888/{nom-du-service}/...  ex: /customer-service/api/customers
     * Desactivee quand on lance le profil "static".
     */
    @Bean
    @Profile("!static")
    DiscoveryClientRouteDefinitionLocator dynamicRoutes(ReactiveDiscoveryClient rdc,
                                                        DiscoveryLocatorProperties dlp) {
        dlp.setLowerCaseServiceId(true);
        return new DiscoveryClientRouteDefinitionLocator(rdc, dlp);
    }
}

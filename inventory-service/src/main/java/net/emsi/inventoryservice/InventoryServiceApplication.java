package net.emsi.inventoryservice;

import net.emsi.inventoryservice.entities.Product;
import net.emsi.inventoryservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.UUID;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
                config.exposeIdsFor(Product.class);
            }
        };
    }

    @Bean
    CommandLineRunner start(ProductRepository productRepository) {
        return args -> {
            productRepository.save(Product.builder().id(UUID.randomUUID().toString())
                    .name("Computer").price(3200).quantity(11).build());
            productRepository.save(Product.builder().id(UUID.randomUUID().toString())
                    .name("Printer").price(1299).quantity(10).build());
            productRepository.save(Product.builder().id(UUID.randomUUID().toString())
                    .name("Smartphone").price(5400).quantity(4).build());
            productRepository.findAll().forEach(System.out::println);
        };
    }
}

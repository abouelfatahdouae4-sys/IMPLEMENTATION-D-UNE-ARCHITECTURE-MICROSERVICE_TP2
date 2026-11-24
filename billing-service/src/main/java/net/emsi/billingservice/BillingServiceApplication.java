package net.emsi.billingservice;

import net.emsi.billingservice.entities.Bill;
import net.emsi.billingservice.entities.ProductItem;
import net.emsi.billingservice.feign.CustomerRestClient;
import net.emsi.billingservice.feign.ProductRestClient;
import net.emsi.billingservice.model.Customer;
import net.emsi.billingservice.model.Product;
import net.emsi.billingservice.repository.BillRepository;
import net.emsi.billingservice.repository.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
                config.exposeIdsFor(Bill.class, ProductItem.class);
            }
        };
    }

    /**
     * Genere des factures de test a partir des clients et produits
     * recuperes via OpenFeign. customer-service et inventory-service
     * doivent etre demarres AVANT billing-service.
     */
    @Bean
    CommandLineRunner start(BillRepository billRepository,
                            ProductItemRepository productItemRepository,
                            CustomerRestClient customerRestClient,
                            ProductRestClient productRestClient) {
        return args -> {
            try {
                Collection<Customer> customers = customerRestClient.getAllCustomers().getContent();
                Collection<Product> products = productRestClient.getAllProducts().getContent();
                Random random = new Random();
                customers.forEach(customer -> {
                    Bill bill = Bill.builder()
                            .billingDate(new Date())
                            .customerId(customer.getId())
                            .build();
                    billRepository.save(bill);
                    products.forEach(product -> {
                        ProductItem item = ProductItem.builder()
                                .bill(bill)
                                .productId(product.getId())
                                .quantity(1 + random.nextInt(10))
                                .unitPrice(product.getPrice())
                                .build();
                        productItemRepository.save(item);
                    });
                });
                System.out.println(">>> " + billRepository.count() + " factures generees");
            } catch (Exception e) {
                System.err.println(">>> Impossible de generer les factures : demarrez d'abord "
                        + "customer-service et inventory-service. (" + e.getMessage() + ")");
            }
        };
    }
}

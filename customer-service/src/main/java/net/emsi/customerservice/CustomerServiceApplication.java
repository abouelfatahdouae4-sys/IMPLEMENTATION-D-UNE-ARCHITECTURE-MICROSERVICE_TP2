package net.emsi.customerservice;

import net.emsi.customerservice.config.CustomerConfigParams;
import net.emsi.customerservice.config.GlobalConfig;
import net.emsi.customerservice.entities.Customer;
import net.emsi.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties({GlobalConfig.class, CustomerConfigParams.class})
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    // Spring Data REST cache les ids par defaut : on les expose
    @Bean
    RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
                config.exposeIdsFor(Customer.class);
            }
        };
    }

    @Bean
    CommandLineRunner start(CustomerRepository customerRepository) {
        return args -> {
            customerRepository.saveAll(List.of(
                    Customer.builder().name("Mohamed").email("med@gmail.com").build(),
                    Customer.builder().name("Imane").email("imane@gmail.com").build(),
                    Customer.builder().name("Yassine").email("yassine@gmail.com").build()
            ));
            customerRepository.findAll().forEach(System.out::println);
        };
    }
}

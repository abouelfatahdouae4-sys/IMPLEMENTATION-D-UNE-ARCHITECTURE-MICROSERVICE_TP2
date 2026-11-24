package net.emsi.billingservice.feign;

import net.emsi.billingservice.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// "customer-service" = nom enregistre dans Eureka (pas d'URL en dur)
@FeignClient(name = "customer-service")
public interface CustomerRestClient {
    @GetMapping("/api/customers/{id}")
    Customer getCustomerById(@PathVariable("id") Long id);

    @GetMapping("/api/customers")
    PagedModel<Customer> getAllCustomers();
}

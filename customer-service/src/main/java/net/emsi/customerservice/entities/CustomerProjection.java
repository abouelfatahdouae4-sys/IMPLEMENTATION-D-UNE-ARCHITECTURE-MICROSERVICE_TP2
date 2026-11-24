package net.emsi.customerservice.entities;

import org.springframework.data.rest.core.config.Projection;

// GET /api/customers?projection=fullCustomer
@Projection(name = "fullCustomer", types = Customer.class)
public interface CustomerProjection {
    Long getId();
    String getName();
    String getEmail();
}

package net.emsi.billingservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Modele (pas une entite JPA) : les donnees viennent de customer-service
@Getter @Setter @ToString
public class Customer {
    private Long id;
    private String name;
    private String email;
}

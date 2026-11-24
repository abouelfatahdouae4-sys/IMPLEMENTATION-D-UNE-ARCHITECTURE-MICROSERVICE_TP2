package net.emsi.billingservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Modele (pas une entite JPA) : les donnees viennent de inventory-service
@Getter @Setter @ToString
public class Product {
    private String id;
    private String name;
    private double price;
    private int quantity;
}

package net.emsi.billingservice.entities;

import net.emsi.billingservice.model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
public class ProductItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productId;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // evite la boucle infinie JSON
    private Bill bill;

    private int quantity;
    private double unitPrice;

    @Transient
    private Product product;     // rempli via OpenFeign

    public double getAmount() {
        return unitPrice * quantity;
    }
}

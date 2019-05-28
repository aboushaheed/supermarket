package com.supermarket.shop.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"offer", "basketLines"})
@Builder(toBuilder = true)
@NoArgsConstructor

@Entity
public class Product {

    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    private String name;
    private Integer stock;
    private BigDecimal initialPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_offer_id")
    private Offer offer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<BasketLine> basketLines;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) &&
                Objects.equals(initialPrice, product.initialPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, initialPrice);
    }
}

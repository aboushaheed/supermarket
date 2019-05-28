package com.supermarket.shop.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@ToString(exclude = {"products"})
@Entity
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Long offerId;
    private String name;
    private Integer quantityToBuy;
    private Integer quantityForFree;
    private boolean active;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "offer")
    private List<Product> products;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return name.equals(offer.name) &&
                quantityToBuy.equals(offer.quantityToBuy) &&
                quantityForFree.equals(offer.quantityForFree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantityToBuy, quantityForFree);
    }


}

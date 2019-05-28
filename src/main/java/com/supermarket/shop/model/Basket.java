package com.supermarket.shop.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "basketLines")
@Builder(toBuilder = true)
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_id")
    private Long basketId;
    private BigDecimal finalPrice;
    private Boolean checkout;
    @OneToMany(mappedBy = "basket")
    private List<BasketLine> basketLines;


}

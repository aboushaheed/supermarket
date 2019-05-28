package com.supermarket.shop.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"basket","product"})
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "basketline")
public class BasketLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_line_id")
    private Long basketLineId;

    private Integer quantity;
    private BigDecimal linePrice;
    private BigDecimal discountedPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_basket_id")
    private Basket basket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_product_id")
    private Product product;



}

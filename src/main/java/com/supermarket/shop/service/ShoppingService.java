package com.supermarket.shop.service;

import com.supermarket.shop.exception.BadParameterException;
import com.supermarket.shop.exception.NoMoreItemsInStockException;
import com.supermarket.shop.model.Basket;
import com.supermarket.shop.model.BasketLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Objects.*;

@Service
public class ShoppingService {

    private DiscountService discountService;
    private BasketService basketService;
    private ProductService productService;

    @Autowired
    public ShoppingService(DiscountService discountService,
                           ProductService productService, BasketService basketService) {
        this.discountService = requireNonNull(discountService);
        this.basketService = requireNonNull(basketService);
        this.productService = requireNonNull(productService);
    }

    public Basket addLine(BasketLine basketLine, Basket actualBasket) {

        if (nonNull(basketLine)) {
            basketLine = decreaseStock(basketLine);
            if (basketLine.getQuantity() <= basketLine.getProduct().getStock()) {
                if (nonNull(actualBasket)) {

                    return getBasketLinePrice(basketLine, actualBasket);
                } else {
                    return getBasketLinePrice(basketLine,
                            Basket.builder()
                                    .checkout(false)
                                    .finalPrice(BigDecimal.ZERO)
                                    .build());
                }
            } else {
                throw new NoMoreItemsInStockException(basketLine.getProduct().getStock().toString(),
                        basketLine.getProduct().getName() + "s");
            }
        } else {
            throw new BadParameterException("Basket line must be not null");
        }
    }

    private Basket getBasketLinePrice(BasketLine basketLine, Basket actualBasket) {


        if (isNull(actualBasket.getBasketLines()) || actualBasket.getBasketLines().isEmpty()) {

            actualBasket.setBasketLines(Collections.singletonList(basketLine));

            return discountService.getDiscount(basketLine, actualBasket);

        } else {
            List<BasketLine> basketLines = new ArrayList<>(actualBasket.getBasketLines());
            basketLines.stream()
                    .filter(line -> line.getProduct().getName().equals(basketLine.getProduct().getName()))
                    .peek(same -> {
                        same.setQuantity(basketLine.getQuantity() + same.getQuantity());
                        same.setDiscountedPrice(BigDecimal.ZERO);
                    });
            actualBasket.setBasketLines(Collections.emptyList());
            basketLines.add(basketLine);
            actualBasket.setBasketLines(basketLines);

            return discountService.getDiscount(basketLine, actualBasket);
        }

    }

    private BasketLine decreaseStock(BasketLine basketLine) {
        int stock = basketLine.getProduct().getStock() - basketLine.getQuantity();
        if (stock < 0) {
            throw new NoMoreItemsInStockException(basketLine.getProduct().getName(), basketLine.getProduct().getStock().toString());
        } else {

            productService.save(basketLine.getProduct().toBuilder().stock(stock).build());

        }
        return basketLine;
    }

    public Basket checkout(Basket actualBasket) {

        return basketService.save(actualBasket.toBuilder()
                .checkout(true)
                .build());
    }


}

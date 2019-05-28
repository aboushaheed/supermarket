package com.supermarket.shop.service;

import com.supermarket.shop.exception.BadParameterException;
import com.supermarket.shop.model.Basket;
import com.supermarket.shop.model.BasketLine;
import com.supermarket.shop.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

@Service
public class DiscountService {

    private BasketLineService basketLineService;
    private BasketService basketService;

    @Autowired
    public DiscountService(BasketLineService basketLineService, BasketService basketService) {
        this.basketLineService = requireNonNull(basketLineService);
        this.basketService = requireNonNull(basketService);
    }

    public Basket getDiscount(BasketLine basketLine, Basket actualBasket) {

        if (nonNull(basketLine)) {

            Integer quantity = basketLine.getQuantity();
            basketLine.setLinePrice(basketLine.getProduct()
                    .getInitialPrice()
                    .multiply(BigDecimal.valueOf(quantity))
                    .setScale(2, BigDecimal.ROUND_HALF_UP));

            final Offer offer = basketLine.getProduct().getOffer();

            if (nonNull(offer) && offer.isActive()) {

                BigDecimal discountedPrice = basketLine.getProduct().getInitialPrice()
                        .multiply(BigDecimal.valueOf(getNumberOfItemsToPay(quantity, offer) * offer.getQuantityForFree()));

                basketLine.setDiscountedPrice(discountedPrice);
                actualBasket.setFinalPrice(actualBasket.getFinalPrice().add(discountedPrice));
            } else {
                basketLine.setDiscountedPrice(BigDecimal.ZERO);
            }

            basketLine.setLinePrice(basketLine.getProduct()
                    .getInitialPrice()
                    .multiply(BigDecimal.valueOf(quantity)));

            basketLineService.save(basketLine);

            return basketService.save(actualBasket.toBuilder()
                    .finalPrice(actualBasket.getBasketLines()
                            .stream()
                            .map(DiscountService::priceToMap)
                            .reduce(BigDecimal::add)
                            .get())
                    .build());
        } else {
            throw new BadParameterException("basket line must be not null");
        }

    }

    private static BigDecimal priceToMap(BasketLine basketLine) {
        return basketLine.getDiscountedPrice().doubleValue() == 0.0
                ? basketLine.getLinePrice() : basketLine.getDiscountedPrice();
    }


    public Integer getNumberOfItemsToPay(Integer quantity, Offer offer) {

        Integer restItems = quantity % (offer.getQuantityForFree() + offer.getQuantityToBuy());

        return ((((quantity - restItems) * offer.getQuantityToBuy()) /
                (offer.getQuantityForFree() + offer.getQuantityToBuy())) + restItems);
    }

}

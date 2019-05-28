package com.supermarket.shop.service;

import com.supermarket.shop.SupermarketApplication;
import com.supermarket.shop.model.Basket;
import com.supermarket.shop.model.BasketLine;
import com.supermarket.shop.model.Offer;
import com.supermarket.shop.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SupermarketApplication.class} , properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Testing Discount Service")
public class DiscountServiceTest {

    @Autowired
    private BasketLineService basketLineService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private BasketService basketService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShoppingService shoppingService;



    @Test
    @DisplayName("Three For The Price Of Two")
    public void test_three_for_the_price_of_two() {

        // Given
        final Integer quantity = 10;

        Offer offer = offerService.save(Offer.builder()
                .name("Three For The Price Of Two")
                .quantityForFree(1)
                .quantityToBuy(2)
                .build());

        final Integer expectedResult = 7;

        // When
        final Integer result = discountService.getNumberOfItemsToPay(quantity, offer);

        // Then
        assertEquals(expectedResult, result);

    }
    @Test
    @DisplayName("Buy One Get One Free")
    public void test_buy_one_get_one_free() {

        // Given
        final Integer quantity = 15;

        Offer offer = offerService.save(Offer.builder()
                .name("TBuy One Get One Free")
                .quantityForFree(1)
                .quantityToBuy(1)
                .build());

        final Integer expectedResult = 8;

        // When
        final Integer result = discountService.getNumberOfItemsToPay(quantity, offer);

        // Then
        assertEquals(expectedResult, result);

    }

    @Test
    @DisplayName("Calculate final price with discount")
    public void test_calculate_final_price_with_discount() {
        // Given
        Offer offer = offerService.save(Offer.builder()
                .name("Three For The Price Of Two")
                .active(true)
                .quantityForFree(1)
                .quantityToBuy(2)
                .build());

        Product watermelon = productService.save(Product.builder()
                .offer(offer)
                .stock(50)
                .name("Watermelon")
                .initialPrice(BigDecimal.valueOf(0.80))
                .build());


        BasketLine basketLine = basketLineService.save(BasketLine.builder()
                .product(watermelon)
                .quantity(10)
                .build());
        // When
        Basket result = basketService.save(shoppingService.addLine(basketLine, null));
        Basket basketToCheck = Basket.builder()
                .finalPrice(BigDecimal.valueOf(5.60))
                .build();

        // Then
        assertEquals(result.getFinalPrice(), basketToCheck.getFinalPrice());
    }

}

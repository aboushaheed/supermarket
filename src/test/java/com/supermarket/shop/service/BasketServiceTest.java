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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SupermarketApplication.class} , properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Testing Basket Service")
public class BasketServiceTest {


    @Autowired
    private BasketService basketService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BasketLineService basketLineService;

    @Autowired
    private ShoppingService shoppingService;

    @Test
    @DisplayName("get the current basket")
    public void testLast() {
        // Given
        final Optional<Basket> expectedResult = Optional.ofNullable(Basket.builder()
                .basketId(1L)
                .build());
        Offer offer = offerService.save(Offer.builder()
                .name("Buy One Get One Free")
                .active(true)
                .quantityForFree(1)
                .quantityToBuy(1)
                .build());

        Product apple = productService.save(Product.builder()
                .offer(offer)
                .stock(30)
                .name("Apple")
                .initialPrice(BigDecimal.valueOf(0.20))
                .build());


        BasketLine basketLine = basketLineService.save(BasketLine.builder()
                .product(apple)
                .quantity(2)
                .build());

        basketService.save(shoppingService.addLine(basketLine, null));
        // When
        final Optional<Basket> result = basketService.last();

        // Then 
        assertEquals(expectedResult.get().getBasketId(), result.get().getBasketId());
    }

    @Test
    @DisplayName("save basket")
    public void testSave() {

        // Given
        Offer offer = offerService.save(Offer.builder()
                .name("Buy One Get One Free")
                .active(true)
                .quantityForFree(1)
                .quantityToBuy(1)
                .build());

        Product apple = productService.save(Product.builder()
                .offer(offer)
                .stock(30)
                .name("Apple")
                .initialPrice(BigDecimal.valueOf(0.20))
                .build());


        BasketLine basketLine = basketLineService.save(BasketLine.builder()
                .product(apple)
                .quantity(2)
                .build());

        // When
        Basket result = basketService.save(shoppingService.addLine(basketLine, null));

        // Then
        assertNotNull(result.getBasketId());
    }
}

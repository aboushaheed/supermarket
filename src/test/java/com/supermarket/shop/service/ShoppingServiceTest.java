package com.supermarket.shop.service;

import com.supermarket.shop.SupermarketApplication;
import com.supermarket.shop.exception.BadParameterException;
import com.supermarket.shop.exception.NoMoreItemsInStockException;
import com.supermarket.shop.model.Basket;
import com.supermarket.shop.model.BasketLine;
import com.supermarket.shop.model.Offer;
import com.supermarket.shop.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SupermarketApplication.class} , properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Testing Shopping Service")
public class ShoppingServiceTest {

    @Autowired
    private ShoppingService shoppingService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private BasketService basketService;

    @Autowired
    private BasketLineService basketLineService;


    @BeforeEach
    public void setUp() {

        assertNotNull(shoppingService);
        assertNotNull(productService);
        assertNotNull(offerService);
        assertNotNull(basketService);
        assertNotNull(basketLineService);
    }



    @Test
    @DisplayName("test basket containing the following items: Apple 4, Orange 3, Watermelon 5")
    public void test_apple_4_orange_3_watermelon_5() {

        // Given
        Offer buyOneGetOneFree = offerService.save(Offer.builder()
                .name("Buy One Get One Free")
                .active(true)
                .quantityForFree(1)
                .quantityToBuy(1)
                .build());

        Offer threeForThePriceOfTwo = offerService.save(Offer.builder()
                .name("Three For The Price Of Two")
                .active(true)
                .quantityToBuy(2)
                .quantityForFree(1)
                .build());

        Product apple = productService.save(Product.builder()
                .offer(buyOneGetOneFree)
                .stock(5)
                .name("Apple")
                .initialPrice(BigDecimal.valueOf(0.20))
                .build());

        Product watermelon = productService.save(Product.builder()
                .offer(threeForThePriceOfTwo)
                .stock(24)
                .name("Watermelon")
                .initialPrice(BigDecimal.valueOf(0.80))
                .build());

        Product orange = productService.save(Product.builder()
                .stock(14)
                .name("Orange")
                .initialPrice(BigDecimal.valueOf(0.50))
                .build());



        BasketLine appleBasketLine = BasketLine.builder()
                .product(apple)
                .quantity(4)
                .build();

        BasketLine orangeBasketLine =BasketLine.builder()
                .product(orange)
                .quantity(3)
                .build();

        BasketLine watermelonBasketLine = BasketLine.builder()
                .product(watermelon)
                .quantity(5)
                .build();

        List<BasketLine> basketLines = Stream
                .of(appleBasketLine,orangeBasketLine,watermelonBasketLine)
                .collect(Collectors.toList());


        Basket basket = new Basket();

        for ( BasketLine line : basketLines ) {

            Optional<Basket> optionalBasket = basketService.last();
            line  = basketLineService.save(line);
            basket = shoppingService.addLine(line, optionalBasket.isPresent() ? optionalBasket.get() : null);
            line.setBasket(basketService.save(basket));
            basketLineService.save(line);
        }

        // Then
        assertEquals(basket.getFinalPrice().doubleValue(), BigDecimal.valueOf(5.10).doubleValue());
    }


    @Test
    @DisplayName("Add a product to an empty basket")
    public void test_add_to_empty_basket() {

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
        Basket basketToCheck = Basket.builder()
                .finalPrice(BigDecimal.valueOf(0.20))
                .build();

        // Then
       assertEquals(result.getFinalPrice().doubleValue(), basketToCheck.getFinalPrice().doubleValue());
    }

    @Test
    @DisplayName("Add a product to a not empty basket")
    public void test_add_to_not_empty_basket() {
        // Given
        Offer buyTwoGetOneFree = offerService.save(Offer.builder()
                .name("Buy Two Get One Free")
                .active(true)
                .quantityForFree(1)
                .quantityToBuy(2)
                .build());

        Product apple = productService.save(Product.builder()
                .offer(buyTwoGetOneFree)
                .stock(5)
                .name("Apple")
                .initialPrice(BigDecimal.valueOf(0.20))
                .build());


        BasketLine appleBasketLine = basketLineService.save(BasketLine.builder()
                .product(apple)
                .quantity(2)
                .build());
        Basket basketHasApple = basketService.save(shoppingService.addLine(appleBasketLine, null));

        Offer threeForThePriceOfTwo = offerService.save(Offer.builder()
                .name("Three For The Price Of Two")
                .active(true)
                .quantityToBuy(2)
                .quantityForFree(1)
                .build());

        Product watermelon = productService.save(Product.builder()
                .offer(threeForThePriceOfTwo)
                .stock(15)
                .name("Watermelon")
                .initialPrice(BigDecimal.valueOf(0.80))
                .build());


        BasketLine watermelonBasketLine = basketLineService.save(BasketLine.builder()
                .product(watermelon)
                .quantity(3)
                .build());
        // When

         Basket basketWatermelon = shoppingService.addLine(watermelonBasketLine, basketHasApple);

         Basket result = basketService.save(basketWatermelon);

        Basket basketToCheck = Basket.builder()
                .finalPrice(BigDecimal.valueOf(1.6))
                .build();



        // Then
        assertEquals(result.getFinalPrice().doubleValue(), basketToCheck.getFinalPrice().doubleValue());
    }



    @Test
    @DisplayName("bad parameter exception with null basket line")
    public void test_add_null_basket_line_exception() {
        assertThrows(BadParameterException.class, () -> basketService.save(shoppingService.addLine(null, null)));
    }


    @Test
    @DisplayName("no more apples in the stock exception")
    public void test_add_no_more_items_exception() {
        // Given
        Offer offer = offerService.save(Offer.builder()
                .name("Buy Two Get One Free")
                .active(true)
                .quantityForFree(1)
                .quantityToBuy(2)
                .build());

        Product product = productService.save(Product.builder()
                .offer(offer)
                .stock(5)
                .name("Apple")
                .initialPrice(BigDecimal.valueOf(0.20))
                .build());

        BasketLine basketLine = BasketLine.builder()
                .product(product)
                .quantity(100)
                .build();

        // Then
        assertThrows(NoMoreItemsInStockException.class, () -> shoppingService.addLine(basketLine, null));
    }


    @Test
    @DisplayName("Checkout a given basket")
    public void test_Checkout() {
        // Given
        Offer offer = offerService.save(Offer.builder()
                .name("Buy One Get One Free")
                .active(true)
                .quantityForFree(1)
                .quantityToBuy(1)
                .build());

        Product apple = productService.save(Product.builder()
                .offer(offer)
                .stock(5)
                .name("Apple")
                .initialPrice(BigDecimal.valueOf(0.20))
                .build());


        BasketLine basketLine = basketLineService.save(BasketLine.builder()
                .product(apple)
                .quantity(2)
                .build());
        // When
        Basket result = basketService.save(shoppingService.addLine(basketLine, null));

        final Basket checkout = shoppingService.checkout(result);
        //then
        assertTrue(checkout.getCheckout());

    }

}

package com.supermarket.shop.service;

import com.supermarket.shop.SupermarketApplication;
import com.supermarket.shop.model.Offer;
import com.supermarket.shop.model.Product;
import com.supermarket.shop.repository.OfferRepository;
import com.supermarket.shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SupermarketApplication.class} , properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Testing Product Service")
public class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private OfferService offerService;


    @Test
    @DisplayName("test update stock for a given product")
    public void testUpdateStock() {
        // Given
        final String name = "Apple";
        final Integer quantity = 15;
        final Product expectedResult = Product.builder()
                .stock(15)
                .build();
        productService.save(Product.builder()
                .stock(0)
                .name("Apple")
                .initialPrice(BigDecimal.valueOf(0.20))
                .build());

        // When
        final Product result = productService.updateStock(name, quantity);

        // Then 
        assertEquals(expectedResult.getStock(), result.getStock());
    }

    @Test
    @DisplayName("test update a product binding an offer")
    public void testUpdateProductOffer() {
        // Given

        Offer buyOneGetOneFree = offerService.save(Offer.builder()
                .name("Buy One Get One Free")
                .active(true)
                .quantityForFree(1)
                .quantityToBuy(1)
                .build());

        productService.save(Product.builder()
                .stock(5)
                .name("Apple")
                .initialPrice(BigDecimal.valueOf(0.20).setScale(2))
                .build());

        final String productName = "Apple";
        final String offerName = "Buy One Get One Free";
        final Product expectedResult = Product.builder()
                .productId(1L)
                .stock(5)
                .offer(buyOneGetOneFree)
                .name("Apple")
                .initialPrice(BigDecimal.valueOf(0.20).setScale(2))
                .build();

        // When
        final Product result = productService.updateProductOffer(productName, offerName);

        // Then 
        assertEquals(expectedResult.getOffer().getOfferId(), result.getOffer().getOfferId());
    }

    @Test
    @DisplayName("test update the price of a given product")
    public void testUpdatePrice() {
        // Given
        final String name = "apple";
        final BigDecimal newPrice = new BigDecimal("0.70");
        final Product expectedResult = productService.save(Product.builder()
                .stock(5)
                .name("Apple")
                .initialPrice(BigDecimal.valueOf(0.70).setScale(2))
                .build());


        // When
        final Product result = productService.updatePrice(name, newPrice);

        // Then 
        assertEquals(expectedResult.getInitialPrice().doubleValue(), result.getInitialPrice().doubleValue());
    }


    @Test
    @DisplayName("test save a given product")
    public void testSave() {
        // Given
        final Product product = Product.builder()
                .stock(5)
                .name("Apple")
                .initialPrice(BigDecimal.valueOf(0.70).setScale(2))
                .build();
        final Product expectedResult = Product.builder()
                .productId(1L)
                .stock(5)
                .name("Apple")
                .initialPrice(BigDecimal.valueOf(0.70).setScale(2))
                .build();

        // When
        final Product result = productService.save(product);

        // Then 
        assertEquals(expectedResult, result);
    }
}

package com.supermarket.shop.service;

import com.supermarket.shop.SupermarketApplication;
import com.supermarket.shop.model.BasketLine;
import com.supermarket.shop.model.Offer;
import com.supermarket.shop.model.Product;
import com.supermarket.shop.repository.BasketLineRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SupermarketApplication.class} , properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Testing Basket line Service")
public class BasketLineServiceTest {

    @Autowired
    private BasketLineService basketLineService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("test save basket line")
    public void testSave() {
        // Given
        Offer buyOneGetOneFree = offerService.save(Offer.builder()
                .name("Buy One Get One Free")
                .active(true)
                .quantityForFree(1)
                .quantityToBuy(1)
                .build());

        Product apple = productService.save(Product.builder()
                .offer(buyOneGetOneFree)
                .stock(5)
                .name("Apple")
                .initialPrice(BigDecimal.valueOf(0.20))
                .build());
        BasketLine appleBasketLine = BasketLine.builder()
                .product(apple)
                .quantity(4)
                .build();
        // When
        final BasketLine result = basketLineService.save(appleBasketLine);

        // Then 
       assertNotNull(result.getBasketLineId());
    }

}

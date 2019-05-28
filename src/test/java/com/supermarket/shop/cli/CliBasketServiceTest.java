package com.supermarket.shop.cli;

import com.supermarket.shop.SupermarketApplication;
import com.supermarket.shop.model.Basket;
import com.supermarket.shop.service.BasketLineService;
import com.supermarket.shop.service.BasketService;
import com.supermarket.shop.service.ProductService;
import com.supermarket.shop.service.ShoppingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.initMocks;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SupermarketApplication.class} , properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Testing Basket CLI service")
public class CliBasketServiceTest {


    @Autowired
    private CliBasketService cliBasketService;
    @Autowired
    private BasketService basketService;

    @Autowired
    private Shell shell;

    @Test
    @DisplayName("test shop --name XXX --quantity XX  CLI command")
    public void testShop() {
        // Given

        shell.evaluate(() -> "init");
        shell.evaluate(() -> "shop --name apple --quantity 6 ");

        // When
        Optional<Basket> optionalBasket = basketService.last();
        Basket basket = optionalBasket.isPresent() ? optionalBasket.get() : null;
        // Then
        assertNotNull(basket);
        assertNotNull(basket.getBasketLines());
        assertTrue(basket.getBasketLines().stream().findFirst().isPresent());
        assertEquals(basket.getBasketLines().stream().findFirst().get().getQuantity(), Integer.valueOf(6));
    }

    @Test
    @DisplayName("test checkout CLI command")
    public void testCheckout() {
        // Given
        shell.evaluate(() -> "init");
        shell.evaluate(() -> "shop --name orange --quantity 3 ");
        // When
        Optional<Basket> optionalBasket = basketService.last();
        Basket basket = optionalBasket.isPresent() ? optionalBasket.get() : null;
        assertNotNull(basket);
        Optional<Basket> beforCheckout = basketService.findById(basket.getBasketId());
        cliBasketService.checkout();
        Optional<Basket> afterCheckout = basketService.findById(basket.getBasketId());

        // Then
        assertFalse(beforCheckout.get().getCheckout());
        assertTrue(afterCheckout.get().getCheckout());
    }
}

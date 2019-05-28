package com.supermarket.shop.cli;

import com.supermarket.shop.SupermarketApplication;
import com.supermarket.shop.model.Product;
import com.supermarket.shop.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SupermarketApplication.class} , properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Testing Update CLI Service")
public class CliUpdateServiceTest {

    @Autowired
    Shell shell;

    @Autowired
    ProductService productService;

    @Test
    @DisplayName("test update a stock for a product by CLI command : update-product-stock ")
    public void testUpdateProductStock() {
        // Given  30 apples  in stock
        shell.evaluate(() -> "init");
        // When + 50
        shell.evaluate(() -> "update-product-stock -n apple -q 50");
        // Then
         Optional<Product> apple = productService.findByName("Apple");

        assertEquals(apple.get().getStock(), Optional.of(80).get());


    }

    @Test
    @DisplayName("update a product price by CLI command : update-product-price")
    public void testUpdateProductPrice() {
        // Given apple price 0.20
        shell.evaluate(() -> "init");
        // When
        shell.evaluate(() -> "update-product-price --name apple --price 0.65 ");
        // Then
        Optional<Product> apple = productService.findByName("Apple");

        assertEquals(apple.get().getInitialPrice(), Optional.of(new BigDecimal("0.65")).get());
    }

}

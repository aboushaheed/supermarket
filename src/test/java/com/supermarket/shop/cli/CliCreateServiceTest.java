package com.supermarket.shop.cli;

import com.supermarket.shop.SupermarketApplication;
import com.supermarket.shop.model.Product;
import com.supermarket.shop.service.OfferService;
import com.supermarket.shop.service.ProductService;
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

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SupermarketApplication.class} , properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Testing Create CLI Service")
public class CliCreateServiceTest {


    @Autowired
    ProductService productService;
    @Autowired
    private Shell shell;

    @Test
    @DisplayName("test creat a product by CLI command : update-product-stock ")
    public void testCreateProduct() {
        // Given
        shell.evaluate(()->"create-product --name banana --quantity 5 --price 0.33");
        //then
        assertNotNull(productService.findByName("Banana").get().getProductId());
        assertEquals(productService.findByName("Banana").get().getName(),
               "Banana");
    }
}

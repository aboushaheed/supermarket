package com.supermarket.shop.cli;

import com.supermarket.shop.SupermarketApplication;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SupermarketApplication.class} , properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Testing Stock CLI Service")
public class CliStockServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    Shell shell;
    @Test

    @DisplayName("test initialize stock by CLI command : init ")
    public void testInit() {
        // Given

        // When
        shell.evaluate(() -> "init");

        // Then
        assertNotNull(productService.findAll());

    }
}

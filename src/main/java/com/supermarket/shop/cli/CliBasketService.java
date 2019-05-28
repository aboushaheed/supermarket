package com.supermarket.shop.cli;

import com.supermarket.shop.exception.BadParameterException;
import com.supermarket.shop.exception.NoMoreItemsInStockException;
import com.supermarket.shop.model.Basket;
import com.supermarket.shop.model.BasketLine;
import com.supermarket.shop.service.BasketLineService;
import com.supermarket.shop.service.BasketService;
import com.supermarket.shop.service.ProductService;
import com.supermarket.shop.service.ShoppingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang.StringUtils.capitalize;


@ShellComponent
@Slf4j
public class CliBasketService {

    private ProductService productService;
    private BasketService basketService;
    private ShoppingService shoppingService;
    private BasketLineService basketLineService;
    private CliPrinterService cliPrinterService;

    @Autowired
    public CliBasketService(ProductService productService,
                            BasketService basketService,
                            ShoppingService shoppingService,
                            BasketLineService basketLineService,
                            CliPrinterService cliPrinterService) {
        this.productService = requireNonNull(productService);
        this.cliPrinterService = requireNonNull(cliPrinterService);
        this.basketService = requireNonNull(basketService);
        this.shoppingService = requireNonNull(shoppingService);
        this.basketLineService = requireNonNull(basketLineService);
    }


    @ShellMethod("shop a product to the current basket ,[ shop -n|--name PRODUCT_NAME -q|--quantity QUANTITY ]")
    public void shop(@ShellOption({"-n", "--name"}) String name, @ShellOption({"-q", "--quantity"}) Integer quantity) {

        try {
            Optional<Basket> optionalBasket = basketService.last();

            BasketLine basketLine = BasketLine.builder()
                    .product(productService.findByName(capitalize(name)).get())
                    .quantity(quantity)
                    .build();

            basketLine = basketLineService.save(basketLine);
            Basket basket = shoppingService.addLine(basketLine, optionalBasket.isPresent() ? optionalBasket.get() : null);
            basketLine.setBasket(basketService.save(basket));
            basketLineService.save(basketLine);
            cliPrinterService.basket();

        } catch (NoSuchElementException e) {
            cliPrinterService.message("Please give a correct product name, or tape init to generate stock");
        } catch (NoMoreItemsInStockException e) {
            cliPrinterService.message(e.getMessage());
        } catch (BadParameterException e) {
            cliPrinterService.message(e.getMessage());
        }
    }

    @ShellMethod("checkout all basket")
    public void checkout() {
        Optional<Basket> basket = basketService.last();
        if (basket.isPresent()) {
            shoppingService.checkout(basket.get());
            cliPrinterService.message("Payed [" + basket.get().getFinalPrice() + "], Thank you for placing your order with us!");
        }
    }


}

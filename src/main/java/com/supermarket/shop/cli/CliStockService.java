package com.supermarket.shop.cli;

import com.supermarket.shop.model.Offer;
import com.supermarket.shop.model.Product;
import com.supermarket.shop.service.OfferService;
import com.supermarket.shop.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;


@ShellComponent
@Slf4j
public class CliStockService {

    private ProductService productService;
    private OfferService offerService;
    private CliPrinterService cliPrinterService;
    private boolean callInit = true;

    @Autowired
    public CliStockService(ProductService productService,
                           OfferService offerService,
                           CliPrinterService cliPrinterService) {
        this.productService = requireNonNull(productService);
        this.offerService = requireNonNull(offerService);
        this.cliPrinterService = requireNonNull(cliPrinterService);
    }


    @ShellMethod("init stock (one shot option)")
    public void init() {

        if (callInit) {
            Offer buyOneGetOneFree = offerService.save(Offer.builder()
                    .active(true)
                    .quantityToBuy(1)
                    .quantityForFree(1)
                    .name("Buy One Get One Free")
                    .build());
            productService.save(Product.builder()
                    .name("Apple")
                    .offer(buyOneGetOneFree)
                    .stock(30)
                    .initialPrice(BigDecimal.valueOf(0.20))
                    .build());

            Offer threeForThePriceOfTwo = offerService.save(Offer.builder()
                    .active(true)
                    .quantityToBuy(2)
                    .quantityForFree(1)
                    .name("Three For The Price Of Two")
                    .build());
            productService.save(Product.builder()
                    .name("Watermelon")
                    .offer(threeForThePriceOfTwo)
                    .stock(16)
                    .initialPrice(BigDecimal.valueOf(0.80))
                    .build());

            productService.save(Product.builder()
                    .name("Orange")
                    .stock(40)
                    .initialPrice(BigDecimal.valueOf(0.50))
                    .build());
            callInit = false;
            cliPrinterService.stock();
        } else {
            cliPrinterService.message("stock is already initialized, tape command : [ update -n||--name PRODUCT_NAME -q||--quantity QUANTITY ]");
            return;
        }

    }
}

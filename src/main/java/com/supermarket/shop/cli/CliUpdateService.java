package com.supermarket.shop.cli;

import com.supermarket.shop.exception.NotFoundException;
import com.supermarket.shop.service.OfferService;
import com.supermarket.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;


@ShellComponent
public class CliUpdateService {

    private ProductService productService;
    private OfferService offerService;
    private CliPrinterService cliPrinterService;

    @Autowired
    public CliUpdateService(ProductService productService,
                            OfferService offerService,
                            CliPrinterService cliPrinterService) {

        this.productService = requireNonNull(productService);
        this.cliPrinterService = requireNonNull(cliPrinterService);
        this.offerService = requireNonNull(offerService);
    }


    @ShellMethod(value = "Update a stock for a given product,[ update-product-stock -n||--name PRODUCT_NAME -q||--quantity QUANTITY ]", key = "update-product-stock")
    public void updateProductStock(@ShellOption({"-n", "--name"}) String name, @ShellOption({"-q", "--quantity"}) Integer quantity) {
        try {
            productService.updateStock(name, quantity);
            cliPrinterService.stock();
        } catch (NotFoundException e) {
            cliPrinterService.message(e.getMessage());
        }
    }

    @ShellMethod(value = "Update the price for a given product,[ update-product-price -n||--name PRODUCT_NAME -p||--price PRICE ]", key = "update-product-price")
    public void updateProductPrice(@ShellOption({"-n", "--name"}) String name, @ShellOption({"-p", "--price"}) BigDecimal price) {
        try {
            productService.updatePrice(name, price);
            cliPrinterService.stock();
        } catch (NotFoundException e) {
            cliPrinterService.message(e.getMessage());
        }
    }

    @ShellMethod(value = "Update the offer for a given product,[ update-product-offer -n||--name PRODUCT_NAME -f||--offer OFFER_NAME , use quote fo long name ]", key = "update-product-offer")
    public void updateProductOffer(@ShellOption({"-n", "--name"}) String name, @ShellOption({"-f", "--offer"}) String offerName) {
        try {
            productService.updateProductOffer(name, offerName);
            cliPrinterService.stock();
        } catch (NotFoundException e) {
            cliPrinterService.message(e.getMessage());
        }
    }

    @ShellMethod(value = "Update the offer for a given product,[ update-offer -n||--name OFFER_NAME -qb QUANTITY_TO_BUY  -qf QUANTITY_FOR_FREE  , use quote fo long name ]", key = "update-offer")
    public void updateOffer(@ShellOption({"-n", "--name"}) String name, @ShellOption({"-qb"}) Integer quantityToBuy, @ShellOption({"-qf"}) Integer quantityForFree) {
        try {

            offerService.update(name, quantityToBuy, quantityForFree);
            cliPrinterService.stock();
        } catch (NotFoundException e) {
            cliPrinterService.message(e.getMessage());
        }
    }

}

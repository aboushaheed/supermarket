package com.supermarket.shop.cli;

import com.supermarket.shop.exception.BadParameterException;
import com.supermarket.shop.service.OfferService;
import com.supermarket.shop.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;


@ShellComponent
@Slf4j
public class CliCreateService {

    private ProductService productService;
    private OfferService offerService;
    private CliPrinterService cliPrinterService;

    @Autowired
    public CliCreateService(
            ProductService productService,
            OfferService offerService,
            CliPrinterService cliPrinterService) {
        this.productService = requireNonNull(productService);
        this.offerService = requireNonNull(offerService);
        this.cliPrinterService = requireNonNull(cliPrinterService);
    }


    @ShellMethod(value = "Create an offer,[ create-offer -n||--name OFFER_NAME  -qb QUANTITY_TO_BUY  -qf QUANTITY_FOR_FREE ]", key = "create-offer")
    public void createOffer(@ShellOption({"-n", "--name"}) String name, @ShellOption({"-qb"}) Integer quantityToBuy, @ShellOption({"-qf"}) Integer quantityForFree) {
        try {
            offerService.create(name, quantityToBuy, quantityForFree);
            cliPrinterService.offers();
        }catch (BadParameterException e) {
            cliPrinterService.message(e.getMessage());
        }
    }

    @ShellMethod(value = "Create a product,[ create-product -n||--name PRODUCT_NAME -q||--quantity QUANTITY  -p||--price PRICE ]", key = "create-product")
    public void createProduct(@ShellOption({"-n", "--name"}) String name, @ShellOption({"-q", "--quantity"}) Integer quantity, @ShellOption({"-p", "--price"}) BigDecimal price) {

        try {
            productService.createProduct(name, quantity, price);
            cliPrinterService.stock();
        } catch (BadParameterException e) {
            cliPrinterService.message(e.getMessage());

        }
    }

}

package com.supermarket.shop.cli;

import com.supermarket.shop.model.Basket;
import com.supermarket.shop.model.BasketLine;
import com.supermarket.shop.model.Offer;
import com.supermarket.shop.model.Product;
import com.supermarket.shop.service.BasketLineService;
import com.supermarket.shop.service.BasketService;
import com.supermarket.shop.service.OfferService;
import com.supermarket.shop.service.ProductService;
import dnl.utils.text.table.TextTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;


@ShellComponent
@Slf4j
public class CliPrinterService {

    private ProductService productService;
    private OfferService offerService;
    private BasketService basketService;
    private BasketLineService basketLineService;

    @Autowired
    public CliPrinterService(
                             ProductService productService,
                             OfferService offerService,
                             BasketService basketService,
                             BasketLineService basketLineService) {
        this.productService = requireNonNull(productService);
        this.offerService = requireNonNull(offerService);
        this.basketService = requireNonNull(basketService);
        this.basketLineService = requireNonNull(basketLineService);
    }



    @ShellMethod("show current stock")
    public void stock() {

        final List<Product> products = productService.findAll();
        if(!products.isEmpty()) {

            String[] columnNames = {
                    "Product name",
                    "Qtty",
                    "Price",
                    "Active Offer"};
            int bound = products.size();
            Object data[][] = new Object[bound][4];

            IntStream.range(0, bound).forEachOrdered(i -> {
                data[i][0] = products.get(i).getName();
                data[i][1] = products.get(i).getStock();
                data[i][2] = products.get(i).getInitialPrice();

                data[i][3] = products.get(i).getOffer() != null && products.get(i).getOffer().isActive() ?
                        products.get(i).getOffer().getName() : "has no offer";

            });

            TextTable stockTable = new TextTable(columnNames, data);
            stockTable.printTable();
        }else {
            message("stock is empty, tape [init]");
        }
    }
    @ShellMethod("show current basket")
    public void basket() {

         Optional<Basket> basket = basketService.last();

        if(basket.isPresent()){

              List<BasketLine> basketLines = basketLineService.findBasketLineByBasketId(basket.get().getBasketId());

             String[] columnNames = {
                     "Product name",
                     "Unit price",
                     "Qtty",
                     "Normal price",
                     "Discount price",
                     "Applied offer"};

             int bound = basketLines.size();
             Object data[][] = new Object[basketLines.size()][6];

             IntStream.range(0, bound).forEachOrdered(i -> {
                 data[i][0] = basketLines.get(i).getProduct().getName();
                 data[i][1] = basketLines.get(i).getProduct().getInitialPrice();
                 data[i][2] = basketLines.get(i).getQuantity();
                 data[i][3] = basketLines.get(i).getLinePrice();
                 data[i][4] = basketLines.get(i).getDiscountedPrice();
                 data[i][5] = basketLines.get(i).getProduct().getOffer() != null ?
                         basketLines.get(i).getProduct().getOffer().getName() : "no offer";


             });

             TextTable stockTable = new TextTable(columnNames, data);
             stockTable.printTable();

            String[] columnTotal = {"Total"};

            Object total[][] = new Object[1][1];
                total[0][0] = basket.get().getFinalPrice();

            TextTable totalTable = new TextTable(columnTotal, total);
            totalTable.printTable();
        }else {
            message("basket is not ready, shop some product to initialize it");
        }

    }

    @ShellMethod("show current offers")
    public void offers() {

        final List<Offer> offers = offerService.findAll();
        if(!offers.isEmpty()) {

            String[] columnNames = {
                    "Offer name",
                    "Qtty to buy",
                    "Qtty for free",
                    "Active"};
            int bound = offers.size();
            Object data[][] = new Object[bound][4];

            IntStream.range(0, bound).forEachOrdered(i -> {
                data[i][0] = offers.get(i).getName();
                data[i][1] = offers.get(i).getQuantityToBuy();
                data[i][2] = offers.get(i).getQuantityForFree();

                data[i][3] = offers.get(i).isActive() ?
                        "Yes" : "No";

            });

            TextTable offersTable = new TextTable(columnNames, data);
            offersTable.printTable();
        }else {
            message("no offers, tape [init]");
        }
    }

    @ShellMethod("show a message to screen ,[ message -m|--message MESSAGE_TEXT ]")
    public void message(@ShellOption({"-m", "--message"}) String message){
        String[] columnNames = {
                "Supermarket"};

        Object data[][] = new Object[1][1];
        data[0][0] = message;
        TextTable stockTable = new TextTable(columnNames, data);
        stockTable.printTable();
    }

}

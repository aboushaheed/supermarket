package com.supermarket.shop.exception;

public class NoMoreItemsInStockException extends NotFoundException {

    public NoMoreItemsInStockException(String productName, String rest) {
        super(String.format("we have just %s %s left in our stock", productName, rest));

    }
}

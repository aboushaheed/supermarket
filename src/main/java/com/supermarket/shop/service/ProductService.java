package com.supermarket.shop.service;

import com.supermarket.shop.exception.BadParameterException;
import com.supermarket.shop.exception.NotFoundException;
import com.supermarket.shop.model.Offer;
import com.supermarket.shop.model.Product;
import com.supermarket.shop.repository.OfferRepository;
import com.supermarket.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang.StringUtils.capitalize;

@Component
public class ProductService {

    private ProductRepository productRepository;
    private OfferRepository offerRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, OfferRepository offerRepository) {
        this.productRepository = requireNonNull(productRepository);
        this.offerRepository = requireNonNull(offerRepository);
    }

    public Product updateStock(String name, Integer quantity) {

        Product productToUpdate;
        Optional<Product> optionalProduct = productRepository.findByName(capitalize(name));

        if (optionalProduct.isPresent()) {
            Integer stock = optionalProduct.get().getStock();
            productToUpdate = optionalProduct.get();
            productToUpdate.setStock(stock + quantity);
            productToUpdate = productRepository.save(productToUpdate);

        } else {
            throw new NotFoundException("the product named " + name + " is not created yet");
        }
        return productToUpdate;
    }

    public Product updateProductOffer(String productName, String offerName) {

        Product productToUpdate;
        Optional<Product> optionalProduct = productRepository.findByName(capitalize(productName));
        if (optionalProduct.isPresent()) {

            productToUpdate = optionalProduct.get();
            Optional<Offer> optionalOffer = offerRepository.findByName(offerName);

            if (optionalOffer.isPresent()) {
                productToUpdate.setOffer(optionalOffer.get());
                productToUpdate = productRepository.save(productToUpdate);
            } else {
                throw new NotFoundException("the offer named " + offerName + " is not created yet");
            }
        } else {
            throw new NotFoundException("the product named " + productName + " is not created yet");
        }
        return productToUpdate;
    }

    public Product updatePrice(String name, BigDecimal newPrice) {

        Product productToUpdate;
        Optional<Product> optionalProduct = productRepository.findByName(capitalize(name));

        if (optionalProduct.isPresent()) {
            productToUpdate = optionalProduct.get();
            productToUpdate.setInitialPrice(newPrice);
            productToUpdate = productRepository.save(productToUpdate);

        } else {
            throw new NotFoundException("the product named " + name + " is not created yet");
        }
        return productToUpdate;
    }

    public Product createProduct(String name, Integer quantity, BigDecimal price) {

        Optional<Product> optionalProduct = productRepository.findByName(capitalize(name));

        if (optionalProduct.isPresent()) {
            throw new BadParameterException("the product named " + name + " already exists");

        } else {
          return   productRepository.save(Product.builder()
                    .offer(null)
                    .stock(quantity)
                    .name(capitalize(name))
                    .initialPrice(price)
                    .build());
        }
    }

    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }
}

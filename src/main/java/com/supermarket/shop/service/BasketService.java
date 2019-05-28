package com.supermarket.shop.service;

import com.supermarket.shop.model.Basket;
import com.supermarket.shop.repository.BasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Service
public class BasketService {

    private BasketRepository basketRepository;

    @Autowired
    public BasketService(BasketRepository basketRepository) {
        this.basketRepository = requireNonNull(basketRepository);
    }

    public Optional<Basket> last() {
        return basketRepository.last();
    }

    public Basket save(Basket basket) {
        return basketRepository.save(basket);
    }

    public Optional<Basket> findById(Long basketId) {return basketRepository.findById(basketId);}
}

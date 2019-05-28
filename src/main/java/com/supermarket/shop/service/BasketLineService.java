package com.supermarket.shop.service;

import com.supermarket.shop.model.BasketLine;
import com.supermarket.shop.repository.BasketLineRepository;
import org.springframework.stereotype.Service;


import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
public class BasketLineService {
    private BasketLineRepository basketLineRepository;

    public BasketLineService(BasketLineRepository basketLineRepository) {
        this.basketLineRepository = requireNonNull(basketLineRepository);
    }

    public BasketLine save(BasketLine basketLine) {
        return basketLineRepository.save(basketLine);
    }

    public List<BasketLine> findBasketLineByBasketId(Long basketId) {
       return basketLineRepository.findBasketLineByBasketId(basketId);
    }
}

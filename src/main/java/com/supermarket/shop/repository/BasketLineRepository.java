package com.supermarket.shop.repository;

import com.supermarket.shop.model.Basket;
import com.supermarket.shop.model.BasketLine;
import com.supermarket.shop.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface BasketLineRepository extends JpaRepository<BasketLine, Long> {

    @Query(value = "select * from basketline b  where b.fk_basket_id =:basketId ", nativeQuery = true)
    List<BasketLine> findBasketLineByBasketId(@Param("basketId") Long basketId);
}

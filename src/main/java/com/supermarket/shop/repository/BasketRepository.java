package com.supermarket.shop.repository;

import com.supermarket.shop.model.Basket;
import com.supermarket.shop.model.BasketLine;
import com.supermarket.shop.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface BasketRepository extends JpaRepository<Basket, Long> {

    @Query(value = "select * from basket where checkout is false order by basket_id desc limit 1" , nativeQuery = true)
     Optional<Basket> last();

}

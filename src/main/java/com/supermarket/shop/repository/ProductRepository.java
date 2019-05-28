package com.supermarket.shop.repository;

import com.supermarket.shop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

}

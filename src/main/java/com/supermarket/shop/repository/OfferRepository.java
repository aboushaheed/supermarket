package com.supermarket.shop.repository;

import com.supermarket.shop.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface OfferRepository extends JpaRepository<Offer, Long> {

    Optional<Offer> findByName(String offerName);

}

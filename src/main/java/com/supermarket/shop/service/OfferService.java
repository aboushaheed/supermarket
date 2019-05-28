package com.supermarket.shop.service;


import com.supermarket.shop.exception.BadParameterException;
import com.supermarket.shop.exception.NotFoundException;
import com.supermarket.shop.model.Offer;
import com.supermarket.shop.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Service
public class OfferService {

    private OfferRepository offerRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository) {

        this.offerRepository =requireNonNull(offerRepository);
    }


    public Offer save(Offer offer) {
        return offerRepository.save(offer);
    }

    public Offer create(String name, Integer quantityToBuy, Integer quantityForFree) {

         Optional<Offer> optionalOffer = offerRepository.findByName(name);

         if(!optionalOffer.isPresent()) {

             return offerRepository.save(Offer.builder()
                     .name(name.trim())
                     .quantityForFree(quantityForFree)
                     .quantityToBuy(quantityToBuy)
                     .active(true)
                     .build());

         }else {
             throw new BadParameterException("The offer named " +name+" is already exist");
         }
    }

    public Offer update( String name, Integer quantityToBuy, Integer quantityForFree) {

        Optional<Offer> optionalOffer = offerRepository.findByName(name);

        if(optionalOffer.isPresent()) {

            return offerRepository.save(optionalOffer.get().toBuilder()
                    .quantityForFree(quantityForFree)
                    .quantityToBuy(quantityToBuy)
                    .build());

        }else {
            throw new NotFoundException("The offer named " +name+" dose not exist");
        }
    }

    public Offer activate( String name) {

        Optional<Offer> optionalOffer = offerRepository.findByName(name);

        if(optionalOffer.isPresent()) {

            return offerRepository.save(optionalOffer
                    .get().toBuilder()
                    .active(true)
                    .build());

        }else {
            throw new NotFoundException("The offer named " +name+" dose not exist");
        }
    }

    public List<Offer> findAll() {
        return offerRepository.findAll();
    }
}

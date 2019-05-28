package com.supermarket.shop.service;

import com.supermarket.shop.SupermarketApplication;
import com.supermarket.shop.model.Offer;
import com.supermarket.shop.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SupermarketApplication.class} , properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Testing Offer Service")
public class OfferServiceTest {

    @Autowired
    private OfferService offerService;

    @Test
    @DisplayName(" test save offer")
    public void test_save_offer() {
        // Given
        Offer threeForThePriceOfTwo =  Offer.builder()
                .name("Three For The Price Of Two")
                .active(true)
                .quantityToBuy(2)
                .quantityForFree(3)
                .build();

        // When
        final Offer result = offerService.save(threeForThePriceOfTwo);

        // Then 
        assertNotNull(result.getOfferId());
    }

    @Test
   @DisplayName("test create offer")
    public void testCreate() {
        // Given
        final String name = "Five For The Price Of Three";
        final Integer quantityToBuy = 3;
        final Integer quantityForFree = 5;
        final Offer expectedResult =  Offer.builder()
                .name("Five For The Price Of Three")
                .active(true)
                .quantityToBuy(3)
                .quantityForFree(5)
                .build();

        // When
        final Offer result = offerService.create(name, quantityToBuy, quantityForFree);

        // Then 
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("test update a given offer by name")
    public void testUpdate() {

        // Given
        Offer threeForThePriceOfTwo =  Offer.builder()
                .name("Three For The Price Of Two")
                .active(true)
                .quantityToBuy(2)
                .quantityForFree(3)
                .build();

         offerService.save(threeForThePriceOfTwo);

        final String name = "Three For The Price Of Two";
        final Integer quantityToBuy = 3;
        final Integer quantityForFree = 5;
        final Offer expectedResult =  Offer.builder()
                .offerId(1L)
                .name("Three For The Price Of Two")
                .active(true)
                .quantityToBuy(3)
                .quantityForFree(5)
                .build();

        // When
        final Offer result = offerService.update(name, quantityToBuy, quantityForFree);

        // Then 
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("test activate a given offer by name")
    public void testActivate() {
        // Given
        // Given
        Offer threeForThePriceOfTwo =  Offer.builder()
                .name("Three For The Price Of Two")
                .active(false)
                .quantityToBuy(2)
                .quantityForFree(3)
                .build();

        offerService.save(threeForThePriceOfTwo);

        final String name = "Three For The Price Of Two";
        final Offer expectedResult =  Offer.builder()
                .offerId(1L)
                .name("Three For The Price Of Two")
                .active(true)
                .quantityToBuy(2)
                .quantityForFree(3)
                .build();

        // When
        final Offer result = offerService.activate(name);

        // Then 
        assertEquals(expectedResult, result);
    }

}

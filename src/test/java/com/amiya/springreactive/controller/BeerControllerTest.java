package com.amiya.springreactive.controller;

import com.amiya.springreactive.domain.Beer;
import com.amiya.springreactive.model.BeerDTO;
import com.amiya.springreactive.repositories.BeerRepository;
import com.amiya.springreactive.repositories.BeerRepositoryTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Order(1)
    @Test
    void listBeer() {

        webTestClient.mutateWith(mockOAuth2Login()).get().uri(BeerController.GET_BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Order(2)
    @Test
    void getBeerById() {
        webTestClient.mutateWith(mockOAuth2Login()).get().uri(BeerController.GET_BEER_PATH_ID,1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody(BeerDTO.class);
    }
    @Order(8)
    @Test
    void getBeerByIdFail() {
        webTestClient.mutateWith(mockOAuth2Login()).get().uri(BeerController.GET_BEER_PATH_ID,999)
                .exchange()
                .expectStatus().isNotFound();
    }
    @Order(4)
    @Test
    void saveNewBeerFail() {
        Beer beer = BeerRepositoryTest.getTestBeer();
        beer.setBeerName("");
        webTestClient.mutateWith(mockOAuth2Login()).post().uri(BeerController.GET_BEER_PATH)
                .body(Mono.just(beer),BeerDTO.class)
                .header("Content-Type","application/json")
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Order(3)
    @Test
    void saveNewBeer() {
        webTestClient.mutateWith(mockOAuth2Login()).post().uri(BeerController.GET_BEER_PATH)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()),BeerDTO.class)
                .header("Content-Type","application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/beer/4");
    }

    @Order(5)
    @Test
    void updateExistingBeer() {

        webTestClient.mutateWith(mockOAuth2Login()).put().uri(BeerController.GET_BEER_PATH_ID,1)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()),BeerDTO.class)
                .header("Content-Type","application/json")
                .exchange()
                .expectStatus().isNoContent();
    }
    @Order(6)
    @Test
    void updateExistingBeerFail() {
        Beer beer = BeerRepositoryTest.getTestBeer();
        beer.setBeerName("");
        webTestClient.mutateWith(mockOAuth2Login()).put().uri(BeerController.GET_BEER_PATH_ID,1)
                .body(Mono.just(beer),BeerDTO.class)
                .header("Content-Type","application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }
    @Order(9)
    @Test
    void updateExistingBeerIdNotFoundFail() {
        Beer beer = BeerRepositoryTest.getTestBeer();
        webTestClient
                .mutateWith(mockOAuth2Login()).put()
                .uri(BeerController.GET_BEER_PATH_ID,999)
                .body(Mono.just(beer),BeerDTO.class)
                .header("Content-Type","application/json")
                .exchange()
                .expectStatus().isNotFound();
    }


    @Order(7)
    @Test
    void deleteBeerById() {
        webTestClient.mutateWith(mockOAuth2Login()).delete().uri(BeerController.GET_BEER_PATH_ID,1)
                .exchange()
                .expectStatus().isNoContent();
    }
    @Test
    void testPatchIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch()
                .uri(BeerController.GET_BEER_PATH_ID, 999)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }
    @Test
    void deleteBeerByIdNotFound() {
        webTestClient.
                mutateWith(mockOAuth2Login())
            .delete().uri(BeerController.GET_BEER_PATH_ID,999)
                .exchange()
                .expectStatus().isNotFound();
    }
}
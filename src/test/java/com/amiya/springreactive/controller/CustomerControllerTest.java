package com.amiya.springreactive.controller;

import com.amiya.springreactive.domain.Customer;
import com.amiya.springreactive.model.CustomerDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class CustomerControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(999)
    void testDeleteCustomer() {
        webTestClient.mutateWith(mockOAuth2Login()).delete()
                .uri(CustomerController.GET_CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus()
                .isNoContent();
    }
    @Test
    void testDeleteCustomerFail() {
        webTestClient.mutateWith(mockOAuth2Login()).delete()
                .uri(CustomerController.GET_CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @Order(3)
    void testUpdateCustomer() {
        webTestClient.mutateWith(mockOAuth2Login()).put()
                .uri(CustomerController.GET_CUSTOMER_PATH_ID, 1)
                .body(Mono.just(getCustomerDto()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }
    @Test
    void testUpdateCustomerFail() {
        CustomerDTO customer=getCustomerDto();
        customer.setCustomerName("");
        webTestClient.mutateWith(mockOAuth2Login()).put()
                .uri(CustomerController.GET_CUSTOMER_PATH_ID, 1)
                .body(Mono.just(customer), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }
    @Test
    void testUpdateCustomerNotFound() {
        webTestClient.mutateWith(mockOAuth2Login()).put()
                .uri(CustomerController.GET_CUSTOMER_PATH_ID, 999)
                .body(Mono.just(getCustomerDto()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void testCreateCustomer() {

        webTestClient.mutateWith(mockOAuth2Login()).post().uri(CustomerController.GET_CUSTOMER_PATH)
                .body(Mono.just(getCustomerDto()), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/customer/4");
    }
    @Test
    void testCreateCustomerFail() {
        CustomerDTO customer=getCustomerDto();
        customer.setCustomerName("");
        webTestClient.mutateWith(mockOAuth2Login()).post().uri(CustomerController.GET_CUSTOMER_PATH)
                .body(Mono.just((customer)), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(1)
    void testGetById() {
        webTestClient.mutateWith(mockOAuth2Login()).get().uri(CustomerController.GET_CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(CustomerDTO.class);
    }
    @Test

    void testGetByIdFail() {
        webTestClient.mutateWith(mockOAuth2Login()).get().uri(CustomerController.GET_CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(2)
    void testListCustomers() {
        webTestClient.mutateWith(mockOAuth2Login()).get().uri(CustomerController.GET_CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    public static CustomerDTO getCustomerDto() {
        return CustomerDTO.builder()
                .customerName("Test Customer")
                .build();
    }
}
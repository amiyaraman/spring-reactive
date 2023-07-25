package com.amiya.springreactive.service;

import com.amiya.springreactive.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface CustomerService {
    Flux<CustomerDTO> listCustomer();

    Mono<CustomerDTO> getCustomerById(Integer customerId);

    Mono<CustomerDTO> saveNewCustomer(CustomerDTO customerDTO);

    Mono<CustomerDTO> updateExistingCustomer(Integer customerId, CustomerDTO customerDTO);

    Mono<CustomerDTO> patchCustomer(Integer customerId, CustomerDTO customerDTO);

    Mono<Void> deleteCustomerById(Integer customerId);
}

package com.amiya.springreactive.controller;


import com.amiya.springreactive.model.BeerDTO;
import com.amiya.springreactive.model.CustomerDTO;
import com.amiya.springreactive.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    public static final String GET_CUSTOMER_PATH = "/api/v2/customer";

    public static final String GET_CUSTOMER_PATH_ID = GET_CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;


    @GetMapping(GET_CUSTOMER_PATH)
    public Flux<CustomerDTO> listCustomer() {
//        BeerDTO beer1= BeerDTO.builder().id(1).build();
//        BeerDTO beer2= BeerDTO.builder().id(2).build();
        return customerService.listCustomer();
    }
    @GetMapping(GET_CUSTOMER_PATH_ID)
    public Mono<CustomerDTO> getCustomerById(@PathVariable("customerId") Integer customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PostMapping(GET_CUSTOMER_PATH)
    Mono<ResponseEntity<Void>> saveNewCustomer(@Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.saveNewCustomer(customerDTO)
                .map(savedCustomerDTO -> ResponseEntity
                        .created(UriComponentsBuilder
                                .fromHttpUrl("http://localhost:8080/"
                                        + GET_CUSTOMER_PATH
                                        + "/" + savedCustomerDTO.getId())
                                .build().toUri()).build());
    }

    @PutMapping(GET_CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>>  updateExistingCustomer(@PathVariable("customerId") Integer customerId, @Validated @RequestBody CustomerDTO customerDTO){
        return customerService.updateExistingCustomer(customerId,customerDTO).map(savedCustomerDTO -> ResponseEntity
                .ok().build());
    }

    @PatchMapping(GET_CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> patchExistingCustomer(@PathVariable("customerId") Integer customerId,
                                                 @Validated @RequestBody CustomerDTO customerDTO){
        return customerService.patchCustomer(customerId, customerDTO)
                .map(updatedDto -> ResponseEntity.ok().build());
    }


    @DeleteMapping(GET_CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteById(@PathVariable("customerId") Integer customerId){
        return customerService.deleteCustomerById(customerId).map(response -> ResponseEntity
                .noContent().build());
    }
}

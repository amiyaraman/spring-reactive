package com.amiya.springreactive.service;


import com.amiya.springreactive.mapper.CustomerMapper;
import com.amiya.springreactive.model.BeerDTO;
import com.amiya.springreactive.model.CustomerDTO;
import com.amiya.springreactive.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    @Override
    public Flux<CustomerDTO> listCustomer() {
        return customerRepository.findAll().map(customerMapper::costumerToCostumerDTO);
    }

    @Override
    public Mono<CustomerDTO> getCustomerById(Integer customerId) {
        return customerRepository.findById(customerId).map(customerMapper::costumerToCostumerDTO);
    }

    @Override
    public Mono<CustomerDTO> saveNewCustomer(CustomerDTO customerDTO) {
        return customerRepository.save(customerMapper.customerDTOToCustomer(customerDTO)).map(customerMapper::costumerToCostumerDTO);
    }

    @Override
    public Mono<CustomerDTO> updateExistingCustomer(Integer customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId).map(
                foundCustomer->{
                    foundCustomer.setCustomerName(customerDTO.getCustomerName());
                    return foundCustomer;
                }
        ).flatMap(customerRepository::save).map(customerMapper::costumerToCostumerDTO);
    }

    @Override
    public Mono<CustomerDTO> patchCustomer(Integer customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    if (StringUtils.hasText(customerDTO.getCustomerName())){
                        customer.setCustomerName(customerDTO.getCustomerName());
                    }
                    return customer;
                }).flatMap(customerRepository::save)
                .map(customerMapper::costumerToCostumerDTO);
    }

    @Override
    public Mono<Void> deleteCustomerById(Integer customerId) {
        return customerRepository.deleteById(customerId);
    }
}

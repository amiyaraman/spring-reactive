package com.amiya.springreactive.mapper;

import com.amiya.springreactive.domain.Customer;
import com.amiya.springreactive.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    CustomerDTO costumerToCostumerDTO(Customer customer);

    Customer customerDTOToCustomer(CustomerDTO customerDTO);



}

package com.amiya.springreactive.repositories;

import com.amiya.springreactive.domain.Customer;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CustomerRepository extends R2dbcRepository<Customer,Integer> {
}

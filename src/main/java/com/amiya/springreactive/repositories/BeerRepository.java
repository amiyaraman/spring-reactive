package com.amiya.springreactive.repositories;

import com.amiya.springreactive.domain.Beer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BeerRepository extends ReactiveCrudRepository<Beer,Integer> {
}

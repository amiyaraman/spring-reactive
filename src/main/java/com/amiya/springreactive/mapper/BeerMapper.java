package com.amiya.springreactive.mapper;

import com.amiya.springreactive.domain.Beer;
import com.amiya.springreactive.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    BeerDTO beerToBeerDTO(Beer beer);

    Beer beerDTOToBeer(BeerDTO beerDTO);
}

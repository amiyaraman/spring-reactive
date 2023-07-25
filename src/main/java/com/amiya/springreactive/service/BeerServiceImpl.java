package com.amiya.springreactive.service;

import com.amiya.springreactive.domain.Beer;
import com.amiya.springreactive.mapper.BeerMapper;
import com.amiya.springreactive.model.BeerDTO;
import com.amiya.springreactive.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;

    private final BeerMapper beerMapper;



    @Override
    public Flux<BeerDTO> listBeer() {

        return beerRepository.findAll().map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> getBeerById(Integer beerId) {
        return beerRepository.findById(beerId).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> saveNewBeer(BeerDTO beerDTO) {
        return beerRepository
                .save(beerMapper.beerDTOToBeer(beerDTO))
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> updateExistingBeer(Integer beerId, BeerDTO beerDTO) {
        Mono<Beer> beerMono= beerRepository.findById(beerId).map(
                foundBeer->{
                    foundBeer.setBeerName(beerDTO.getBeerName());
                    foundBeer.setBeerStyle(beerDTO.getBeerStyle());
                    foundBeer.setPrice(beerDTO.getPrice());
                    foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    foundBeer.setUpc(beerDTO.getUpc());
                    return foundBeer;
                }
        );
        return beerMono.flatMap(beerRepository::save).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> patchBeer(Integer beerId, BeerDTO beerDTO) {
        return beerRepository.findById(beerId)
                .map(foundBeer -> {
                    if(StringUtils.hasText(beerDTO.getBeerName())){
                        foundBeer.setBeerName(beerDTO.getBeerName());
                    }

                    if(StringUtils.hasText(beerDTO.getBeerStyle())){
                        foundBeer.setBeerStyle(beerDTO.getBeerStyle());
                    }

                    if(beerDTO.getPrice() != null){
                        foundBeer.setPrice(beerDTO.getPrice());
                    }

                    if(StringUtils.hasText(beerDTO.getUpc())){
                        foundBeer.setUpc(beerDTO.getUpc());
                    }

                    if(beerDTO.getQuantityOnHand() != null){
                        foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    }
                    return foundBeer;
                }).flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<Void> deleteBeerById(Integer beerId) {
        return beerRepository.deleteById(beerId);
    }
}

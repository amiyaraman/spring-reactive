package com.amiya.springreactive.controller;


import com.amiya.springreactive.model.BeerDTO;
import com.amiya.springreactive.service.BeerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
public class BeerController {
    public static final String GET_BEER_PATH = "/api/v2/beer";

    public static final String GET_BEER_PATH_ID = GET_BEER_PATH + "/{beerId}";

    private final BeerService beerService;


    @GetMapping(GET_BEER_PATH)
    public Flux<BeerDTO> listBeer() {
//        BeerDTO beer1= BeerDTO.builder().id(1).build();
//        BeerDTO beer2= BeerDTO.builder().id(2).build();
        return beerService.listBeer();
    }

    @GetMapping(GET_BEER_PATH_ID)
    public Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping(GET_BEER_PATH)
    Mono<ResponseEntity<Void>> saveNewBeer( @Validated @RequestBody BeerDTO beerDTO) {
        return beerService.saveNewBeer(beerDTO)
                .map(savedBeerDTO -> ResponseEntity
                        .created(UriComponentsBuilder
                                .fromHttpUrl("http://localhost:8080/"
                                        + GET_BEER_PATH
                                        + "/" + savedBeerDTO.getId())
                                .build().toUri()).build());
    }

    @PutMapping(GET_BEER_PATH_ID)
    Mono<ResponseEntity<Void>>  updateExistingBeer(@PathVariable("beerId") Integer beerId, @Validated @RequestBody BeerDTO beerDTO){
        return beerService.updateExistingBeer(beerId,beerDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(savedBeerDTO -> ResponseEntity
                .noContent().build());
    }

    @PatchMapping(GET_BEER_PATH_ID)
    Mono<ResponseEntity<Void>> patchExistingBeer(@PathVariable Integer beerId,
                                                 @Validated @RequestBody BeerDTO beerDTO){
        return beerService.patchBeer(beerId, beerDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(updatedDto -> ResponseEntity.noContent().build());
    }

    @DeleteMapping(GET_BEER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteBeerById(@PathVariable("beerId") Integer beerId){
        return beerService.getBeerById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(beerDTO -> beerService.deleteBeerById(beerDTO.getId()))
                .thenReturn(ResponseEntity.noContent().build());
    }
}


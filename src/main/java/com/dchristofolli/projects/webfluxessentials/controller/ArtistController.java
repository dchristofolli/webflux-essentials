package com.dchristofolli.projects.webfluxessentials.controller;

import com.dchristofolli.projects.webfluxessentials.domain.Artist;
import com.dchristofolli.projects.webfluxessentials.service.ArtistService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("artists")
public class ArtistController {
    private final ArtistService artistService;

    @GetMapping(path = "artists")
    public Flux<Artist> listAllArtists() {
        return artistService.findAll();
    }

    @PostMapping(path = "artist")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Artist> saveArtist(@Valid @RequestBody Artist artist) {
        return artistService.save(artist);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteArtist(String id){
        return artistService.delete(id);
    }
}

package com.dchristofolli.projects.webfluxessentials.service;

import com.dchristofolli.projects.webfluxessentials.domain.Artist;
import com.dchristofolli.projects.webfluxessentials.repository.ArtistRepository;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;

    public Flux<Artist> findAll() {
        return artistRepository.findAll()
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public Mono<Artist> findById(String id) {
        return artistRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

//    public Mono<Artist> findByName(String name) {
//        return artistRepository.findByArtistName(name);
//    }

    public Mono<Boolean> existsById(String id){
        return artistRepository.existsById(id);
    }

    public <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found"));
    }

    public Mono<Artist> save(Artist artist) {
        return artistRepository.save(artist.withArtistName(artist.getArtistName().toUpperCase()))
                .doOnNext(this::throwResponseStatusWhenEmptyArtist);
    }

    public Mono<Void> update(Artist artist) {
        return findById(artist.getId())
                .flatMap(artistRepository::save)
                .then();
    }

    public Mono<Void> delete(String id) {
        return findById(id)
                .flatMap(artistRepository::delete);
    }

    private void throwResponseStatusWhenEmptyArtist(Artist artist) {
        if (StringUtil.isNullOrEmpty(artist.getArtistName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data");
        }
    }
}

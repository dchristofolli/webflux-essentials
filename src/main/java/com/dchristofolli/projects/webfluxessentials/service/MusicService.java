package com.dchristofolli.projects.webfluxessentials.service;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
import com.dchristofolli.projects.webfluxessentials.repository.MusicRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class MusicService {
    private final MusicRepository repository;

    public Flux<Music> findAll() {
        return repository.findAll();
    }

    public Mono<Music> findById(Integer id) {
        return repository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Music not found"));
    }

    public Mono<Music> save(Music music) {
        return repository.save(music);
    }

    public Mono<Void> update(Music music){
        return repository.findById(music.getId())
                .map(foundMusic -> music.withId(foundMusic.getId()))
                .flatMap(repository::save)
                .thenEmpty(Mono.empty());
    }

    public Mono<Void> delete(int id) {
        return repository.findById(id)
                .flatMap(repository::delete);
    }
}

package com.dchristofolli.projects.webfluxessentials.service;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
import com.dchristofolli.projects.webfluxessentials.repository.MusicRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MusicService {
    private final MusicRepository repository;

    public MusicService(MusicRepository repository) {
        this.repository = repository;
    }

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
}

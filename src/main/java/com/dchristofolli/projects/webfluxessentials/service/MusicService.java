package com.dchristofolli.projects.webfluxessentials.service;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
import com.dchristofolli.projects.webfluxessentials.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MusicService {
    private final MusicRepository musicRepository;

    public Flux<Music> findAll() {
        return musicRepository.findAll()
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public Mono<Music> findById(int id) {
        return musicRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Music not found"));
    }

    public Mono<Music> save(Music music) {
        return musicRepository.save(music);
    }

    public Mono<Void> update(Music music) {
        return findById(music.getId())
                .flatMap(musicRepository::save)
                .then();
    }

    public Mono<Void> delete(int id) {
        return findById(id)
                .flatMap(musicRepository::delete);
    }
}

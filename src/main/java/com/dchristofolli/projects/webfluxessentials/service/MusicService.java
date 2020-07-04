package com.dchristofolli.projects.webfluxessentials.service;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
import com.dchristofolli.projects.webfluxessentials.repository.MusicRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MusicService {
    private final MusicRepository repository;

    public MusicService(MusicRepository repository) {
        this.repository = repository;
    }

    public Flux<Music> findAll() {
        return repository.findAll();
    }
}

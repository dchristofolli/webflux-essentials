package com.dchristofolli.projects.webfluxessentials.controller;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
import com.dchristofolli.projects.webfluxessentials.service.MusicService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/musics")
public class MusicController {
    private final MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping
    public Flux<Music> listAll() {
        return musicService.findAll();
    }

    @GetMapping(path = "/{id}")
    public Mono<Music> findById(@PathVariable Integer id){
        return musicService.findById(id);
    }
}

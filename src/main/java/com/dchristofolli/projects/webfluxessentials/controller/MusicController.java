package com.dchristofolli.projects.webfluxessentials.controller;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
import com.dchristofolli.projects.webfluxessentials.repository.MusicRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/music")
public class MusicController {
    private final MusicRepository musicRepository;

    public MusicController(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    @GetMapping
    public Flux<Music> listAll(){
        return musicRepository.findAll();
    }
}

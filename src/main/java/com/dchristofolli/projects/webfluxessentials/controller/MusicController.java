package com.dchristofolli.projects.webfluxessentials.controller;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
import com.dchristofolli.projects.webfluxessentials.service.MusicService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("musics")
@AllArgsConstructor
public class MusicController {
    private final MusicService musicService;

    @GetMapping
    public Flux<Music> listAll() {
        return musicService.findAll();
    }

    @GetMapping(path = "{id}")
    public Mono<Music> findById(@PathVariable Integer id) {
        return musicService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Music> save(@Valid @RequestBody Music music) {
        return musicService.save(music);
    }

    @PostMapping("batch")
    @ResponseStatus(HttpStatus.CREATED)
    public Flux<Music> saveBatch(@Valid @RequestBody List<Music> musics) {
        return musicService.saveAll(musics);
    }

    @PutMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@PathVariable int id, @Valid @RequestBody Music music) {
        return musicService.update(music.withId(id));
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> delete(@PathVariable int id) {
        return musicService.delete(id);
    }
}

package com.dchristofolli.projects.webfluxessentials.controller;

import com.dchristofolli.projects.webfluxessentials.domain.Artist;
import com.dchristofolli.projects.webfluxessentials.domain.Music;
import com.dchristofolli.projects.webfluxessentials.service.ArtistService;
import com.dchristofolli.projects.webfluxessentials.service.MusicService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final ArtistService artistService;

    @GetMapping
    public Flux<Music> listAll() {
        return musicService.findAll();
    }

    @GetMapping(path = "artists")
    public Flux<Artist> listAllArtists() {
        return artistService.findAll();
    }

    @GetMapping(path = "{id}")
    public Mono<Music> findById(@PathVariable String id) {
        return musicService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Music> save(@Valid @RequestBody Music music) {
        return musicService.save(music);
    }

    @PostMapping(path = "artist")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Artist> saveArtist(@Valid @RequestBody Artist artist) {
        return artistService.save(artist);
    }

    @PostMapping("batch")
    @ResponseStatus(HttpStatus.CREATED)
    public Flux<Music> saveBatch(@Valid @RequestBody List<Music> musics) {
        return musicService.saveAll(musics);
    }

    @PutMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@PathVariable String id, @Valid @RequestBody Music music) {
        return musicService.update(music.withId(id));
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return musicService.delete(id);
    }
}

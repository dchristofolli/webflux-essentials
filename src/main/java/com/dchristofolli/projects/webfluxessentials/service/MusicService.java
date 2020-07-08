package com.dchristofolli.projects.webfluxessentials.service;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
import com.dchristofolli.projects.webfluxessentials.repository.MusicRepository;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicService {
    private final MusicRepository musicRepository;
    private final ArtistService artistService;

    public Flux<Music> findAll() {
        return musicRepository.findAll()
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public Mono<Music> findById(String id) {
        return musicRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Music not found"));
    }

    public Mono<Music> save(Music music) {
        return artistService.existsById(music.getArtistId())
                .flatMap(aBoolean -> {
                    if (Boolean.TRUE.equals(aBoolean))
                        return musicRepository.save(music.withSongName(music.getSongName().toUpperCase()));
                    return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not exists"));
                });
    }

    public Mono<Void> update(Music music) {
        return findById(music.getId())
                .flatMap(musicRepository::save)
                .then();
    }

    public Mono<Void> delete(String id) {
        return findById(id)
                .flatMap(musicRepository::delete);
    }

    @Transactional
    public Flux<Music> saveAll(List<Music> musics) {
        musics.parallelStream()
                .forEach(m -> m.setSongName(m.getSongName().toUpperCase()));
        return musicRepository.saveAll(musics)
                .doOnNext(this::throwResponseStatusWhenEmptyMusic);
    }

    private void throwResponseStatusWhenEmptyMusic(Music music) {
        if (StringUtil.isNullOrEmpty(music.getSongName()) ||
                StringUtil.isNullOrEmpty(music.getArtistId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data");
        }
    }
}

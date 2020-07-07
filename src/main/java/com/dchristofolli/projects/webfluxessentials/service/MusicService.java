package com.dchristofolli.projects.webfluxessentials.service;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
import com.dchristofolli.projects.webfluxessentials.repository.MusicRepository;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
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
        return musicRepository.save(music)
                .doOnNext(this::throwResponseStatusWhenEmptyMusic);
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
        return musicRepository.saveAll(musics)
                .doOnNext(this::throwResponseStatusWhenEmptyMusic);
    }

    private void throwResponseStatusWhenEmptyMusic(Music music) {
        if (StringUtil.isNullOrEmpty(music.getSongName()) ||
                StringUtil.isNullOrEmpty(music.getArtistName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data");
        }
    }
}

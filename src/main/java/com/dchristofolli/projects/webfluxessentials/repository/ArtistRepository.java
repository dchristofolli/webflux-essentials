package com.dchristofolli.projects.webfluxessentials.repository;

import com.dchristofolli.projects.webfluxessentials.domain.Artist;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ArtistRepository extends ReactiveMongoRepository<Artist, String> {
    Mono<Artist> findByArtistName(String name);

    boolean existsByArtistName(String name);
}

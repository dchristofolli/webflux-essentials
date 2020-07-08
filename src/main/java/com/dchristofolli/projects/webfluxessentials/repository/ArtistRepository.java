package com.dchristofolli.projects.webfluxessentials.repository;

import com.dchristofolli.projects.webfluxessentials.domain.Artist;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends ReactiveMongoRepository<Artist, String> {
}

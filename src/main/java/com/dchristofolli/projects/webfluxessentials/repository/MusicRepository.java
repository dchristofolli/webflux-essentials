package com.dchristofolli.projects.webfluxessentials.repository;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepository extends ReactiveMongoRepository<Music, String> {
}

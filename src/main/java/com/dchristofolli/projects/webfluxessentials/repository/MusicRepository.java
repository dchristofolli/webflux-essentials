package com.dchristofolli.projects.webfluxessentials.repository;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepository extends ReactiveCrudRepository<Music, Integer> {
}

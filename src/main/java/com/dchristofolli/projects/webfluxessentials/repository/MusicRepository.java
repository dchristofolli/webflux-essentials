package com.dchristofolli.projects.webfluxessentials.repository;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MusicRepository extends ReactiveCrudRepository<Music, Integer> {
}

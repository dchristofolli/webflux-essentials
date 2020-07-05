package com.dchristofolli.projects.webfluxessentials.integration;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
import com.dchristofolli.projects.webfluxessentials.exception.CustomAttributes;
import com.dchristofolli.projects.webfluxessentials.repository.MusicRepository;
import com.dchristofolli.projects.webfluxessentials.service.MusicCreator;
import com.dchristofolli.projects.webfluxessentials.service.MusicService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import({MusicService.class, CustomAttributes.class})
class MusicControllerIT {
    @MockBean
    private MusicRepository musicRepository;

    @Autowired
    private WebTestClient testClient;

    private final Music music = MusicCreator.createValidMusic();

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
    }

    @BeforeEach
    public void setup() {
        BDDMockito.when(musicRepository.findAll())
                .thenReturn(Flux.just(music));
        BDDMockito.when(musicRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(music));
        BDDMockito.when(musicRepository.save(MusicCreator.createMusicToBeSaved()))
                .thenReturn(Mono.just(music));
        BDDMockito.when(musicRepository.delete(ArgumentMatchers.any(Music.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(musicRepository.save(MusicCreator.createValidMusic()))
                .thenReturn(Mono.empty());
    }

    @Test
    void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("should fail");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

    @Test
    void listAll_ReturnFluxOfMusic_whenSuccessful() {
        testClient
                .get()
                .uri("/musics")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(music.getId())
                .jsonPath("$.[0].name").isEqualTo(music.getName());
    }

    @Test
    void listAll_Flavour2_ReturnFluxOfMusic_whenSuccessful() {
        testClient
                .get()
                .uri("/musics")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Music.class)
                .hasSize(1)
                .contains(music);
    }

    @Test
    void findById_ReturnMonoOfMusic_whenSuccessful() {
        testClient
                .get()
                .uri("/musics/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Music.class)
                .isEqualTo(music);
    }

    @Test
    void findById_ReturnMonoError_whenEmptyMonoIsReturned() {
        BDDMockito.when(musicRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());
        testClient
                .get()
                .uri("/musics/{id}", 1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.developerMessage").isEqualTo("A ResponseStatusException Happened");
    }

    @Test
    void save_createsMusic_whenSuccessful() {
        Music musicToBeSaved = MusicCreator.createMusicToBeSaved();
        testClient
                .post()
                .uri("/musics/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(musicToBeSaved))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Music.class)
                .isEqualTo(music);
    }

    @Test
    void save_returnsError_whenNameIsEmpty() {
        Music musicToBeSaved = MusicCreator.createMusicToBeSaved().withName("");
        testClient
                .post()
                .uri("/musics/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(musicToBeSaved))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400);
    }

    @Test
    void delete_removesMusic_whenSuccessful() {
        testClient
                .delete()
                .uri("/musics/{id}", 1)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody();
    }

    @Test
    void delete_returnsError_whenMusicIsNotFound() {
        BDDMockito.when(musicRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());
        testClient
                .delete()
                .uri("/musics/{id}", 1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.developerMessage").isEqualTo("A ResponseStatusException Happened");
    }

    @Test
    void update_saveUpdateMusic_whenSuccessful() {
        testClient
                .put()
                .uri("/musics/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(music))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void update_returnsMonoError_whenMusicNotExists() {
        BDDMockito.when(musicRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());
        testClient
                .put()
                .uri("/musics/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(music))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.developerMessage").isEqualTo("A ResponseStatusException Happened");

    }
}

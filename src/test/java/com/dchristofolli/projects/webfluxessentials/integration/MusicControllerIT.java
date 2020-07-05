package com.dchristofolli.projects.webfluxessentials.integration;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import(MusicService.class)
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
    void findById_ReturnMonoOfMusic_whenSuccessful(){
        testClient
                .get()
                .uri("/musics/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Music.class)
                .isEqualTo(music);
    }

    @Test
    void findById_ReturnMonoError_whenEmptyMonoIsReturned(){
        BDDMockito.when(musicRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());
        testClient
                .get()
                .uri("/musics/{id}", 1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);
    }

}

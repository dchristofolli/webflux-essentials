package com.dchristofolli.projects.webfluxessentials.service;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
import com.dchristofolli.projects.webfluxessentials.repository.MusicRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
class MusicServiceTest {
    @InjectMocks
    private MusicService musicService;
    @Mock
    private MusicRepository musicRepository;

    private final Music music = MusicCreator.createValidMusic();

    @BeforeAll
    public static void blockHoudSetup(){
        BlockHound.install();
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

    @BeforeEach
    public void setup(){
        BDDMockito.when(musicRepository.findAll()).thenReturn(Flux.just(music));
        BDDMockito.when(musicRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Mono.just(music));
    }

    @Test
    void findAll_ReturnFluxOfMusic_whenSuccessful(){
        StepVerifier.create(musicService.findAll())
                .expectSubscription()
                .expectNext(music)
                .verifyComplete();
    }

    @Test
    void findById_ReturnMonoOfMusic_whenSuccessful(){
        StepVerifier.create(musicService.findById(1))
                .expectSubscription()
                .expectNext(music)
                .verifyComplete();
    }

    @Test
    void findById_ReturnMonoError_whenEmptyMonoIsReturned(){
        BDDMockito.when(musicRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(musicService.findById(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }
}
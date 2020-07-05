package com.dchristofolli.projects.webfluxessentials.controller;

import com.dchristofolli.projects.webfluxessentials.domain.Music;
import com.dchristofolli.projects.webfluxessentials.service.MusicCreator;
import com.dchristofolli.projects.webfluxessentials.service.MusicService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class MusicControllerTest {
    @InjectMocks
    private MusicController musicController;
    @Mock
    private MusicService musicService;

    private final Music music = MusicCreator.createValidMusic();

    @BeforeAll
    public static void blockHoundSetup(){
        BlockHound.install();
    }

    @BeforeEach
    public void setup(){
        BDDMockito.when(musicService.findAll())
                .thenReturn(Flux.just(music));
        BDDMockito.when(musicService.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(music));
        BDDMockito.when(musicService.save(MusicCreator.createMusicToBeSaved()))
                .thenReturn(Mono.just(music));
        BDDMockito.when(musicService.delete(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());
        BDDMockito.when(musicService.update(MusicCreator.createValidMusic()))
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
    void findAll_ReturnFluxOfMusic_whenSuccessful(){
        StepVerifier.create(musicController.listAll())
                .expectSubscription()
                .expectNext(music)
                .verifyComplete();
    }

    @Test
    void findById_ReturnMonoOfMusic_whenSuccessful(){
        StepVerifier.create(musicController.findById(1))
                .expectSubscription()
                .expectNext(music)
                .verifyComplete();
    }

    @Test
    void save_createsMusic_whenSuccessful(){
        Music musicToBeSaved = MusicCreator.createMusicToBeSaved();
        StepVerifier.create(musicController.save(musicToBeSaved))
                .expectSubscription()
                .expectNext(music)
                .verifyComplete();
    }

    @Test
    void delete_removesMusic_whenSuccessful(){
        StepVerifier.create(musicController.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void update_saveUpdateMusic_whenSuccessful(){
        StepVerifier.create(musicController.update(1, MusicCreator.createValidMusic()))
                .expectSubscription()
                .verifyComplete();
    }

}
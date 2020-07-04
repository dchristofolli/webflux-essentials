package com.dchristofolli.projects.webfluxessentials.service;

import com.dchristofolli.projects.webfluxessentials.domain.Music;

public class MusicCreator {
    public static Music createMusicToBeSaved(){
        return Music.builder()
                .name("Your Love")
                .build();
    }

    public static Music createValidMusic(){
        return Music.builder()
                .name("Your Love")
                .build();
    }

    public static Music createValidUpdateMusic(){
        return Music.builder()
                .name("Raining Blood")
                .build();
    }
}

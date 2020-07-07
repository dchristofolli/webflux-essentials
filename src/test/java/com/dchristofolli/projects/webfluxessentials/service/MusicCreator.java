package com.dchristofolli.projects.webfluxessentials.service;

import com.dchristofolli.projects.webfluxessentials.domain.Music;

public class MusicCreator {
    public static Music createMusicToBeSaved(){
        return Music.builder()
                .songName("YOUR LOVE")
                .artistId("THE OUTFIELD")
                .build();
    }

    public static Music createValidMusic(){
        return Music.builder()
                .id(1)
                .songName("YOUR LOVE")
                .artistId("THE OUTFIELD")
                .build();
    }

    public static Music createValidUpdatedMusic(){
        return Music.builder()
                .songName("Your Love")
                .artistId("The Outfield")
                .build();
    }
}

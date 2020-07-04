package com.dchristofolli.projects.webfluxessentials.domain;

import javax.validation.constraints.NotBlank;

public class MusicBuilder {
    private Integer id;
    private @NotBlank(message = "The name of this music must be filled") String name;

    public MusicBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public MusicBuilder setName(@NotBlank(message = "The name of this music must be filled") String name) {
        this.name = name;
        return this;
    }

    public Music createMusic() {
        return new Music(id, name);
    }
}
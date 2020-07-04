package com.dchristofolli.projects.webfluxessentials.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;

@Table("music")
public class Music {
    @Id
    private Integer id;

    @NotBlank(message = "The name of this music must be filled")
    private String name;

    public Music(Integer id, @NotBlank(message = "The name of this music must be filled") String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

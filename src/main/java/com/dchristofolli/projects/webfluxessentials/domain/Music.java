package com.dchristofolli.projects.webfluxessentials.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document("music")
@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class Music {
    @Id
    private String id;

    @NotBlank(message = "The name of this music must be filled")
    @Indexed(name = "name")
    private String songName;

    @NotBlank(message = "The name of artist must be filled")
    @Indexed(name = "artist")
    private String artistName;
}

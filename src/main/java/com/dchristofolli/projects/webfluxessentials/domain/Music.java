package com.dchristofolli.projects.webfluxessentials.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("music")
@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class Music {
    @Id
    private String id;

    private String songName;

    private String artistId;
}

package com.dchristofolli.projects.webfluxessentials.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document("artist")
@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class Artist {
    @Id
    private String id;

    @NotBlank(message = "The name of artist must be filled")
    private String artistName;
}

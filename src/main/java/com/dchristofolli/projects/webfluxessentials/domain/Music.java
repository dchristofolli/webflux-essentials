package com.dchristofolli.projects.webfluxessentials.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;

@Table("music")
@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class Music {
    @Id
    private Integer id;

    @NotBlank(message = "The name of this music must be filled")
    private String name;
}

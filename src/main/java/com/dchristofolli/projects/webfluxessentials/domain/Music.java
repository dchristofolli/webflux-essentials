package com.dchristofolli.projects.webfluxessentials.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;

@Table("music")
@AllArgsConstructor
@NoArgsConstructor
@Data
@With
public class Music {
    @Id
    private Integer id;

    @NotBlank(message = "The name of this music must be filled")
    private String name;
}

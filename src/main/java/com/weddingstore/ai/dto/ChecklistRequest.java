package com.weddingstore.ai.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ChecklistRequest {

    @NotBlank
    private String city;

    @NotNull
    @Future
    private LocalDate weddingDate;

    @NotNull
    @Min(1)
    private Integer guests;

    private String weddingType;

    private String currentStage;

    private String completedTasks;

    private String preferences;
}
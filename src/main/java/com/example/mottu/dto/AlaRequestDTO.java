package com.example.mottu.dto;

import jakarta.validation.constraints.NotBlank;

public record AlaRequestDTO(
        @NotBlank
        String nome
) {
}

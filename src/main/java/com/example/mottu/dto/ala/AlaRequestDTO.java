package com.example.mottu.dto.ala;

import jakarta.validation.constraints.NotBlank;

public record AlaRequestDTO(
        Long id,
        @NotBlank
        String nome
) {
}

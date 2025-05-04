package com.example.mottu.dto;

import java.util.List;

public record AlaResponseDTO(
        Long id,
        String nome,
        List<MotoResponseDTO> motos
) {
}

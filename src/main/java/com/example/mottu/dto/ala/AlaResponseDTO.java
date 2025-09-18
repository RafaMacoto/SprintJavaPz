package com.example.mottu.dto.ala;

import com.example.mottu.dto.moto.MotoResponseDTO;


import java.util.List;

public record AlaResponseDTO(
        Long id,
        String nome,
        List<MotoResponseDTO> motos
) {
}

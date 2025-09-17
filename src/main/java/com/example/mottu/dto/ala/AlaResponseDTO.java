package com.example.mottu.dto.ala;

import com.example.mottu.dto.moto.MotoResponseDTO;
import com.example.mottu.model.moto.Moto;

import java.util.List;

public record AlaResponseDTO(
        Long id,
        String nome,
        List<Moto> motos
) {
}

package com.example.mottu.dto.moto;

import com.example.mottu.model.moto.Status;

public record MotoResponseDTO(
        Long id,
        String modelo,
        Status status,
        String posicao,
        String problema,
        String placa,
        Long alaId,
        String nomeAla
) {
}

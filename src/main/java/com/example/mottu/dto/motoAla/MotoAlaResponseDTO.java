package com.example.mottu.dto.motoAla;

import com.example.mottu.dto.ala.AlaResponseDTO;
import com.example.mottu.model.moto.Status;

public record MotoAlaResponseDTO(
        Long motoId,
        String modelo,
        Status status,
        String placa,
        AlaResponseDTO ala
) {
}

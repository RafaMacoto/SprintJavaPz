package com.example.mottu.dto;

import com.example.mottu.model.moto.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MotoRequestDTO(

        @NotBlank(message = "Modelo da moto não pode ser nulo")
        String modelo,

        @NotNull
        Status status,

        String posicao,

        String problema,

        String placa,

        double latitude,

        double longitude,

        Long alaId
) {
}

package com.example.mottu.dto.moto;

import com.example.mottu.model.moto.Status;

public record MotoFilter(
        Status status,
        Long alaId,
        String placa
) {
}

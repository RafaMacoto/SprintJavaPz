package com.example.mottu.model.moto;

import java.util.Optional;

public record MotoFilter(
        Status status,
        Long alaId,
        String placa
) {
}

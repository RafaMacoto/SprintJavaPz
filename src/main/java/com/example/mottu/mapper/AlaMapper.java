package com.example.mottu.mapper;

import com.example.mottu.dto.AlaRequestDTO;
import com.example.mottu.dto.AlaResponseDTO;
import com.example.mottu.dto.MotoResponseDTO;
import com.example.mottu.model.ala.Ala;

import java.util.List;
import java.util.stream.Collectors;

public class AlaMapper {

    public static Ala toEntity(AlaRequestDTO request) {
        Ala ala = new Ala();
        ala.setNome(request.nome());
        return ala;
    }

    public static AlaResponseDTO toResponse(Ala ala) {
        List<MotoResponseDTO> motos = ala.getMotos() != null
                ? ala.getMotos().stream()
                .map(MotoMapper::toMotoDTO)
                .collect(Collectors.toList())
                : List.of();

        return new AlaResponseDTO(ala.getId(), ala.getNome(), motos);
    }
}

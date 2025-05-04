package com.example.mottu.mapper;

import com.example.mottu.dto.MotoResponseDTO;
import com.example.mottu.model.moto.Moto;
import org.springframework.stereotype.Component;

@Component
public class MotoMapper {

    public static MotoResponseDTO toMotoDTO(Moto moto) {

        return new MotoResponseDTO(
                moto.getId(),
                moto.getModelo(),
                moto.getStatus(),
                moto.getPosicao(),
                moto.getProblema(),
                moto.getPlaca(),
                moto.getLatitude(),
                moto.getLongitude(),
                moto.getAla() != null ? moto.getAla().getId() : null,
                moto.getAla() != null ? moto.getAla().getNome() : null
        );
    }
}

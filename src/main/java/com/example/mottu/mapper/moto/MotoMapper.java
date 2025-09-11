package com.example.mottu.mapper.moto;

import com.example.mottu.dto.moto.MotoRequestDTO;
import com.example.mottu.dto.moto.MotoResponseDTO;
import com.example.mottu.model.ala.Ala;
import com.example.mottu.model.moto.Moto;

public class MotoMapper {

    public static MotoResponseDTO toMotoDTO(Moto moto) {
        return new MotoResponseDTO(
                moto.getId(),
                moto.getModelo(),
                moto.getStatus(),
                moto.getPosicao(),
                moto.getProblema(),
                moto.getPlaca(),
                moto.getAla() != null ? moto.getAla().getId() : null,
                moto.getAla() != null ? moto.getAla().getNome() : null
        );
    }


    public static Moto toMoto(MotoRequestDTO dto, Ala ala) {
        Moto moto = new Moto();
        moto.setModelo(dto.modelo());
        moto.setStatus(dto.status());
        moto.setPosicao(dto.posicao());
        moto.setProblema(dto.problema());
        moto.setPlaca(dto.placa());
        moto.setAla(ala);
        return moto;
    }


    public static void updateMotoFromDTO(Moto moto, MotoRequestDTO dto, Ala ala) {
        moto.setModelo(dto.modelo());
        moto.setStatus(dto.status());
        moto.setPosicao(dto.posicao());
        moto.setProblema(dto.problema());
        moto.setPlaca(dto.placa());
        moto.setAla(ala);
    }
}

package com.example.mottu.service.motoAla;

import com.example.mottu.dto.ala.AlaResponseDTO;
import com.example.mottu.dto.motoAla.MotoAlaRequestDTO;
import com.example.mottu.dto.motoAla.MotoAlaResponseDTO;
import com.example.mottu.mapper.moto.MotoMapper;
import com.example.mottu.model.ala.Ala;
import com.example.mottu.model.moto.Moto;
import com.example.mottu.repository.AlaRepository;
import com.example.mottu.repository.MotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


import java.util.stream.Collectors;

@Service
public class MotoAlaService implements IMotoAlaService {

    private final MotoRepository motoRepository;
    private final AlaRepository alaRepository;

    public MotoAlaService(MotoRepository motoRepository, AlaRepository alaRepository) {
        this.motoRepository = motoRepository;
        this.alaRepository = alaRepository;
    }

    public MotoAlaResponseDTO vincularMotoAla(MotoAlaRequestDTO dto) {
        Moto moto = motoRepository.findById(dto.motoId())
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada"));

        Ala ala = alaRepository.findById(dto.alaId())
                .orElseThrow(() -> new EntityNotFoundException("Ala não encontrada"));

        moto.setAla(ala);
        Moto salvo = motoRepository.save(moto);

        var motos = ala.getMotos().stream().map(MotoMapper::toMotoDTO).collect(Collectors.toList());

        return new MotoAlaResponseDTO(
                salvo.getId(),
                salvo.getModelo(),
                salvo.getStatus(),
                salvo.getPlaca(),
                new AlaResponseDTO(ala.getId(), ala.getNome(), motos)
        );
    }
}

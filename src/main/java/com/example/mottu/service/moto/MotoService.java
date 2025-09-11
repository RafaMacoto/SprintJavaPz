package com.example.mottu.service.moto;

import com.example.mottu.dto.moto.MotoRequestDTO;
import com.example.mottu.dto.moto.MotoResponseDTO;
import com.example.mottu.mapper.moto.MotoMapper;
import com.example.mottu.model.ala.Ala;
import com.example.mottu.model.moto.Moto;
import com.example.mottu.dto.moto.MotoFilter;
import com.example.mottu.repository.AlaRepository;
import com.example.mottu.repository.MotoRepository;
import com.example.mottu.specification.MotoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MotoService implements IMotoService {

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private AlaRepository alaRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<MotoResponseDTO> listar(MotoFilter filter, Pageable pageable) {
        var specification = MotoSpecification.withFilters(filter);
        return motoRepository.findAll(specification, pageable)
                .map(MotoMapper::toMotoDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public MotoResponseDTO buscarPorId(Long id) {
        Moto moto = getMoto(id);
        return MotoMapper.toMotoDTO(moto);
    }

    @Override
    @Transactional
    public MotoResponseDTO criar(MotoRequestDTO dto) {
        Ala ala = dto.alaId() != null ? getAla(dto.alaId()) : null;
        Moto moto = MotoMapper.toMoto(dto, ala);
        Moto salvo = motoRepository.save(moto);
        return MotoMapper.toMotoDTO(salvo);
    }

    @Override
    @Transactional
    public MotoResponseDTO atualizar(Long id, MotoRequestDTO dto) {
        Moto moto = getMoto(id);
        Ala ala = dto.alaId() != null ? getAla(dto.alaId()) : null;
        MotoMapper.updateMotoFromDTO(moto, dto, ala);
        Moto atualizado = motoRepository.save(moto);
        return MotoMapper.toMotoDTO(atualizado);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Moto moto = getMoto(id);
        motoRepository.delete(moto);
    }


    private Moto getMoto(Long id) {
        return motoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Moto não encontrada com id: " + id));
    }

    private Ala getAla(Long id) {
        return alaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ala não encontrada com id: " + id));
    }
}

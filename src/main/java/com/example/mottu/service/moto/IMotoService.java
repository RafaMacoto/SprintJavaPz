package com.example.mottu.service.moto;

import com.example.mottu.dto.moto.MotoRequestDTO;
import com.example.mottu.dto.moto.MotoResponseDTO;
import com.example.mottu.dto.moto.MotoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMotoService {

    Page<MotoResponseDTO> listar(MotoFilter filter, Pageable pageable);

    MotoResponseDTO buscarPorId(Long id);

    MotoResponseDTO criar(MotoRequestDTO dto);

    MotoResponseDTO atualizar(Long id, MotoRequestDTO dto);

    void deletar(Long id);
}

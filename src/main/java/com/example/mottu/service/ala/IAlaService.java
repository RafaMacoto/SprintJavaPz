package com.example.mottu.service.ala;

import com.example.mottu.dto.ala.AlaFilter;
import com.example.mottu.dto.ala.AlaRequestDTO;
import com.example.mottu.dto.ala.AlaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAlaService {

    Page<AlaResponseDTO> list(AlaFilter filter, Pageable pageable);
    AlaResponseDTO getById(Long id);
    AlaResponseDTO create(AlaRequestDTO request);
    AlaResponseDTO update(Long id, AlaRequestDTO request);
    void delete(Long id);
}

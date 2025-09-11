package com.example.mottu.service.ala;

import com.example.mottu.dto.ala.AlaFilter;
import com.example.mottu.dto.ala.AlaRequestDTO;
import com.example.mottu.dto.ala.AlaResponseDTO;
import com.example.mottu.exception.NotFoundException;
import com.example.mottu.mapper.ala.AlaMapper;
import com.example.mottu.model.ala.Ala;
import com.example.mottu.repository.AlaRepository;
import com.example.mottu.specification.AlaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlaService implements IAlaService {

    @Autowired
    private AlaRepository repository;

    @Override
    public Page<AlaResponseDTO> list(AlaFilter filter, Pageable pageable) {
        var specification = AlaSpecification.withFilters(filter);
        return repository.findAll(specification, pageable)
                .map(AlaMapper::toResponse);
    }

    @Override
    public AlaResponseDTO getById(Long id) {
        return AlaMapper.toResponse(getAla(id));
    }

    @Override
    public AlaResponseDTO create(AlaRequestDTO request) {
        Ala ala = AlaMapper.toEntity(request);
        Ala saved = repository.save(ala);
        return AlaMapper.toResponse(saved);
    }

    @Override
    public AlaResponseDTO update(Long id, AlaRequestDTO request) {
        Ala ala = getAla(id);
        ala.setNome(request.nome());
        Ala updated = repository.save(ala);
        return AlaMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        Ala ala = getAla(id);
        repository.delete(ala);
    }

    private Ala getAla(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("id", "Ala não encontrada com o id: " + id));
    }
}

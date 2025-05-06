package com.example.mottu.controller;

import com.example.mottu.dto.AlaRequestDTO;
import com.example.mottu.dto.AlaResponseDTO;
import com.example.mottu.exception.NotFoundException;
import com.example.mottu.mapper.AlaMapper;
import com.example.mottu.model.ala.Ala;
import com.example.mottu.model.ala.AlaFilter;
import com.example.mottu.repository.AlaRepository;
import com.example.mottu.specification.AlaSpecification;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("alas")
public class AlaController {

    @Autowired
    private AlaRepository repository;

    @GetMapping
    @Cacheable("alas")
    public ResponseEntity<Page<AlaResponseDTO>> index(
            @ModelAttribute AlaFilter filter,
            @PageableDefault(size = 5, sort = "nome") Pageable pageable
    ) {
        var specification = AlaSpecification.withFilters(filter);
        Page<Ala> alas = repository.findAll(specification, pageable);
        return ResponseEntity.ok(alas.map(AlaMapper::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlaResponseDTO> findById(@PathVariable Long id) {
        Ala ala = getAla(id);
        return ResponseEntity.ok(AlaMapper.toResponse(ala));
    }

    @PostMapping
    @CacheEvict(value = "alas", allEntries = true)
    public ResponseEntity<AlaResponseDTO> create(@RequestBody @Valid AlaRequestDTO request) {
        Ala ala = AlaMapper.toEntity(request);
        Ala saved = repository.save(ala);
        return ResponseEntity.status(HttpStatus.CREATED).body(AlaMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "alas", allEntries = true)
    public ResponseEntity<AlaResponseDTO> update(@PathVariable Long id, @RequestBody @Valid AlaRequestDTO request) {
        Ala ala = getAla(id);
        ala.setNome(request.nome());
        Ala updated = repository.save(ala);
        return ResponseEntity.ok(AlaMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "alas", allEntries = true)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Ala ala = getAla(id);
        repository.delete(ala);
        return ResponseEntity.noContent().build();
    }

    private Ala getAla(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("id", "Ala não encontrada com o id: " + id));
    }
}

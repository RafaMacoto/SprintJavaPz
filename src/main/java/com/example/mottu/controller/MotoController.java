package com.example.mottu.controller;

import com.example.mottu.dto.MotoRequestDTO;
import com.example.mottu.dto.MotoResponseDTO;
import com.example.mottu.exception.NotFoundException;
import com.example.mottu.mapper.MotoMapper;
import com.example.mottu.model.ala.Ala;
import com.example.mottu.model.moto.Moto;
import com.example.mottu.model.moto.MotoFilter;
import com.example.mottu.repository.AlaRepository;
import com.example.mottu.repository.MotoRepository;
import com.example.mottu.specification.MotoSpecification;
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
@RequestMapping("motos")
public class MotoController {

    @Autowired
    private MotoRepository repository;

    @Autowired
    private AlaRepository alaRepository;

    @GetMapping
    @Cacheable("motos")
    public ResponseEntity<Page<MotoResponseDTO>> index(
            @ModelAttribute MotoFilter filter,
            @PageableDefault(size = 5, sort = "id") Pageable pageable
    ){
        var especification = MotoSpecification.withFilters(filter);
        Page<Moto> motos = repository.findAll(especification, pageable);
        return ResponseEntity.ok(motos.map(MotoMapper::toMotoDTO));

    }

    @GetMapping("/{id}")
    public ResponseEntity<MotoResponseDTO> findById(@PathVariable Long id) {
        Moto moto = getMoto(id);
        return ResponseEntity.ok(MotoMapper.toMotoDTO(moto));
    }

    @PostMapping
    @CacheEvict(value = "motos", allEntries = true)
    public ResponseEntity<MotoResponseDTO> create(@RequestBody @Valid MotoRequestDTO dto) {
        Moto moto = new Moto();
        moto.setModelo(dto.modelo());
        moto.setStatus(dto.status());
        moto.setPosicao(dto.posicao());
        moto.setProblema(dto.problema());
        moto.setPlaca(dto.placa());
        moto.setLatitude(dto.latitude());
        moto.setLongitude(dto.longitude());

        if (dto.alaId() != null) {
            moto.setAla(getAla(dto.alaId()));
        }

        Moto saved = repository.save(moto);
        return ResponseEntity.status(HttpStatus.CREATED).body(MotoMapper.toMotoDTO(saved));
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "motos", allEntries = true)
    public ResponseEntity<MotoResponseDTO> update(@PathVariable Long id, @RequestBody @Valid MotoRequestDTO dto) {
        Moto moto = getMoto(id);

        moto.setModelo(dto.modelo());
        moto.setStatus(dto.status());
        moto.setPosicao(dto.posicao());
        moto.setProblema(dto.problema());
        moto.setPlaca(dto.placa());
        moto.setLatitude(dto.latitude());
        moto.setLongitude(dto.longitude());

        if (dto.alaId() != null) {
            moto.setAla(getAla(dto.alaId()));
        } else {
            moto.setAla(null);
        }

        Moto updated = repository.save(moto);
        return ResponseEntity.ok(MotoMapper.toMotoDTO(updated));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "motos", allEntries = true)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Moto moto = getMoto(id);
        repository.delete(moto);
        return ResponseEntity.noContent().build();
    }

    private Ala getAla(Long id){
        return alaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id", "Ala não encontrada com o id: " + id));
    }

    private Moto getMoto(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("id", "Moto não encontrada com o id: " + id));
    }


}

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Listar motos",
            description = "Retorna uma lista paginada de motos com base nos filtros fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
            })
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

    @Operation(
            summary = "Buscar moto por ID",
            description = "Retorna os dados da moto com o ID informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Moto encontrada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Moto não encontrada")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MotoResponseDTO> findById(@PathVariable Long id) {
        Moto moto = getMoto(id);
        return ResponseEntity.ok(MotoMapper.toMotoDTO(moto));
    }

    @Operation(
            summary = "Cadastrar nova moto",
            description = "Cria uma nova moto no sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Moto criada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos para criação da moto")
            }
    )
    @PostMapping
    @CacheEvict(value = "motos", allEntries = true)
    public ResponseEntity<MotoResponseDTO> create(@RequestBody @Valid MotoRequestDTO dto) {
        System.out.println("Recebido alaId no DTO: " + dto.alaId());

        Moto moto = new Moto();
        moto.setModelo(dto.modelo());
        moto.setStatus(dto.status());
        moto.setPosicao(dto.posicao());
        moto.setProblema(dto.problema());
        moto.setPlaca(dto.placa());

        if (dto.alaId() != null) {
            Ala ala = getAla(dto.alaId());
            moto.setAla(ala);
        }

        Moto saved = repository.save(moto);

        return ResponseEntity.status(HttpStatus.CREATED).body(MotoMapper.toMotoDTO(saved));
    }

    @Operation(
            summary = "Atualizar moto",
            description = "Atualiza os dados da moto com o ID informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Moto atualizada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Moto não encontrada"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização")
            }
    )
    @PutMapping("/{id}")
    @CacheEvict(value = "motos", allEntries = true)
    public ResponseEntity<MotoResponseDTO> update(@PathVariable Long id, @RequestBody @Valid MotoRequestDTO dto) {
        Moto moto = getMoto(id);

        moto.setModelo(dto.modelo());
        moto.setStatus(dto.status());
        moto.setPosicao(dto.posicao());
        moto.setProblema(dto.problema());
        moto.setPlaca(dto.placa());

        if (dto.alaId() != null) {
            moto.setAla(getAla(dto.alaId()));
        } else {
            moto.setAla(null);
        }

        Moto updated = repository.save(moto);
        return ResponseEntity.ok(MotoMapper.toMotoDTO(updated));
    }

    @Operation(
            summary = "Excluir moto",
            description = "Exclui a moto com o ID informado.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Moto excluída com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Moto não encontrada")
            }
    )
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

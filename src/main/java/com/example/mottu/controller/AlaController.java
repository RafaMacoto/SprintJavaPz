package com.example.mottu.controller;

import com.example.mottu.dto.ala.AlaRequestDTO;
import com.example.mottu.dto.ala.AlaResponseDTO;
import com.example.mottu.exception.NotFoundException;
import com.example.mottu.mapper.ala.AlaMapper;
import com.example.mottu.model.ala.Ala;
import com.example.mottu.dto.ala.AlaFilter;
import com.example.mottu.repository.AlaRepository;
import com.example.mottu.service.ala.AlaService;
import com.example.mottu.service.ala.IAlaService;
import com.example.mottu.specification.AlaSpecification;
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
@RequestMapping("alas")
public class AlaController {

    @Autowired
    private IAlaService service;

    @Operation(
            summary = "Listar alas",
            description = "Retorna uma lista paginada de alas com base nos filtros fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
            }
    )
    @GetMapping
    @Cacheable("alas")
    public ResponseEntity<Page<AlaResponseDTO>> index(
            @ModelAttribute AlaFilter filter,
            @PageableDefault(size = 5, sort = "nome") Pageable pageable
    ) {
        return ResponseEntity.ok(service.list(filter, pageable));
    }

    @Operation(
            summary = "Buscar ala por ID",
            description = "Retorna os dados da ala com o ID informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ala encontrada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Ala não encontrada")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AlaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(
            summary = "Cadastrar nova ala",
            description = "Cria uma nova ala no sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ala criada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos para criação da ala")
            }
    )
    @PostMapping
    @CacheEvict(value = "alas", allEntries = true)
    public ResponseEntity<AlaResponseDTO> create(@RequestBody @Valid AlaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(
            summary = "Atualizar ala",
            description = "Atualiza os dados da ala com o ID informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ala atualizada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Ala não encontrada"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização")
            }
    )
    @PutMapping("/{id}")
    @CacheEvict(value = "alas", allEntries = true)
    public ResponseEntity<AlaResponseDTO> update(@PathVariable Long id, @RequestBody @Valid AlaRequestDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(
            summary = "Excluir ala",
            description = "Exclui a ala com o ID informado.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Ala excluída com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Ala não encontrada")
            }
    )
    @DeleteMapping("/{id}")
    @CacheEvict(value = "alas", allEntries = true)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

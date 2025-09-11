package com.example.mottu.controller;

import com.example.mottu.dto.moto.MotoRequestDTO;
import com.example.mottu.dto.moto.MotoResponseDTO;
import com.example.mottu.dto.moto.MotoFilter;
import com.example.mottu.service.moto.IMotoService;
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

        private final IMotoService motoService;

        @Autowired
        public MotoController(IMotoService motoService) {
            this.motoService = motoService;
        }

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
            Page<MotoResponseDTO> motos = motoService.listar(filter, pageable);
            return ResponseEntity.ok(motos);
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
            MotoResponseDTO motoDTO = motoService.buscarPorId(id);
            return ResponseEntity.ok(motoDTO);
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
            MotoResponseDTO salvo = motoService.criar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
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
            MotoResponseDTO atualizado = motoService.atualizar(id, dto);
            return ResponseEntity.ok(atualizado);
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
            motoService.deletar(id);
            return ResponseEntity.noContent().build();
        }

}

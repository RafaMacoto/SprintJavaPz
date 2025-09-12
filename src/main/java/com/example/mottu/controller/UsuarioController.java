package com.example.mottu.controller;

import com.example.mottu.dto.usuario.UsuarioFilter;
import com.example.mottu.dto.usuario.UsuarioRequestDTO;
import com.example.mottu.dto.usuario.UsuarioResponseDTO;
import com.example.mottu.dto.usuario.UsuarioUpdateDTO;
import com.example.mottu.model.usuario.Usuario;
import com.example.mottu.service.usuario.IUsuarioService;
import com.example.mottu.service.usuario.UsuarioService;
import com.example.mottu.specification.UsuarioSpecification;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Operation(
            summary = "Criar novo usuário",
            description = "Cadastra um novo usuário no sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PostMapping
    @CacheEvict(value = "usuarios", allEntries = true)
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO criado = usuarioService.createUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(
            summary = "Listar usuários",
            description = "Retorna uma lista paginada de usuários.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
            }
    )
    @GetMapping
    @Cacheable("usuarios")
    public ResponseEntity<Page<UsuarioResponseDTO>> listarUsuarios(
            @ModelAttribute UsuarioFilter filter,
            @AuthenticationPrincipal Usuario logado,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        var specification = UsuarioSpecification.withFilters(filter);
        Page<UsuarioResponseDTO> resultado = usuarioService.listarUsuarios(logado, pageable, filter);
        return ResponseEntity.ok(resultado);
    }

    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os dados do usuário com o ID informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuario(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario logado) {
        UsuarioResponseDTO dto = usuarioService.buscarPorId(id, logado);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza os dados do usuário com o ID informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    @PutMapping("/{id}")
    @CacheEvict(value = "usuarios", allEntries = true)
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateDTO dto,
            @AuthenticationPrincipal Usuario logado) {
        UsuarioResponseDTO atualizado = usuarioService.atualizarUsuario(id, dto, logado);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(
            summary = "Deletar usuário",
            description = "Exclui o usuário com o ID informado.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    @CacheEvict(value = "usuarios", allEntries = true)
    public ResponseEntity<Void> deletarUsuario(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario logado) {
        usuarioService.deletarUsuario(id, logado);
        return ResponseEntity.noContent().build();
    }
}

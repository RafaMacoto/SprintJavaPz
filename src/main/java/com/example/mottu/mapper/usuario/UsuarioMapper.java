package com.example.mottu.mapper.usuario;

import com.example.mottu.dto.usuario.UsuarioRequestDTO;
import com.example.mottu.dto.usuario.UsuarioResponseDTO;
import com.example.mottu.dto.usuario.UsuarioUpdateDTO;
import com.example.mottu.model.usuario.Usuario;

import java.util.ArrayList;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequestDTO dto) {
        return Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .role(dto.role())
                .senha(dto.senha())
                .build();
    }

    public static UsuarioResponseDTO toDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole().toString()
        );
    }

    public static void updateEntity(Usuario u, UsuarioUpdateDTO dto) {
        u.setNome(dto.nome());
    }
}

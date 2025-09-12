package com.example.mottu.dto.usuario;

import java.util.List;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String role
) {
}

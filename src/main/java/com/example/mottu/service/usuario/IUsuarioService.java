package com.example.mottu.service.usuario;

import com.example.mottu.dto.usuario.UsuarioFilter;
import com.example.mottu.dto.usuario.UsuarioRequestDTO;
import com.example.mottu.dto.usuario.UsuarioResponseDTO;
import com.example.mottu.dto.usuario.UsuarioUpdateDTO;
import com.example.mottu.model.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUsuarioService {

    UsuarioResponseDTO createUsuario(UsuarioRequestDTO dto);

    Page<UsuarioResponseDTO> listarUsuarios(Usuario logado, Pageable pageable, UsuarioFilter filter);

    UsuarioResponseDTO buscarPorId(Long id, Usuario logado);

    UsuarioResponseDTO atualizarUsuario(Long id, UsuarioUpdateDTO dto, Usuario logado);

    void deletarUsuario(Long id, Usuario logado);
}

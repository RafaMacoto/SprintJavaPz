package com.example.mottu;

import com.example.mottu.dto.usuario.UsuarioRequestDTO;
import com.example.mottu.dto.usuario.UsuarioResponseDTO;
import com.example.mottu.mapper.usuario.UsuarioMapper;
import com.example.mottu.model.usuario.RoleUsuario;
import com.example.mottu.model.usuario.Usuario;
import com.example.mottu.repository.UsuarioRepository;
import com.example.mottu.service.usuario.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("rafael@email.com");
        usuario.setNome("Rafael");
        usuario.setRole(RoleUsuario.USER);
    }

    @Test
    void deveCriarUsuarioComSenhaCriptografada() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Rafael", "rafael@email.com", RoleUsuario.USER, "123456");
        when(repository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encoded");
        when(repository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        UsuarioResponseDTO response = usuarioService.createUsuario(dto);

        assertNotNull(response);
        verify(passwordEncoder).encode("123456");
        verify(repository).save(any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoSeEmailJaExistir() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Rafael", "rafael@email.com", RoleUsuario.USER, "123456");
        when(repository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> usuarioService.createUsuario(dto));
    }

    @Test
    void deveLancarAccessDeniedAoListarSemPermissao() {
        Usuario comum = new Usuario();
        comum.setId(2L);
        comum.setRole(RoleUsuario.USER);

        assertThrows(AccessDeniedException.class, () ->
                usuarioService.listarUsuarios(comum, null, null));
    }

    @Test
    void deveLancarEntityNotFoundAoBuscarUsuarioInexistente() {
        when(repository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                usuarioService.buscarPorId(1L, usuario));
    }
}

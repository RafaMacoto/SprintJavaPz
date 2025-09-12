package com.example.mottu.service.usuario;

import com.example.mottu.dto.usuario.UsuarioFilter;
import com.example.mottu.dto.usuario.UsuarioRequestDTO;
import com.example.mottu.dto.usuario.UsuarioResponseDTO;
import com.example.mottu.dto.usuario.UsuarioUpdateDTO;
import com.example.mottu.mapper.usuario.UsuarioMapper;
import com.example.mottu.model.usuario.RoleUsuario;
import com.example.mottu.model.usuario.Usuario;
import com.example.mottu.repository.UsuarioRepository;
import com.example.mottu.specification.UsuarioSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UsuarioResponseDTO createUsuario(UsuarioRequestDTO dto) {

        if (repository.existsByEmail(dto.email())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario u = UsuarioMapper.toEntity(dto);
        u.setSenha(passwordEncoder.encode(dto.senha()));;
        Usuario salvo = repository.save(u);

        return UsuarioMapper.toDTO(salvo);
    }

    public Page<UsuarioResponseDTO> listarUsuarios(Usuario logado, Pageable pageable, UsuarioFilter filter) {
        if (!isAdmin(logado)) {
            throw new AccessDeniedException("Somente admin pode listar todos usuários");
        }
        Specification<Usuario> spec = UsuarioSpecification.withFilters(filter);

        return repository.findAll(spec, pageable)
                .map(UsuarioMapper::toDTO);
    }

    public UsuarioResponseDTO buscarPorId(Long id, Usuario logado) {
        Usuario u = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        if (!logado.getId().equals(id) && !isAdmin(logado)) {
            throw new AccessDeniedException("Você só pode acessar seu próprio usuário");
        }
        return UsuarioMapper.toDTO(u);
    }

    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioUpdateDTO dto, Usuario logado) {
        Usuario u = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (!logado.getId().equals(id) && !isAdmin(logado)) {
            throw new AccessDeniedException("Você só pode atualizar seu próprio usuário");
        }

        UsuarioMapper.updateEntity(u, dto);
        Usuario salvo = repository.save(u);

        return UsuarioMapper.toDTO(salvo);
    }

    public void deletarUsuario(Long id, Usuario logado) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        if (!logado.getId().equals(id) && !isAdmin(logado)) {
            throw new AccessDeniedException("Você só pode deletar seu próprio usuário");
        }
        repository.deleteById(id);
    }

    private boolean isAdmin(Usuario usuario) {
        return usuario.getRole() == RoleUsuario.ADMIN;
    }
}

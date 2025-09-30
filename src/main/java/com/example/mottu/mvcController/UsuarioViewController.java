package com.example.mottu.mvcController;

import com.example.mottu.dto.usuario.UsuarioFilter;
import com.example.mottu.dto.usuario.UsuarioRequestDTO;
import com.example.mottu.dto.usuario.UsuarioResponseDTO;
import com.example.mottu.dto.usuario.UsuarioUpdateDTO;
import com.example.mottu.model.usuario.Usuario;
import com.example.mottu.service.usuario.IUsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("usuarios-view")
public class UsuarioViewController {

    private final IUsuarioService usuarioService;

    @Autowired
    public UsuarioViewController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model,
                         @AuthenticationPrincipal Usuario logado,
                         @ModelAttribute UsuarioFilter filter,
                         @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        if (logado == null) {
            return "redirect:/login";
        }

        Page<UsuarioResponseDTO> usuariosPage;

        if (logado.getRole().name().equals("ADMIN")) {
            usuariosPage = usuarioService.listarUsuariosView(pageable, filter);

        } else {
            UsuarioFilter filtroProprio = new UsuarioFilter(null, logado.getEmail());
            usuariosPage = usuarioService.listarUsuariosView(pageable, filtroProprio);
        }

        model.addAttribute("usuarios", usuariosPage.getContent());
        model.addAttribute("page", usuariosPage);
        model.addAttribute("filter", filter);
        model.addAttribute("logado", logado);

        return "usuarios/index";
    }

    @GetMapping("/cadastro")
    public String formCadastro(@ModelAttribute("usuario") UsuarioRequestDTO usuario, Model model) {
        model.addAttribute("roles", com.example.mottu.model.usuario.RoleUsuario.values());
        return "usuarios/cadastro";
    }

    @PostMapping("/cadastro")
    public String salvar(@Valid @ModelAttribute("usuario") UsuarioRequestDTO usuario,
                         BindingResult result,
                         RedirectAttributes redirect) {

        if (result.hasErrors()) {
            return "usuarios/cadastro";
        }

        try {

            usuarioService.createUsuario(usuario);
            redirect.addFlashAttribute("message", "Cadastro realizado com sucesso! Faça o login.");
            return "redirect:/login";

        } catch (RuntimeException e) {
            result.rejectValue("email", "error.usuario", e.getMessage());
            return "usuarios/cadastro";
        }
    }


    @GetMapping("/perfil")
    public String verPerfil(@AuthenticationPrincipal Usuario logado, Model model) {
        if (logado == null) {
            return "redirect:/login";
        }
        UsuarioResponseDTO perfil = usuarioService.buscarPorId(logado.getId(), logado);
        model.addAttribute("perfil", perfil);

        return "usuarios/perfil";
    }

    @GetMapping("/editar/{id}")
    public String formEdicao(@PathVariable Long id,
                             @AuthenticationPrincipal Usuario logado,
                             Model model) {

        UsuarioResponseDTO usuarioDTO = usuarioService.buscarPorId(id, logado);

        UsuarioUpdateDTO updateDTO = new UsuarioUpdateDTO(
                usuarioDTO.nome(),
                usuarioDTO.email()
        );

        model.addAttribute("usuarioId", id);
        model.addAttribute("usuario", updateDTO);
        model.addAttribute("isOwnProfile", logado.getId().equals(id)); // Útil para a view

        return "usuarios/edicao";
    }


    @PostMapping("/editar/{id}")
    public String salvarEdicao(@PathVariable Long id,
                               @Valid @ModelAttribute("usuario") UsuarioUpdateDTO dto,
                               BindingResult result,
                               @AuthenticationPrincipal Usuario logado,
                               RedirectAttributes redirect) {

        if (result.hasErrors()) {
            return "usuarios/edicao";
        }

        try {
            usuarioService.atualizarUsuario(id, dto, logado);
            redirect.addFlashAttribute("message", "Usuário atualizado com sucesso!");


            if (logado.getId().equals(id)) {
                return "redirect:/usuarios-view/perfil";
            }
            return "redirect:/usuarios-view";

        } catch (AccessDeniedException | EntityNotFoundException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuarios-view";
        } catch (RuntimeException e) {
            result.rejectValue("email", "error.usuario", e.getMessage());
            return "usuarios/edicao";
        }
    }
}

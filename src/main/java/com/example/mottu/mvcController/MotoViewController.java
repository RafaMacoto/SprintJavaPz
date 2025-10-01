package com.example.mottu.mvcController;

import com.example.mottu.dto.moto.MotoFilter;
import com.example.mottu.dto.moto.MotoRequestDTO;
import com.example.mottu.dto.moto.MotoResponseDTO;
import com.example.mottu.service.ala.IAlaService;
import com.example.mottu.service.moto.IMotoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/motos-view")
public class MotoViewController {

    private final IMotoService motoService;
    private final IAlaService alaService;

    @Autowired
    public MotoViewController(IMotoService motoService, IAlaService alaService) {
        this.motoService = motoService;
        this.alaService = alaService;
    }

    @GetMapping
    public String listar(Model model,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "20") int size) {
        var motosPage = motoService.listar(new MotoFilter(null, null, null), PageRequest.of(page, size));
        model.addAttribute("motos", motosPage.getContent());
        model.addAttribute("page", motosPage);
        return "motos/index";
    }

    @GetMapping("/form")
    public String form(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            MotoResponseDTO dto = motoService.buscarPorId(id);
            MotoRequestDTO formDto = new MotoRequestDTO(
                    dto.id(),
                    dto.modelo(),
                    dto.status(),
                    dto.posicao(),
                    dto.problema(),
                    dto.placa(),
                    dto.alaId()
            );
            model.addAttribute("moto", formDto);
        } else {
            model.addAttribute("moto", new MotoRequestDTO(null, null, null, null, null, null, null));
        }
        model.addAttribute("alas", alaService.list(null, PageRequest.of(0, 100)).getContent());
        return "motos/form";
    }

    @PostMapping("/form")
    public String salvar(@Valid @ModelAttribute("moto") MotoRequestDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {

        if (result.hasErrors()) {
            model.addAttribute("alas", alaService.list(null, PageRequest.of(0, 100)).getContent());
            return "motos/form";
        }

        if (dto.id() == null) {
            motoService.criar(dto);
            redirect.addFlashAttribute("message", "Moto criada com sucesso!");
        } else {
            motoService.atualizar(dto.id(), dto);
            redirect.addFlashAttribute("message", "Moto atualizada com sucesso!");
        }

        return "redirect:/motos-view";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirect) {
        motoService.deletar(id);
        redirect.addFlashAttribute("message", "Moto deletada com sucesso!");
        return "redirect:/motos-view";
    }
}
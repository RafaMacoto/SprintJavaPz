package com.example.mottu.mvcController;

import com.example.mottu.dto.ala.AlaRequestDTO;
import com.example.mottu.service.ala.IAlaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/alas-view")
public class AlaViewController {

    private final IAlaService alaService;

    @Autowired
    public AlaViewController(IAlaService alaService) {
        this.alaService = alaService;
    }

    @GetMapping
    public String listar(Model model) {
        var alas = alaService.list(null, PageRequest.of(0, 100)).getContent();
        model.addAttribute("alas", alas);
        return "alas/index";
    }

    @GetMapping("/novo")
    public String novo(Model model) {

        model.addAttribute("ala", new AlaRequestDTO(null, ""));
        return "alas/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        var dto = alaService.getById(id);
        model.addAttribute("ala", dto);
        return "alas/form";
    }


    @PostMapping("/form")
    public String salvarOuAtualizar(@Valid @ModelAttribute("ala") AlaRequestDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {

            model.addAttribute("ala", dto);
            return "alas/form";
        }


        if (dto.id() != null) {
            alaService.update(dto.id(), dto);
        } else {
            alaService.create(dto);
        }

        return "redirect:/alas-view";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        alaService.delete(id);
        return "redirect:/alas-view";
    }
}
